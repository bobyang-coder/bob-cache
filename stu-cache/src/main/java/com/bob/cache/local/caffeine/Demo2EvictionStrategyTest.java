package com.bob.cache.local.caffeine;

import com.bob.cache.entity.Person;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.Expiry;
import com.github.benmanes.caffeine.cache.LoadingCache;
import com.github.benmanes.caffeine.cache.Weigher;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

/**
 * 驱逐策略(删除策略)
 * 1.基于大小
 * |-1.基于缓存大小
 * |-2.基于权重大小
 * 2.基于时间
 * |-1.基于固定的到期策略
 * |--1.1 访问后一段时间过期(使用缓存淘汰算法：LRU算法)
 * |--1.2 添加后一段时间过期
 * |-2.基于自定义策略
 * 3.基于引用
 * |-1.基于key/value的弱引用
 * |-2.基于value的软引用
 *
 * @author bob   <bobyang_coder@163.com>
 * @version v1.0
 * @since 2018/2/12
 */
public class Demo2EvictionStrategyTest {


    /**
     * 基于大小又分为两种
     * |-1.基于缓存大小
     * |-2.基于权重大小
     */
    @Test
    public void sizeBased() {
        //1.基于缓存大小(缓存数量小于1)
        LoadingCache<String, Person> cache1 = Caffeine.newBuilder()
                .maximumSize(2)
                .build(key -> createPerson(key));
        cache1.put("3", new Person(13, "bob3"));
        cache1.put("1", new Person(11, "bob1"));
        cache1.put("2", new Person(12, "bob2"));
        System.out.println(cache1.get("1"));
        System.out.println(cache1.get("2"));
        System.out.println(cache1.get("3"));


        //2.基于权重大小(年龄小于100)
        LoadingCache<String, Person> cache2 = Caffeine.newBuilder()
                .maximumWeight(100)
                .weigher((Weigher<String, Person>) (s, person) -> person.getAge())
                .build(this::createPerson);
        cache2.put("1", new Person(91, "bob1"));
        cache2.put("2", new Person(102, "bob2"));
        cache2.put("3", new Person(103, "bob3"));
        System.out.println(cache2.get("1"));
        System.out.println(cache2.get("2"));
        System.out.println(cache2.get("3"));
    }


    /**
     * 基于时间又分为两种
     * |-1.基于固定的到期策略
     * |--1.1 访问后一段时间过期(使用缓存淘汰算法：LRU算法)
     * |--1.2 添加后一段时间过期
     * |-2.基于自定义策略
     */
    @Test
    public void timeBased() throws InterruptedException {
        //|-1.基于固定的到期策略
        //|--1.1 访问后一段时间过期
        LoadingCache<String, Person> cache11 = Caffeine.newBuilder()
                .expireAfterAccess(5, TimeUnit.SECONDS)
                .build(key -> createPerson(key));
        cache11.put("1", new Person(91, "bob1"));
        //Person(age=91, name=bob1)
        System.out.println(cache11.get("1"));
        Thread.sleep(6000L);
        //Person(age=10, name=bob-c-1)
        System.out.println(cache11.get("1"));

        //|--1.2 添加后一段时间过期
        LoadingCache<String, Person> cache12 = Caffeine.newBuilder()
                .expireAfterWrite(5, TimeUnit.SECONDS)
                .build(key -> createPerson(key));
        cache11.put("1", new Person(91, "bob1"));
        Thread.sleep(6000L);
        //Person(age=10, name=bob-c-1)
        System.out.println(cache11.get("1"));

        //|-2.基于自定义策略
        LoadingCache<String, Person> cache2 = Caffeine.newBuilder()
                .expireAfter(new Expiry<String, Person>() {
                    @Override
                    public long expireAfterCreate(String s, Person person, long currentTime) {
                        return TimeUnit.SECONDS.toNanos(currentTime + 1000L);
                    }

                    @Override
                    public long expireAfterUpdate(String s, Person person, long currentTime, long currentDuration) {
                        System.out.println(s + ":::" + person);
                        return currentDuration;
                    }

                    @Override
                    public long expireAfterRead(String s, Person person, long currentTime, long currentDuration) {
                        System.out.println(s + ":::" + person);
                        return currentDuration;
                    }
                })
                .build(this::createPerson);
        cache2.put("1", new Person(91, "bob1"));
        cache2.put("1", new Person(92, "bob1"));
        //Person(age=10, name=bob-c-1)
        System.out.println(cache2.get("1"));
        System.out.println(cache2.get("1"));


    }


    /**
     * 基于引用
     * |-1.基于key/value的弱引用
     * |-2.基于value的软引用
     */
    @Test
    public void referenceBased() {
        //|-1.基于key/value的弱引用
        // 当key和value都没有引用时驱逐缓存(在垃圾回收时)
        LoadingCache<String, Person> cache1 = Caffeine.newBuilder()
                .weakKeys()
                .weakValues()
                .build(this::createPerson);
        System.out.println(cache1.get("1"));


        //|-2.基于key/value的软引用
        //当垃圾收集器需要释放内存时驱逐(在内存不足时)
        LoadingCache<String, Person> cache2 = Caffeine.newBuilder()
                .softValues()
                .build(this::createPerson);
        System.out.println(cache2.get("1"));
        System.gc();
        //{}
        System.out.println(cache1.asMap());
        //{1=Person(age=10, name=bob-c-1)}
        System.out.println(cache2.asMap());

    }


    private Person createPerson(String k1) {
        return Person.Builder.newBuilder()
                .setInfo(10, String.format("bob-c-%s", k1))
                .build();
    }
}
