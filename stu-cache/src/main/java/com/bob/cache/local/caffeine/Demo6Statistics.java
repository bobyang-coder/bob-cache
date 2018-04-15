package com.bob.cache.local.caffeine;

import com.bob.cache.entity.Person;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.stats.CacheStats;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 统计
 *
 * @author bob   <bobyang_coder@163.com>
 * @version v1.0
 * @since 2018/2/24
 */
public class Demo6Statistics {
    public static final Logger logger = LoggerFactory.getLogger(Demo6Statistics.class);


    @org.junit.Test
    public void statistics() {
        Cache<String, Person> cache = Caffeine.newBuilder()
                .maximumSize(10_000)
                .recordStats()
                .build();
        cache.put("1", Person.Builder.newBuilder().setInfo(10, "bob").build());
        logger.info("Person1:{}", cache.getIfPresent("1"));
        logger.info("Person2:{}", cache.getIfPresent("2"));
        logger.info("Person3:{}", cache.getIfPresent("3"));
        CacheStats stats = cache.stats();
        logger.info("statistics result:{}", stats);

    }
}
