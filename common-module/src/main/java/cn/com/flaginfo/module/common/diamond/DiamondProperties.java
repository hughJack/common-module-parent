package cn.com.flaginfo.module.common.diamond;

import com.taobao.diamond.manager.DiamondManager;
import com.taobao.diamond.manager.ManagerListener;
import com.taobao.diamond.manager.impl.DefaultDiamondManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertiesPropertySource;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.Executor;

/**
 * 从Diamond配置中心初始化数据
 * @author Meng.Liu
 * @create 2017-09-26 10:55
 **/
@Slf4j
public class DiamondProperties extends DynamicProperties {

    private static ConfigurableEnvironment env;

    public static final String CONFIG_KEY = "diamondProperties";

    private static PropertiesPropertySource diamondProperties = new PropertiesPropertySource(CONFIG_KEY, new Properties());


    private DiamondProperties(){
        super();
    }

    public static DynamicProperties initInstance(ConfigurableEnvironment cenv){
        if( log.isDebugEnabled() ){
            log.debug("init Diamond Properties...");
        }
        Assert.notNull(cenv);
        DiamondProperties diamondProperties = new DiamondProperties();
        diamondProperties.setEnv(cenv);
        diamondProperties.init();
        return diamondProperties;
    }


    private void setEnv(ConfigurableEnvironment env) {
        DiamondProperties.env = env;
    }

    /**
     *
     * @return
     */
    public ConfigurableEnvironment getEnv() {
        return env;
    }

    @Override
    public Map<String,Object> getAllConfig() {
        MutablePropertySources propertySources = env.getPropertySources();
        MapPropertySource ps = (MapPropertySource)propertySources.get(CONFIG_KEY);
        if(ps == null){
            return null;
        }
        return ps.getSource();
    }

    private void init(){

        final String propGroup = env.getProperty("prop_group");
        final String propDataId = env.getProperty("prop_data_id");
//        读取全局配置
//        initDiamond(propGroup,"global-prop");
        //读取服务配置信息
        initDiamond(propGroup,propDataId);
    }

    public void initDiamond(String propGroup,String propDataId){
        if( log.isDebugEnabled() ){
            log.debug("load Diamond Properties. group : [{}], propDataId : [{}]", propGroup, propDataId );
        }
        DiamondManager manager = new DefaultDiamondManager(propGroup,propDataId,new ManagerListener(){
            @Override
            public Executor getExecutor() {
                return null;
            }
            @Override
            public void receiveConfigInfo(String configInfo) {
                if( log.isDebugEnabled() ){
                    log.info("Receive Diamond configInfo : [{}]", configInfo);
                }
                updateData(configInfo);
            }
        });
        updateData(manager.getAvailableConfigureInfomation(5000));
    }


    /**
     *
     * @param key
     * @return
     */
    @Override
    public String getProperty(String key){
        try {
            return env.getProperty(key);
        } catch (Exception e) {
           log.error("", e);
        }
        return null;
    }


    private void updateData(String data){
        preData.clear();
        Map<String, Object> current = this.getAllConfig();
        if(!CollectionUtils.isEmpty(current)){
            preData.putAll(current);
        }
        Properties properties = getPropertiesForString(data);
        diamondProperties.getSource().putAll(getAllConfig(properties));
        MutablePropertySources propertySources = env.getPropertySources();
        propertySources.addLast(diamondProperties);
        messageChange();

    }


    private Properties getPropertiesForString(String configInfo) {
        Properties p = new Properties();
        if(configInfo == null){ return p;}
        try {
            p.load(new StringReader(configInfo));
        } catch (IOException e) {
            log.error("Exception : {}", e);
        }
        return p;
    }

    private Map<String,Object> getAllConfig(Properties prop) {
        Set<Entry<Object, Object>> set = prop.entrySet();
        Map<String,Object> allMap = new HashMap<String,Object>();
        for (Entry<Object, Object> e : set) {
            allMap.put((String)e.getKey(), e.getValue());
        }
        return allMap;
    }


    /**
     *
     * @param s
     * @param defVal
     * @return
     */
    public static Integer getPropertyInteger(String s, Integer defVal){
        Integer val = getPropertyInteger(s);
        return null == val ? defVal : val;
    }

    public static Integer getPropertyInteger(String s){
        try {
            return Integer.valueOf(getPropertyString(s));
        }catch (Exception e){
            return null;
        }
    }

    public static Long getPropertyLong(String s, Long defVal){
        Long val = getPropertyLong(s);
        return null == val ? defVal : val;
    }

    public static Long getPropertyLong(String s){
        try {
            return Long.valueOf(getPropertyString(s));
        }catch (Exception e){
            return null;
        }
    }

    public static Double getPropertyDouble(String s, Double defVal){
        Double val = getPropertyDouble(s);
        return null == val ? defVal : val;
    }

    public static Double getPropertyDouble(String s){
        try {
            return Double.valueOf(getPropertyString(s));
        }catch (Exception e){
            return null;
        }
    }

    public static Boolean getPropertyBoolean(String s, Boolean defVal){
        Boolean val = getPropertyBoolean(s);
        return null == val ? defVal : val;
    }

    public static Boolean getPropertyBoolean(String s){
        try {
            return Boolean.valueOf(getPropertyString(s));
        }catch (Exception e){
            return null;
        }
    }

    public static String getPropertyString(String s, String defVal){
        return env.getProperty(s, defVal);
    }

    public static String getPropertyString(String s){
        return env.getProperty(s);
    }

}
