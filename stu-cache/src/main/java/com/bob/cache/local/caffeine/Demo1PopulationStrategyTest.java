package com.bob.cache.local.caffeine;

import com.bob.cache.entity.Person;
import com.github.benmanes.caffeine.cache.AsyncLoadingCache;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * 缓存填充策略
 * 1.手动
 * 2.同步
 * 3.异步
 *
 * @author bob   <bobyang_coder@163.com>
 * @version v1.0
 * @since 2018/2/12
 */
public class Demo1PopulationStrategyTest {

    /**
     * 手动添加缓存
     */
    @Test
    public void manualCache() {
        //创建缓存
        Cache<String, Person> cache = Caffeine.newBuilder()
                .expireAfterWrite(10, TimeUnit.MINUTES)
                .maximumSize(10_000)
                .build();
        String key = "bob";
        //1.根据key查询缓存，如果没有返回null
        Person bob = cache.getIfPresent(key);
        System.out.println(bob);
        //2.如果根据key查询缓存，则返回，如果没有则会调用createValue()查找或者创建一个，并放入缓存中
        Person person = cache.get(key, this::createPerson);
        System.out.println(person);
        bob = cache.getIfPresent(key);
        assert bob != null;
        System.out.println(bob);
        bob.setName("bob2");
        //3.添加缓存，如果key存在则覆盖
        cache.put(key, bob);
        cache.put("bob2", bob);
        System.out.println(bob);
        //4.删除缓存
        cache.invalidate(key);
        ConcurrentMap<String, Person> map = cache.asMap();
        System.out.println(map.toString());
    }

    /**
     * 同步加载缓存
     */
    @Test
    public void syncLoadingCache() {
        LoadingCache<String, Person> loadingCache = Caffeine.newBuilder()
                .expireAfterWrite(10, TimeUnit.MINUTES)
                .maximumSize(10_100)
                .build(this::createPerson);
        String key = "bob";
        //1.根据key查询缓存，如果没有返回null
        Person bob = loadingCache.get(key);
        System.out.println(bob);

        //2.获取组key的值返回一个Map
        List<String> keys = new ArrayList<>();
        keys.add(key);
        Map<String, Person> map = loadingCache.getAll(keys);
        System.out.println(map);
    }

    /**
     * 异步加载缓存
     *
     * @throws ExecutionException
     * @throws InterruptedException
     */
    @Test
    public void asyncLoadingCache() throws ExecutionException, InterruptedException {
        AsyncLoadingCache<String, Person> cache = Caffeine.newBuilder()
                .expireAfterWrite(10, TimeUnit.MINUTES)
                .maximumSize(10_100)
                .buildAsync(this::createPerson);
        String key = "bob";
        CompletableFuture<Person> future = cache.get(key);
        Person person = future.get();
        System.out.println(person);
        //2.获取组key的值返回一个Map
        List<String> keys = new ArrayList<>();
        keys.add(key);
        CompletableFuture<Map<String, Person>> map = cache.getAll(keys);
        System.out.println(map);

    }

    private Person createPerson(String k1) {
        return Person.Builder.newBuilder()
                .setInfo(10, "bob")
                .build();
    }
}
