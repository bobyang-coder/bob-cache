github链接：https://github.com/ben-manes/caffeine/wiki


[Executor api]:http://docs.oracle.com/javase/8/docs/api/java/util/concurrent/Executor.html
[ForkJoinPool api]:https://docs.oracle.com/javase/8/docs/api/java/util/concurrent/ForkJoinPool.html

# Caches(缓存)


## Eviction(驱逐策略/删除策略)

```
1.基于大小
|-1.基于缓存大小
|-2.基于权重大小
2.基于时间
|-1.基于固定的到期策略
|--1.1 访问后一段时间过期(使用缓存淘汰算法：LRU算法)
|--1.2 添加后一段时间过期
|-2.基于自定义策略
3.基于引用
|-1.基于key/value的弱引用
|-2.基于value的软引用
```

##  Removal(删除)
代码:见cache.server.caffeine.Demo3RemovalTest

术语：
1. eviction(驱逐)：驱逐意味着由于政策而被移除
2. invalidation(失效):失效意味着主叫方手动移除
3. removal(移除):移除是由于失效或驱逐而发生的

### 显示移除

监听缓存移除时间
|-1.手动删除
|-2.定义删除监听器

### 删除监听器
删除侦听器操作使用[Executor][Executor api]异步执行

## refresh(刷新)
### 1.手动刷新
```java
LoadingCache.refresh(K)
```
手动调用，异步刷新key对应的value值
### 2.自动刷新
```java
LoadingCache<Key, Graph> graphs = Caffeine.newBuilder()
    .maximumSize(10_000)
    .refreshAfterWrite(1, TimeUnit.MINUTES)
    .build(key -> createExpensiveGraph(key));
```

refreshAfterWrite方法指定自动刷新的策略，
当第一个过期请求发生时自动刷新将被执行，请求将触发异步刷新，同时返回旧值

与expireAfterWrite相比，refreshAfterWrite会在指定的持续时间后使关键字符合刷新条件，
但只有在查询条目时才会实际启动刷新。
因此，例如，您可以在同一个缓存中指定refreshAfterWrite和expireAfterWrite，
以便条目的到期计时器不会在条目符合刷新条件时盲目重置。
如果条目在达到刷新条件后不被查询，则允许其过期。
(bob ps:即刷新动作最好设定在过期动作之前，因为刷新后过期动作的时间也会改变)

刷新操作使用[Executor][Executor api]异步执行,默认的执行器是[ForkJoinPool.commonPool()][ForkJoinPool api],
可通过```Caffeine.executor(Executor)```更换执行器

## writer(刷新)

## refresh(刷新)

Caffeine缓存不会自动执行清理和清除值，或在值过期后立即执行。
相反，它在写入操作后执行少量维护，或者在读取操作后偶尔会执行少量维护（如果写入很少）。
此维护委托给后台Executor，默认情况下为ForkJoinPool.commonPool（），
可通过Caffeine.executor（Executor）覆盖。

# Extensions(扩展)