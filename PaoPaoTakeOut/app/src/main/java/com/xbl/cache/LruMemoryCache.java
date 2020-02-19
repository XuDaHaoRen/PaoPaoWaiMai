package com.xbl.cache;

import android.graphics.Bitmap;

import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by HG on 2016/9/1.
 * Lru算法 实现一个缓存机制  将图片进行缓存，如果缓存的空间满了就会把不经常用的图片进行回收
 * 别人写的
 */
public class LruMemoryCache {
    // 实例化一个LinkedHashMap  放置图片  一种数据结构
    private final LinkedHashMap<String, Bitmap> cache;
    // 最大缓存空间
    private final int maxSize;
    // 当前缓存空间
    private int currentSize;
    public LruMemoryCache(int maxSize)
    {
        //得到最大的缓存空间如果最大缓存空间小于0，抛出栈溢出异常
        if (maxSize <= 0)
        {
            throw new IllegalArgumentException("maxSize <= 0");
        }
        this.maxSize = maxSize;
        // 初始化队列按照访问顺序从少到多排列（参数：初始大小为0，默认使用率0.75，
        // 按照哪种顺序排列：按照访问次数顺序从小到多）
        this.cache = new LinkedHashMap<String, Bitmap>(0, 0.75f, true);
    }
    //实现put方法，相当于HashMap的put方法，根据key，value进行数据存储，让自己写的缓存机制也有put方法
    public final boolean put(String key, Bitmap value)
    {
        //如果输入的key或者value是空值，报出空指针异常
        if (key == null || value == null)
        {
            throw new NullPointerException("key == null || value == null");
        }
        //一个同步线程管理的块，因为有很多的线程 需要处理

        synchronized (this)
        {
            //存入图片之后当前内存改变了sizeOf得到当前图片的大小
            currentSize += sizeOf(key, value);
            //得到key所返回的value,如果以前存储过（key网址下面下面有两张图片，本来是一张忽然又来了一张图片）
            Bitmap previous = cache.put(key, value);
            //如果以前有过一张图片得到的previous就不等于空，要先将以前那张图片的占有的内存删除，再
            // 将新图片添加进去，相当于一个替换
            if (previous != null)
            {
                currentSize -= sizeOf(key, previous);
            }
        }

        trimToSize(maxSize);
        return true;
    }
    //缓存机制的get方法
    public final Bitmap get(String key)
    {
        if (key == null)
        {
            throw new NullPointerException("key == null");
        }

        synchronized (this)
        {
            return cache.get(key);
        }
    }
    //从缓存机制移除图片
    public final void remove(String key)
    {
        if (key == null)
        {
            throw new NullPointerException("key == null");
        }

        synchronized (this)
        {
            Bitmap previous = cache.remove(key);
            if (previous != null)
            {
                currentSize -= sizeOf(key, previous);
            }
        }
    }
    //请看弄缓存机制
    public void clear()
    {
        trimToSize(-1);
    }
    //获取缓存机制链表的所有key值
    public Collection<String> keys()
    {
        return new HashSet<String>(cache.keySet());
    }
    //在接近内存峰值的时候将不常用的图片进行移除，在存入缓存的时候判断加载当前图片时是否要将头部的图片移除
    private void trimToSize(int maxSize)
    {
        while (true)
        {
            String key;
            Bitmap value;
            synchronized (this)
            {
                if (currentSize < 0 || (cache.isEmpty() && currentSize != 0))
                {
                    throw new IllegalStateException(getClass().getName() + ".sizeOf() is reporting inconsistent results!");
                }

                if (currentSize <= maxSize || cache.isEmpty())
                {
                    break;
                }
                //用迭代器去遍历这个Map,并去到头上不经常用的图片
                Map.Entry<String, Bitmap> toEvict = cache.entrySet().iterator().next();
                if (toEvict == null)
                {
                    break;
                }
                key = toEvict.getKey();
                value = toEvict.getValue();
                cache.remove(key);
                //改变当前内存缓存的大小
                currentSize -= sizeOf(key, value);
            }
        }
    }
    //返回指定图片大小
    private int sizeOf(String key, Bitmap value)
    {
        return value.getRowBytes() * value.getHeight();
    }

    @Override
    public synchronized final String toString()
    {
        return String.format("LruCache[maxSize=%d]", maxSize);
    }


}
