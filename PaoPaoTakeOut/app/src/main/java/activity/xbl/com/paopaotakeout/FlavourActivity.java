package activity.xbl.com.paopaotakeout;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.xbl.adapter.MyBaseAdapter;
import com.xbl.cache.AsyncImageLoader;
import com.xbl.com.xbl.vo.FlavourVo;

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

import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;

/**
 * Created by HG on 2016/11/8.
 */
public class FlavourActivity extends Activity {
    @ViewInject(R.id.flavour_back_btn)
    private ImageView mBackIv;
    private List<FlavourVo> mList;
    @ViewInject(R.id.flavour_buy_car_iv)
    private ImageView mBuyCarIv;
    @ViewInject(R.id.flavour_name_tv)
    private  TextView mNameTv;
    private int classify_id;
    private  String classify_name;
    @ViewInject(R.id.flavour_list_view)
    private PullToRefreshListView pListView;
    @ViewInject(R.id.flavour_buy_car_iv)
    private ImageView mCar;
    private int count=10;
    private List<FlavourVo>mTmpList;
    private FlavourBaseAdapter flavourBaseAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flavour);
        x.view().inject(this);
        //设置时间
        getHttpData();
        //设置PullToRefresh刷新模式
        pListView.setMode(PullToRefreshBase.Mode.BOTH);
        //设置刷新监听
        pListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {

                String str = DateUtils.formatDateTime(FlavourActivity.this, System.currentTimeMillis(), DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);
                // 下拉刷新 业务代码
                if (refreshView.isShownHeader()) {
                    pListView.getLoadingLayoutProxy().setRefreshingLabel("正在刷新");
                    pListView.getLoadingLayoutProxy().setPullLabel("下拉刷新");
                    pListView.getLoadingLayoutProxy().setReleaseLabel("释放开始刷新");
                    refreshView.getLoadingLayoutProxy().setLastUpdatedLabel("最后更新时间:" + str);
                 getHttpData();
                }
                // 上拉加载更多 业务代码
                if(refreshView.isShownFooter()) {
                    pListView.getLoadingLayoutProxy().setRefreshingLabel("正在加载");
                    pListView.getLoadingLayoutProxy().setPullLabel("上拉加载更多");
                    pListView.getLoadingLayoutProxy().setReleaseLabel("释放开始加载");
                    refreshView.getLoadingLayoutProxy().setLastUpdatedLabel("最后加载时间:" + str);
                   //数据的增加
                    getHttpLesData();

                }
            }
        });




    }
    //上拉刷新
    private void getHttpLesData() {
        mList=new ArrayList<>();
       count=count+10;
        Intent intent = this.getIntent();    //获得当前的Intent
        Bundle bundle = intent.getExtras();  //获得全部数据
        classify_id = bundle.getInt("classify_id");
        classify_name=bundle.getString("classify_name");
        mNameTv.setText(classify_name);
        Log.e("classify_id",+classify_id+"");
        RequestParams requestParams = new RequestParams("http://103.244.59.105:8014/paopaoserver/categorylist");
        //异步任务
        requestParams
                .addQueryStringParameter("params", "{\"classify_id\":"+classify_id+",\"page\":1,\"page_count\":"+count+"}");
        Log.e("tag",requestParams.toString());
        x.http().get(requestParams, new Callback.CacheCallback<List<FlavourVo>>() {
            @Override
            public void onSuccess(List<FlavourVo> result) {
                for (FlavourVo flavourVo :result){
                    Log.e("tag1",flavourVo.toString());
                    mList.add(flavourVo);
                }

                FlavourBaseAdapter  flavourBaseAdapter =new FlavourBaseAdapter(FlavourActivity.this,mList);
                pListView.setAdapter(flavourBaseAdapter);
                pListView.onRefreshComplete();



                for (FlavourVo flavourVo :mList){
                    Log.e("tag2",flavourVo.toString()+"111");

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
            @Override
            public boolean onCache(List<FlavourVo> result) {
                return false;
            }
        });




    }

    @Event(value = {R.id.flavour_back_btn, R.id.flavour_buy_car_iv})
    private void onClick(View v) {
        switch (v.getId()) {
            case R.id.flavour_back_btn:
                Log.d("TAG", "item_food_back_iv");
                Intent intent=new Intent(FlavourActivity.this,MainActicity.class);
                startActivity(intent);
                break;
            case R.id.flavour_buy_car_iv:
                Log.d("TAG", "item_food_back_iv");
                Intent intent1=new Intent(FlavourActivity.this,CarActivity.class);
                startActivity(intent1);
                break;

        }

    }
    private void getHttpData(){
        mList=new ArrayList<>();
        Intent intent = this.getIntent();    //获得当前的Intent
        Bundle bundle = intent.getExtras();  //获得全部数据
        classify_id = bundle.getInt("classify_id");
        classify_name=bundle.getString("classify_name");
        mNameTv.setText(classify_name);
        Log.e("classify_id",+classify_id+"");
        RequestParams requestParams = new RequestParams("http://103.244.59.105:8014/paopaoserver/categorylist");

        requestParams
                .addQueryStringParameter("params", "{\"classify_id\":"+classify_id+",\"page\":1,\"page_count\":10}");
        Log.e("tag",requestParams.toString());
        x.http().get(requestParams, new Callback.CacheCallback<List<FlavourVo>>() {
            @Override
            public void onSuccess(List<FlavourVo> result) {
                for (FlavourVo flavourVo :result){
                    Log.e("tag1",flavourVo.toString());
                    mList.add(flavourVo);
                }
                flavourBaseAdapter =new FlavourBaseAdapter(FlavourActivity.this,mList);
                pListView.setAdapter(flavourBaseAdapter);
                pListView.onRefreshComplete();



                for (FlavourVo flavourVo :mList){
                    Log.e("tag2",flavourVo.toString()+"111");

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
            @Override
            public boolean onCache(List<FlavourVo> result) {
                return false;
            }
        });
    }


        class FlavourBaseAdapter extends MyBaseAdapter {
            @Override
            public int getCount() {
                return mList.size();
            }

            AsyncImageLoader imageLoader = new AsyncImageLoader();

            public FlavourBaseAdapter(Context context, List list) {
                super(context, list);
            }

            @Override
            public View getView(final int position, View convertView, ViewGroup parent) {
                ViewHolder viewHolder = null;
                if (convertView == null) {
                    viewHolder = new ViewHolder();
                    convertView = LayoutInflater.from(FlavourActivity.this).inflate(R.layout.item_food_falvour, null);
                    x.view().inject(this, convertView);
                    viewHolder.mNameTv = (TextView) convertView.findViewById(R.id.item_food_name_tv);
                    viewHolder.mLine2Tv = (TextView) convertView.findViewById(R.id.item_food_line2_tv);
                    viewHolder.mMoneyTv = (TextView) convertView.findViewById(R.id.item_food_money_tv);
                    viewHolder.mPicIv = (ImageView) convertView.findViewById(R.id.item_food_pic_iv);
                    viewHolder.mAddIv = (ImageView) convertView.findViewById(R.id.item_food_add_iv);
                    viewHolder.mLesIv = (ImageView) convertView.findViewById(R.id.item_food_les_iv);
                    viewHolder.mBuyBtn = (Button) convertView.findViewById(R.id.item_food_buy_btn);
                    viewHolder.mNumTv = (TextView) convertView.findViewById(R.id.item_food_num_tv);
                    convertView.setTag(viewHolder);
                } else {
                    viewHolder = (ViewHolder) convertView.getTag();
                }
                viewHolder.mNameTv.setText(mList.get(position).getProduct_name());
                viewHolder.mLine2Tv.setText(mList.get(position).getCategory_name() + "-"
                        + mList.get(position).getClassify_name());
                viewHolder.mMoneyTv.setText(mList.get(position).getNowprice()+"");
                imageLoader.loadBitMap(pListView, viewHolder.mPicIv, "http://103.244.59.105:8014/paopaoserver"
                        + mList.get(position).getSmall_pic());
                viewHolder.mBuyBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                String str_url="http://103.244.59.105:8014/paopaoserver/addcart?params={\"user_id\":\"8888888\",\"product_id\":\""+mList.get(position).getProduct_id()+"\",\"product_num\":\"3\"}";
                                try {
                                    URL url=new URL(str_url);
                                    HttpURLConnection connection= (HttpURLConnection) url.openConnection();
                                    connection.setRequestMethod("GET");
                                    InputStream inputStream=connection.getInputStream();
                                } catch (MalformedURLException e) {
                                    e.printStackTrace();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }).start();
                            Toast.makeText(FlavourActivity.this,"已放到购物车中",Toast.LENGTH_SHORT).show();
                    }
                });

                return convertView;
            }


            @Event(value = {R.id.flavour_back_btn, R.id.item_food_add_iv, R.id.item_food_les_iv, R.id.item_food_buy_btn})
            private void onClick(View v) {
                switch (v.getId()) {
                    case R.id.flavour_back_btn:
                        Log.d("TAG", "item_food_back_iv");
                        finish();
                        break;
                    case R.id.item_food_add_iv:
                        Log.d("TAG", "item_food_add_iv");
                        finish();
                        break;
                }

            }


        }
    class ViewHolder {

        private ImageView mAddIv;
        private ImageView mLesIv;
        private TextView mNumTv;
        private ImageView mPicIv;
        private TextView mNameTv;
        private TextView mLine2Tv;
        private Button mBuyBtn;
        private TextView mMoneyTv;

    }


    }












