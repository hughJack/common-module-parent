package cn.com.flaginfo.module.common.queue;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @Author: LiuMeng
 * @Describe:
 * @Time: 2018/10/26 14:24
 */
public class LimitQueue<E>{

    private final int limit;

    private LinkedList<E> queue = new LinkedList<>();

    private ReentrantLock lock = new ReentrantLock();

    private LimitQueue(int limit){
        this.limit = limit;
    }

    /**
     * 入队
     * @param e
     * @return
     */
    public boolean offer(E e) {
        final ReentrantLock lock = this.lock;
        lock.lock();
        try {
            if(queue.size() >= limit){
                queue.poll();
            }
            return queue.offer(e);
        } finally {
            lock.unlock();
        }
    }

    /**
     * 出队
     * @return
     */
    public E poll() {
        return queue.poll();
    }

    public int size(){
        return queue.size();
    }

    public E getFirst(){
        return queue.getFirst();
    }

    public E getLast(){
        return queue.getLast();
    }

    public Queue<E> getQueue(){
        return queue;
    }

    /**
     * 获取队列长度
     * @return
     */
    public int getLimit(){
        return limit;
    }
}
