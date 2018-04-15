package com.bob.cache.local.caffeine;

import com.bob.cache.entity.Person;
import com.github.benmanes.caffeine.cache.CacheWriter;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
import com.github.benmanes.caffeine.cache.RemovalCause;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Created by bob on 2018/2/23.
 *
 * @author bob   <bobyang_coder@163.com>
 * @version v1.0
 * @since 2018/2/23
 */
public class Demo5WriterTest {


    public void writer() {
        LoadingCache<String, Person> loadingCache = Caffeine.newBuilder()
                .writer(new CacheWriter<String, Person>() {
                    @Override
                    public void write(@Nonnull String key, @Nonnull Person value) {

                    }

                    @Override
                    public void delete(@Nonnull String key, @Nullable Person value, @Nonnull RemovalCause cause) {

                    }
                }).build(this::createPerson);
    }

    private Person createPerson(String k1) {
        return Person.Builder.newBuilder()
                .setInfo(10, String.format("bob-c-%s", k1))
                .build();
    }
}
