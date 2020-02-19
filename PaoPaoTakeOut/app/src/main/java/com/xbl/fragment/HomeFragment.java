package com.xbl.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.xbl.flipper.FlipperGesture;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import activity.xbl.com.paopaotakeout.DayGoodsActivity;
import activity.xbl.com.paopaotakeout.FlavourActivity;
import activity.xbl.com.paopaotakeout.FoodActivity;
import activity.xbl.com.paopaotakeout.MainActicity;
import activity.xbl.com.paopaotakeout.R;
import activity.xbl.com.paopaotakeout.SearchActivity;
import activity.xbl.com.paopaotakeout.StoreActivity;

/**
 * Created by HG on 2016/11/8.
 */
public class HomeFragment extends Fragment {
    @ViewInject(R.id.home_articles_iv)
    private ImageView mArtuclesIv;
    @ViewInject(R.id.home_agency_iv)
    private ImageView mAgencyIv;
    @ViewInject(R.id.home_food_iv)
    private ImageView mFoodIv;
    @ViewInject(R.id.home_view_flipper)
    private ViewFlipper mViewFlipper;
    @ViewInject(R.id.home_search_tv)
    private TextView mSerachTv;
    @ViewInject(R.id.home_search_et)
    private EditText mSearchEt;
    private FlipperGesture flipperGesture;
    private List<Bitmap> list = new ArrayList<>();
    private List<String> stringList = new ArrayList<>();
    private Bitmap bitmap;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, null);
        x.view().inject(this, view);
     //  initPicData();

       new HomeAsync().execute();


        return view;

    }

    private void initPicData() {
        RequestParams requestParams = new RequestParams("http://103.244.59.105:8014/paopaoserver/advert");
        requestParams.addQueryStringParameter("params", "{\"advert_type\":1}");
        Log.e("tag",requestParams.toString())  ;
        x.http().get(requestParams, new Callback.CacheCallback<String>()  {
            @Override
            public boolean onCache(String result) {
                return false;
            }

            @Override
            public void onSuccess(String result) {
                try {
                    Log.e("tag", result);
                    JSONObject object_array = new JSONObject(result);
                    JSONArray array = object_array.getJSONArray("datas");
                    for (int i = 0; i < 4; i++) {
                        JSONObject object = array.getJSONObject(i);
                        String url = object.getString("advert_image_url");
                        String tmp = "http://103.244.59.105:8014/paopaoserver";
                        url = tmp + url;
                        Log.e("tag", url);
                        stringList.add(url);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });


    }

    @Event(value = {R.id.home_articles_iv, R.id.home_food_iv, R.id.home_agency_iv,R.id.home_search_tv})
    private void doClick(View view) {
        switch (view.getId()) {
            case R.id.home_articles_iv:

                Intent intent = new Intent(getActivity(), DayGoodsActivity.class);
                startActivity(intent);
                break;
            case R.id.home_food_iv:
                Intent intent2 = new Intent(getActivity(), StoreActivity.class);
                startActivity(intent2);
                break;
            case R.id.home_agency_iv:
                Intent intent3 = new Intent(getActivity(), StoreActivity.class);
                startActivity(intent3);
                break;
            case R.id.home_search_tv:
                Intent intent1=new Intent(getActivity(), SearchActivity.class);
                intent1.putExtra("product_name",mSearchEt.getText().toString());
                startActivity(intent1);
                break;
        }


    }


    private void imageload(String u) {
        bitmap=null;
        URL url = null;
        try {
            url = new URL(u);
            //打开网址
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("GET");
            bitmap = BitmapFactory.decodeStream(httpURLConnection.getInputStream());
            Log.e("tag","图片下载成功");
            list.add(bitmap);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }


    private void onTuch() {
        flipperGesture = new FlipperGesture(mViewFlipper, list, getActivity());
        MainActicity.MyTouchListener myTouchListener = new MainActicity.MyTouchListener() {
            @Override
            public void onTouchEvent(MotionEvent event) {
//                mViewFlipper.stopFlipping();             // 点击事件后，停止自动播放
                mViewFlipper.setAutoStart(false);
                flipperGesture.gestureDetector.onTouchEvent(event);//注册手势事件

            }
        };
        // 将myTouchListener注册到分发列表
        ((MainActicity) this.getActivity()).registerMyTouchListener(myTouchListener);

    }
    class HomeAsync extends AsyncTask<Void,Integer,Void>{

        @Override
        protected Void doInBackground(Void... params) {
            imageload("http://103.244.59.105:8014/paopaoserver/ms/jimumentouzhaoxin.jpg");
            imageload("http://103.244.59.105:8014/paopaoserver/ms/wangjixiaolongxia.jpg");
            imageload("http://103.244.59.105:8014/paopaoserver/ms/xingfulikafeixin.jpg");
            imageload("http://103.244.59.105:8014/paopaoserver/ms/takexin.jpg");
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            onTuch();

        }
    }


}

