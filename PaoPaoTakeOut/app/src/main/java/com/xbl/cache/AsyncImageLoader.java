package com.xbl.cache;

import android.graphics.Bitmap;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ListView;


import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;


/**
 * Created by HG on 2016/9/2.
 * 封装的用于管理异步加载图片的工具类
 * 放置一个线程池
 * 1.管理异步任务
    public Executor executor;
    * 2.异步加载图片
    */
    public class AsyncImageLoader {
        //管理异步任务，将异步任务都放到一个集合中
        public Set<BitmapTask> taskSet;
        //实例化一个线程池
    //内存缓存类
    LruMemoryCache lruMemoryCache;
    public  AsyncImageLoader(){
        //内存缓存的初始化
        //指定内存缓存区域的大小，首先知道当前应用程序运行时可用的内存大小
        int maxMemory = (int) Runtime.getRuntime().maxMemory();
        //为缓存区域指定内存大小一般为总的/8
        int cacheSize=maxMemory/8;
        //初始化缓存机制类
        lruMemoryCache =new LruMemoryCache(cacheSize);

    }
    //加载图片(对应的图片控件所在的父容器，对应的图片控件，对应图片的网络地址)
    public void loadBitMap(ListView listView, ImageView imageView,String imageUrl){
        //先到内存缓存中取图片
        Bitmap bitmap=readMemoryCache(imageUrl);
        //如果在内存缓存中可以读到直接放入图片（如果取不到readMemoryCache返回为空）
        if (bitmap!=null){
            imageView.setImageBitmap(bitmap);
            //退出程序
            return;
        }else{
            //如果不能读到，说明这个图片控件是第一次被下拉到，先给它做标记位，并且发送一个异步线程下载图片
            imageView.setTag(imageUrl);
            BitmapTask bitmapTask=new BitmapTask(listView,this);
            bitmapTask.execute(imageUrl);
        }
        
    }
    //存入缓存，put方法
    public void addToMemoryCache(String key, Bitmap value){
        lruMemoryCache.put(key,value);
        Log.d("TAG","图片存入缓存站");

    }
    //读取缓存
    public Bitmap readMemoryCache(String key){
        Bitmap  bitmap=lruMemoryCache.get(key);
        Log.d("TAG","在缓存站读取图片");
        return bitmap;

    }
    //加载图片(对应的图片控件所在的父容器，对应的图片控件，对应图片的网络地址)  自定义下拉刷新的ListView
    public void loadBitMap(PullToRefreshListView pullToRefreshListView, ImageView imageView, String imageUrl){
        //先到内存缓存中取图片
        Bitmap bitmap=readMemoryCache(imageUrl);
        //如果在内存缓存中可以读到直接放入图片（如果取不到readMemoryCache返回为空）
        if (bitmap!=null){
            imageView.setImageBitmap(bitmap);
            //退出程序
            return;
        }else{
            //如果不能读到，说明这个图片控件是第一次被下拉到，先给它做标记位，并且发送一个异步线程下载图片
            imageView.setTag(imageUrl);
            BitmapTask2 bitmapTask=new BitmapTask2(pullToRefreshListView,this);
            bitmapTask.execute(imageUrl);
        }

    }
}
