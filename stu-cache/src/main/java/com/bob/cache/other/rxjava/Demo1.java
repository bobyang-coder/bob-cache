package com.bob.cache.other.rxjava;

import io.reactivex.Flowable;

/**
 * Created by bob on 2018/2/24.
 *
 * @author bob   <bobyang_coder@163.com>
 * @version v1.0
 * @since 2018/2/24
 */
public class Demo1 {
    public static void main(String[] args) {
        Flowable.just("Hello world").subscribe(System.out::println);
    }
}
