package com.xbl.cache;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;
import android.widget.ListView;

import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import activity.xbl.com.paopaotakeout.R;

/**
 * Created by HG on 2016/8/29.
 * 将图片显示的线程--在这之前要先判断是否存在
 */
public class BitmapTask2 extends AsyncTask<String,Void,Bitmap> {
    private String imageUrl;
    private ImageView mIV;
    private AsyncImageLoader imageLoader;
    private PullToRefreshListView pullToRefreshListView;

    public BitmapTask2(PullToRefreshListView pullToRefreshListView, AsyncImageLoader imageLoader) {
        this.pullToRefreshListView = pullToRefreshListView;
        this.imageLoader=imageLoader;
    }

    @Override
    protected Bitmap doInBackground(String... params) {
        //可变参数取0，取得为第一个
         imageUrl=params[0];
        //图片的下载请求
        Bitmap bitmap=getNetworkBitmap(imageUrl);
        return bitmap;
    }
    //显示图片，将图片显示在IV中,所以需要一个构造方法将要显示的图片传入
    @Override
    protected void onPostExecute(Bitmap bitmap) {
        super.onPostExecute(bitmap);
        //根据绑定的网址标记寻找相应的ImageView
        mIV = (ImageView) pullToRefreshListView.findViewWithTag(imageUrl);
        if (mIV != null) {
            if (bitmap != null) {
                mIV.setImageBitmap(bitmap);
                //加入图片的方法，此方法中有判断图片是否已经在缓存中存在的方法
                imageLoader.addToMemoryCache(imageUrl,bitmap);
            } else {
                mIV.setImageResource(R.drawable.add);
            }

        }
    }

    //下载图片的方法
    public Bitmap getNetworkBitmap(String imageUrl){
        HttpURLConnection httpURLConnection=null;
        Bitmap bitmap=null;
        try {
            URL url= new URL(imageUrl);
            httpURLConnection= (HttpURLConnection) url.openConnection();
            //BitmapFactory可以用Stream来编码一张图片
            bitmap= BitmapFactory.decodeStream(httpURLConnection.getInputStream());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (httpURLConnection!=null){
                httpURLConnection.disconnect();
            }
        }
        return bitmap;
    }



}
