package com.bob.cache.server;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by bob on 2018/2/11.
 *
 * @author bob   <bobyang_coder@163.com>
 * @version v1.0
 * @since 2018/2/11
 */
public class ServerCache<K, V> {

    private Map<K, V> cacheMap1 = new ConcurrentHashMap<K, V>();
    private Map<K, V> cacheMap2 = new ConcurrentHashMap<K, V>();


    private int current = 1;

    private V getValue(K key) {
        if (1 == current) {
            return cacheMap1.get(key);
        } else {
            return cacheMap2.get(key);
        }
    }


    /**
     * 加载缓存
     */
    private void load() {

    }


    /**
     * 刷新缓存
     */
    private void refresh() {

    }


}
