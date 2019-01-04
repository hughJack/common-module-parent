package cn.com.flaginfo.redis.id;

import cn.com.flaginfo.exception.ErrorCode;
import cn.com.flaginfo.exception.restful.RestfulException;
import cn.com.flaginfo.module.common.diamond.DiamondProperties;
import cn.com.flaginfo.module.common.utils.TimeUtils;
import cn.com.flaginfo.redis.RedisUtils;
import cn.com.flaginfo.redis.lock.jedis.impl.RedisLockNx;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.PostConstruct;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * 分布式ID生成器
 * 在所有bean初始化完毕之后才初始化，防止相关依赖没有加载，顾在Bean的初始化阶段请勿使用该类
 * @author: Meng.Liu
 * @date: 2018/11/16 上午11:10
 */
@Slf4j
public class DistributeIdGenerator {

    private static final String DISTRIBUTE_ID_GENERATOR_LOCK_KEY = "_DISTRIBUTE_ID_GENERATOR_";
    /**
     * 向Redis服务器注册自己的机器ID
     */
    private static final String DISTRIBUTED_MACHINE_ID_REGISTER = "_DISTRIBUTED_MACHINE_ID_REGISTER_";
    /**
     * 向Redis服务器注册自己的数据ID
     */
    private static final String DISTRIBUTED_DATA_CENTER_ID_REGISTER = "_DISTRIBUTED_DATA_CENTER_ID_REGISTER_";
    private static final String HAD_REGISTER_CENTER_ID = "_DISTRIBUTED_DATA_CENTER_ID_HAD_REGISTER_";

    private static volatile DistributeIdGenerator INSTANCE;

    private final static int RETRY_TIMES = 3;

    protected DistributeIdGenerator() {

    }

    @PostConstruct
    private void instance(){
        INSTANCE = this;
        initSnowFlake();
    }

    /**
     * 获取ID
     * @return
     * @throws RestfulException
     */
    public static Long nextId() throws RestfulException {
        if( null == INSTANCE ){
            log.error("Distribute id generator is not enabled, please set 'enable.distribute.id.generator=true' to open it.");
            throw new RestfulException(ErrorCode.SYS_BUSY.code(), ErrorCode.SYS_BUSY.message());
        }
        return INSTANCE.getSnowFlake().nextId();
    }



    private volatile SnowFlake snowFlake;

    /**
     * 异步初始化
     */
    private void asyncInitSnowFlak(){
        log.info("async init snow flak...");
        new Thread(()->INSTANCE.initSnowFlake()).start();
    }

    private synchronized void initSnowFlake() {
        if( null != snowFlake ){
            return;
        }
        try{
            log.info("init snow flake start...");
            String appName = DiamondProperties.getPropertyString("spring.application.name", "DEFAULT_APPLICATION");
            int dataCenterId = findDataCenterId(appName, 0);
            int machineId = findMachineId(dataCenterId, 0);
            log.info("init snow flake success with data center id [{}] and machine id [{}]", dataCenterId, machineId);
            this.snowFlake = new SnowFlake(dataCenterId, machineId);
        }catch (Exception e){
            log.error("init error, try again after 10 seconds.", e);
            try {
                TimeUnit.SECONDS.sleep(10);
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            }
            initSnowFlake();
        }
    }

    private SnowFlake getSnowFlake() throws RestfulException {
        if (null == this.snowFlake) {
            log.error("id generator is not init...");
            throw new RestfulException(ErrorCode.SYS_BUSY.code(), ErrorCode.SYS_BUSY.message());
        }
        return snowFlake;
    }

    private synchronized void clear() {
        log.warn("clear snowFlake...");
        snowFlake = null;
    }


    /**
     * 获取分布式服务的data center id
     * @param appName
     * @param count
     * @return
     */
    private int findDataCenterId(String appName, int count) {
        String dataCenterKey = RedisUtils.buildKey(DISTRIBUTED_DATA_CENTER_ID_REGISTER, appName);
        RedisLockNx lockNx = new RedisLockNx(DISTRIBUTE_ID_GENERATOR_LOCK_KEY, 60L * 1000);
        if (lockNx.lock()) {
            int dataCenterId = 0;
            try {
                Object objDataCenterId = RedisUtils.selectDatabase(0).getValue(dataCenterKey);
                if (null == objDataCenterId) {
                    Set<Object> objectSet = RedisUtils.selectDatabase(0).sMembers(HAD_REGISTER_CENTER_ID);
                    if (null != objectSet && SnowFlake.MAX_DATACENTER_NUM < objectSet.size()) {
                        throw new RuntimeException("Distribute Id Generator init error. The Data Center ID assignment is full and cannot be assigned.");
                    }
                    if (null != objectSet) {
                        boolean hasFoundNextDataCenterId = false;
                        for (int i = 0; i <= SnowFlake.MAX_DATACENTER_NUM; i++) {
                            if (objectSet.contains(i)) {
                                continue;
                            }
                            dataCenterId = i;
                            hasFoundNextDataCenterId = true;
                            break;
                        }
                        if (!hasFoundNextDataCenterId) {
                            throw new RuntimeException("Cannot find a new data center id for this application");
                        }
                    }
                    RedisUtils.selectDatabase(0).addSetValue(HAD_REGISTER_CENTER_ID, dataCenterId);
                    RedisUtils.selectDatabase(0).addValue(dataCenterKey, dataCenterId);
                }
            } finally {
                lockNx.unlock();
            }
            return dataCenterId;
        } else {
            if (count < RETRY_TIMES) {
                log.warn("get distribute id generation lock timeout, try it again." );
                return findDataCenterId(appName, count + 1);
            }
            throw new RuntimeException("init distribute error, get distribute id generation lock timeout...");
        }

    }

    /**
     * 获取分布式服务的机器id
     * @param dataCenterId
     * @param count
     * @return
     */
    private int findMachineId(int dataCenterId, int count) {
        RedisLockNx lockNx = new RedisLockNx(RedisUtils.buildKey(DISTRIBUTE_ID_GENERATOR_LOCK_KEY, dataCenterId));
        if (lockNx.lock()) {
            try {
                String registerKey = null;
                int machineId = 0;
                boolean hadFoundNextMachineId = false;
                for (int i = 0; i <= SnowFlake.MAX_MACHINE_NUM; i++) {
                    registerKey = RedisUtils.buildKey(DISTRIBUTED_MACHINE_ID_REGISTER, dataCenterId, i);
                    if (RedisUtils.selectDatabase(0).hasKey(registerKey)) {
                        continue;
                    }
                    machineId = i;
                    hadFoundNextMachineId = true;
                    break;
                }
                if (!hadFoundNextMachineId) {
                    throw new RuntimeException("Distribute Id Generator init error. The Machine ID assignment is full and cannot be assigned for the data center id [" + dataCenterId + "].");
                }
                //设置60秒过期
                RedisUtils.selectDatabase(0).addValue(registerKey, TimeUtils.currentTimeStr(), 60, TimeUnit.SECONDS);
                //启动心跳测试
                new HeartbeatThread(registerKey, dataCenterId, machineId).start();
                return machineId;
            } finally {
                lockNx.unlock();
            }
        }else {
            if (count < RETRY_TIMES) {
                log.warn("get data center lock timeout, try it again." );
                return findMachineId(dataCenterId, count + 1);
            }
            throw new RuntimeException("init distribute error, get data center lock timeout...");
        }
    }

    private static class HeartbeatThread extends Thread {

        private String registerKey;
        private long dataCenterId;
        private long machineId;
        private int defaultSleepTime = 30;

        HeartbeatThread(String registerKey, long dataCenterId, long machineId) {
            this.registerKey = registerKey;
            this.dataCenterId = dataCenterId;
            this.machineId = machineId;
            this.setDaemon(true);
        }

        @Override
        public void run() {
            int retryTime = 0;
            int sleepTime;
            while (true) {
                if( retryTime > 3 ){
                    log.error("Distribute Id Generator heartbeat : Failed. had tried it three times. init id generation again.");
                    //异步重新注册一次，如果失败则全局失败
                    INSTANCE.clear();
                    INSTANCE.asyncInitSnowFlak();
                    return;
                }
                try {
                    if (RedisUtils.selectDatabase(0).hasKey(registerKey)) {
                        if( log.isDebugEnabled() ){
                            log.debug("Distribute Id Generator heartbeat : Success. data center [{}], machine [{}]", dataCenterId, machineId);
                        }
                        RedisUtils.selectDatabase(0).expire(registerKey, 60L, TimeUnit.SECONDS);
                        retryTime = 0;
                        sleepTime = defaultSleepTime;
                    } else {
                        log.error("Distribute Id Generator heartbeat : Failed, retry it.");
                        sleepTime = 10;
                        retryTime++;
                    }
                } catch (Exception e) {
                    log.error("", e);
                    sleepTime = 5;
                    retryTime++;
                    log.error("Distribute Id Generator heartbeat : Failed, retry it.");
                }
                try {
                    TimeUnit.SECONDS.sleep(sleepTime);
                } catch (InterruptedException e) {
                    log.error("", e);
                }
            }
        }
    }
}
