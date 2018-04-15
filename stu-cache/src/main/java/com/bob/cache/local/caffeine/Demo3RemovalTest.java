package com.bob.cache.local.caffeine;

import com.bob.cache.entity.Person;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
import com.github.benmanes.caffeine.cache.RemovalCause;
import com.github.benmanes.caffeine.cache.RemovalListener;
import org.junit.Test;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;

/**
 * 术语：
 * 1.eviction(驱逐)：驱逐意味着由于政策而被移除
 * 2.invalidation(失效):失效意味着主叫方手动移除
 * 3.removal(移除):移除是由于无效或驱逐而发生的
 * <p>
 * 监听缓存移除时间
 * |-1.手动删除
 * |-2.定义删除监听器
 * <p>
 * 删除监听器
 *
 * @author bob   <bobyang_coder@163.com>
 * @version v1.0
 * @since 2018/2/13
 */
public class Demo3RemovalTest {

    @Test
    public void removal() {
        LoadingCache<String, Person> cache = Caffeine.newBuilder()
                .removalListener(new MyRemovalListener())
                .weakValues()
                .build(this::createPerson);
        System.out.println(cache.get("1"));
        System.gc();
        //1.失效指定key
        cache.invalidate("1");

        ArrayList<String> keys = new ArrayList<>();
        keys.add("1");
        //
        cache.invalidateAll(keys);
        //失效所有key
        cache.invalidateAll();
        System.out.println("=====end=====");
    }

    public class MyRemovalListener implements RemovalListener<String, Person> {
        @Override
        public void onRemoval(@Nullable String key, @Nullable Person value, @Nonnull RemovalCause cause) {
            System.out.println(String.format("key=%s,value:%s,cause:%s", key, value, cause));
        }
    }

    private Person createPerson(String k1) {
        return Person.Builder.newBuilder()
                .setInfo(10, String.format("bob-c-%s", k1))
                .build();
    }
}
