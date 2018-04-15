package com.bob.cache.local.caffeine;

import com.bob.cache.entity.Person;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

/**
 * 刷新缓存
 * |-1.手动刷新
 * |-2.自动刷新
 *
 * @author bob   <bobyang_coder@163.com>
 * @version v1.0
 * @since 2018/2/13
 */
public class Demo4RefreshTest {
    private static final Logger logger = LoggerFactory.getLogger(Demo4RefreshTest.class);

    /**
     * |-1.手动刷新
     */
    @Test
    public void handleRefresh() {
        LoadingCache<String, Person> cache = Caffeine.newBuilder()
                .maximumSize(10_000)
                .refreshAfterWrite(1, TimeUnit.SECONDS)
                .build(this::createPerson);
        String key = "1";
        logger.info("init value:{}", cache.get(key));
        cache.refresh(key);
        logger.info("after refresh value:{}", cache.get(key));
    }

    /**
     * |-2.自动刷新
     *
     * @throws InterruptedException
     */
    @Test
    public void autoRefresh() throws InterruptedException {
        LoadingCache<String, Person> cache = Caffeine.newBuilder()
                .maximumSize(10_000)
                .refreshAfterWrite(1, TimeUnit.SECONDS)
                .build(this::createPerson);
        Person person = cache.get("1");
        person.setName("bob-bob");
        cache.put("1", person);
        logger.info("init value:{}", person);
        Thread.sleep(2000L);
        //当第一个过期请求发生时自动刷新将被执行，请求将触发异步刷新，同时返回旧值
        Person person2 = cache.get("1");
        //此时应该返回的是新值
        Person person3 = cache.get("1");
        logger.info("old value:{}", person2);
        logger.info("new value:{}", person3);
    }

    private Person createPerson(String k1) {
        return Person.Builder.newBuilder()
                .setInfo(10, String.format("bob-c-%s", k1))
                .build();
    }
}
