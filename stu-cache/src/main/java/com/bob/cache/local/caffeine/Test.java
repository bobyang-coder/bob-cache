package com.bob.cache.local.caffeine;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by bob on 2018/2/13.
 *
 * @author bob   <bobyang_coder@163.com>
 * @version v1.0
 * @since 2018/2/13
 */
public class Test {

    private static final Logger logger = LoggerFactory.getLogger(Test.class);

    public static void main(String[] args) {
        Lock lock = new ReentrantLock();
        lock.lock();
    }

    @org.junit.Test
    public void forkJoinPool() {
        ForkJoinPool executor = ForkJoinPool.commonPool();
        executor.execute(new Runnable() {
            @Override
            public void run() {
                System.out.println("bob is coming!");
            }
        });
    }


    @org.junit.Test
    public void scheduledExecutorService() {
        ScheduledExecutorService executorService = new ScheduledThreadPoolExecutor(1,
                new ThreadFactory() {
                    private final AtomicInteger threadNumber = new AtomicInteger(1);

                    @Override
                    public Thread newThread(Runnable r) {
                        return new Thread(r, "pool-thread" + threadNumber.getAndIncrement());
                    }
                });

        ScheduledFuture<?> scheduledFuture =
                executorService.scheduleAtFixedRate(() -> logger.info("beep beep"), 10, 10, TimeUnit.SECONDS);

        executorService.schedule((Runnable) () -> {
            scheduledFuture.cancel(true);
            logger.info("取消");
        }, 60 * 60, TimeUnit.SECONDS);


    }


    /**
     *
     */
    @org.junit.Test
    public void testArrayList() {
        String s = "11";

        //=======List
        ArrayList<String> list = new ArrayList<>();
        list.add(s);
        list.add(s);
        list.add(1, "22");
        list.get(1);
        list.trimToSize();
        System.out.println(list.size());

        Vector<String> vector = new Vector<>();
        vector.add(s);
        vector.add(s);
        System.out.println(vector.size());


        LinkedList<String> linkedList = new LinkedList<>();
        linkedList.add(s);
        linkedList.add(s);
        linkedList.remove(s);
        linkedList.add(1, "222");
        System.out.println(linkedList.size());

        //========Set
        HashSet<String> hashSet = new HashSet<>();
        System.out.println(hashSet.add(s));
        System.out.println(hashSet.add(s));

        TreeSet<String> treeSet = new TreeSet<>();
        treeSet.add(s);
        treeSet.add(s);


        //========Map

        HashMap<String, String> map = new HashMap<>();
        map.put(null, null);
        map.put("1", s);
        map.put("2", s);
        String s1 = map.get("1");
        ConcurrentHashMap<String, String> concurrentHashMap = new ConcurrentHashMap<>();
        concurrentHashMap.put("1", s);

        TreeMap<Object, Object> treeMap = new TreeMap<>();
        treeMap.put("11","11");
        treeMap.put("12","11");
        Set<Map.Entry<Object, Object>> entrySet = treeMap.entrySet();
        for (Map.Entry<Object, Object> objectObjectEntry : entrySet) {
            objectObjectEntry.getKey();
            objectObjectEntry.getValue();
        }

        Hashtable<Object, Object> hashtable = new Hashtable<>();
        hashtable.put("1", "222");
        hashtable.put("2", "222");
        System.out.println("end");
    }


}




