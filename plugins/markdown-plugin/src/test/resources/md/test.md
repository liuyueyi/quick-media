## ThreadLocal使用及原理介绍
> 线程本地变量，每个线程保存变量的副本，对副本的改动，对其他的线程而言是透明的（即隔离的）

### 1. 使用姿势一览

使用方式也比较简单，常用的三个方法

```java
// 设置当前线程的线程局部变量的值
void set(Object value); 

// 该方法返回当前线程所对应的线程局部变量
public Object get();

// 将当前线程局部变量的值删除
public void remove();
```

下面给个实例，来瞅一下，这个东西一般的使用姿势。通常要获取线程变量， 直接调用 `ParamsHolder.get()` 

```java
public class ParamsHolder {
    private static final ThreadLocal<Params> PARAMS_INFO = new ThreadLocal<>();

    @ToString
    @Getter
    @Setter
    public static class Params {
        private String mk;
    }

    public static void setParams(Params params) {
        PARAMS_INFO.set(params);
    }

    public static void clear() {
        PARAMS_INFO.remove();
    }
    
    public static Params get() {
        return PARAMS_INFO.get();
    }
    
    
    public static void main(String[] args) {
        Thread child = new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("child thread initial: " + ParamsHolder.get());
                ParamsHolder.setParams(new ParamsHolder.Params("thread"));
                System.out.println("child thread final: " + ParamsHolder.get());
            }
        });


        child.start();

        System.out.println("main thread initial: " + ParamsHolder.get());
        ParamsHolder.setParams(new ParamsHolder.Params("main"));
        System.out.println("main thread final: " + ParamsHolder.get());
    }
}
```

输出结果

```
child thread initial: null
main thread initial: null
child thread final: ParamsHolder.Params(mk=thread)
main thread final: ParamsHolder.Params(mk=main)
```

### 2. 实现原理探究

直接看源码中的两个方法， get/set， 看下到底是如何实现线程变量的

```java
public void set(T value) {
   Thread t = Thread.currentThread();
   ThreadLocalMap map = getMap(t);
   if (map != null)
       map.set(this, value);
   else
       createMap(t, value);
}

public T get() {
   Thread t = Thread.currentThread();
   ThreadLocalMap map = getMap(t);
   if (map != null) {
       ThreadLocalMap.Entry e = map.getEntry(this);
       if (e != null) {
           @SuppressWarnings("unchecked")
           T result = (T)e.value;
           return result;
       }
   }
   return setInitialValue();
}

ThreadLocalMap getMap(Thread t) {
    return t.threadLocals;
}
```

#### 主要以set方法进行讲解

逻辑比较清晰

- 获取当前线程对象
- 获取到线程对象中的`threadLocals` 属性
- 将value塞入`ThreadLocalMap`

**threadLocals属性**

这个属性的解释如下，简单来讲，这个里面的变量都是线程独享的，完全由线程自己hold住

> ThreadLocal values pertaining to this thread. This map is maintained by the ThreadLocal class.



接下来需要了解的就是`ThreadLocalMap`这个对象的内部构造了，里面的有个table对象，维护了一个Entry的数组`table`，`Entry`的key为`ThreadLocal`对象，value为具体的值。


```java
//ThreadLocalMap.java
static class Entry extends WeakReference<ThreadLocal<?>> {
       /** The value associated with this ThreadLocal. */
       Object value;

       Entry(ThreadLocal<?> k, Object v) {
           super(k);
           value = v;
       }
   }
   
   /**
    * The table, resized as necessary.
    * table.length MUST always be a power of two.
    */
private Entry[] table;
   
private void set(ThreadLocal<?> key, Object value) {
    Entry[] tab = table;
    int len = tab.length;
    int i = key.threadLocalHashCode & (len-1);

    for (Entry e = tab[i];
         e != null;
         e = tab[i = nextIndex(i, len)]) {
        ThreadLocal<?> k = e.get();

        if (k == key) {
            e.value = value;
            return;
        }

        if (k == null) {
            replaceStaleEntry(key, value, i);
            return;
        }
    }

    tab[i] = new Entry(key, value);
    int sz = ++size;
    if (!cleanSomeSlots(i, sz) && sz >= threshold)
        rehash();
}
```

聚焦在  `int i = key.threadLocalHashCode & (table.length - 1);`  这一行，这个就是获取Entry对象在`table`中索引值的主要逻辑，主要利用当前线程的hashCode值

假设出现两个不同的线程，这个code值一样，会如何？

这种类似hash碰撞的场景，会调用 `nextIndex` 来获取下一个位置

---

针对上面的逻辑，有点有必要继续研究下， `hashCode` 的计算方式， 为什么要和数组的长度进行与计算


> 作为ThreadLocal实例的变量只有 threadLocalHashCode 这一个，`nextHashCode` 和`HASH_INCREMENT` 是ThreadLocal类的静态变量，实际上`HASH_INCREMENT`是一个常量，表示了连续分配的两个ThreadLocal实例的threadLocalHashCode值的增量，而nextHashCode 的表示了即将分配的下一个ThreadLocal实例的threadLocalHashCode 的值

> 所有ThreadLocal对象共享一个AtomicInteger对象nextHashCode用于计算hashcode，一个新对象产生时它的hashcode就确定了，算法是从0开始，以HASH_INCREMENT = 0x61c88647为间隔递增，这是ThreadLocal唯一需要同步的地方。根据hashcode定位桶的算法是将其与数组长度-1进行与操作

> ThreadLocalMap的初始长度为16，每次扩容都增长为原来的2倍，即它的长度始终是2的n次方，上述算法中使用0x61c88647可以让hash的结果在2的n次方内尽可能均匀分布，减少冲突的概率


### 3. 线程池中使用`ThreadLocal`的注意事项

这里主要的一个问题是线程复用时， 如果不清除掉ThreadLocal 中的值，就会有可怕的事情发生， 先简单的演示一下

```java
private static final ThreadLocal<AtomicInteger> threadLocal =new ThreadLocal<AtomicInteger>() {

        @Override
        protected AtomicInteger initialValue() {
            return new AtomicInteger(0);
        }
    };


    static class Task implements Runnable {

        @Override
        public void run() {
            AtomicInteger s = threadLocal.get();
            int initial = s.getAndIncrement();
            // 期望初始为0
            System.out.println(initial);
        }
    }


    public static void main(String[] args) {

        ExecutorService executor = Executors.newFixedThreadPool(2);
        executor.execute(new Task());
        executor.execute(new Task());
        executor.execute(new Task());
        executor.shutdown();
    }
```

输出结果

```
0
0
1
```

说好的线程变量，这里居然没有按照我们预期的来玩，主要原因就是线程复用了，而线程中的局部变量没有清零，导致下一个使用这个线程的时候，这些局部变量也带过来，导致没有按照我们的预期使用

这个最可能导致的一个超级严重的问题，就是web应用中的用户串掉的问题，如果我们将每个用户的信息保存在 `ThreadLocal` 中， 如果出现线程复用了，那么问题就会导致明明是张三用户，结果登录显示的是李四的帐号，这下就真的呵呵了

**因此，强烈推荐，对于线程变量，一但不用了，就显示的调用 `remove()`方法进行清楚**

### 4. 经典case

`SimpleDataFormate` 是一个非线程安全的类，可以使用 ThreadLocal 完成的线程安全的使用

```java
public class ThreadLocalDateFormat {
    static ThreadLocal<DateFormat> sdf = new ThreadLocal<DateFormat>() {

        @Override
        protected DateFormat initialValue() {
            return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        }
    };

    public static String date2String(Date date) {
        return sdf.get().format(date);
    }

    public static Date string2Date(String str) throws ParseException {
        return sdf.get().parse(str);
    }
}
```

**想一想，为什么这种方式是线程安全的呢？**

## II. 小结

### 1. 一句话介绍

ThreadLocal  线程本地变量，每个线程保存变量的副本，对副本的改动，对其他的线程而言是透明的（即隔离的）

### 2. 常用方法

三个常用的方法

```java
// 设置当前线程的线程局部变量的值
void set(Object value); 

// 该方法返回当前线程所对应的线程局部变量
public Object get();

// 将当前线程局部变量的值删除
public void remove();
```

### 3. 实现原理

利用了HashMap的设计理念，一个map中存储Thread->线程变量的映射关系, 因此线程变量在多线程之间是隔离的


### 4. 注意事项

通常建议是线程执行完毕之后，主动去失效掉ThreadLocal中的变量，以防止线程复用导致变量被乱用了


## III. 其他

### 声明

尽信书则不如，已上内容，纯属一家之言，因本人能力一般，见识有限，如有问题，请不吝指正，感激