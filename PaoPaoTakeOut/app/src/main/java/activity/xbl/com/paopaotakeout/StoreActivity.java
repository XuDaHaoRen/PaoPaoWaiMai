package activity.xbl.com.paopaotakeout;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.opengl.Visibility;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.xbl.adapter.MyBaseAdapter;
import com.xbl.cache.AsyncImageLoader;
import com.xbl.cache.Images;
import com.xbl.com.xbl.vo.StoreVo;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by HG on 2016/11/9.
 */
public class StoreActivity extends Activity {
    @ViewInject(R.id.store_list_view)
    private ListView mListView;
    @ViewInject(R.id.store_back_iv)
    private ImageView mBackIv;
    private List<StoreVo> mList=new ArrayList<>();
    @ViewInject(R.id.store_run_iv)
    private ImageView mRunIv;
    private AnimationDrawable animationDrawable;
    @ViewInject(R.id.store_run_tv)
    private TextView mRunTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store);
        x.view().inject(this);
         initAnimation();
        animationDrawable.start();
        getStoreData();

        //点击到不同的条目，跳转到不同的美食栏
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                intent.setClass(StoreActivity.this, FoodActivity.class);  //从IntentActivity跳转到SubActivity
                intent.putExtra("shop_id", mList.get(position).getShop_id());  //放入数据
                Log.e("123",mList.get(position).getShop_id()+"");
                startActivity(intent);

            }
        });

    }

    private void initAnimation() {
        //为图片设置一个背景的图片
        mRunIv.setBackgroundResource(R.drawable.frame);
        //将设置的背景图片转换成动画
        animationDrawable= (AnimationDrawable) mRunIv.getBackground();

    }

    @Event(value = {R.id.store_back_iv})
    private void onClick(View v){
        switch (v.getId()){
            case R.id.store_back_iv:
               Intent intent=new Intent(StoreActivity.this,MainActicity.class);
                startActivity(intent);
                break;
        }

    }
    private void getStoreData(){
        RequestParams requestParams = new RequestParams("http://103.244.59.105:8014/paopaoserver/getshop");
        requestParams
                .addQueryStringParameter("params","{\"classify_id\":0,\"counties_id\":0,\"page\":1,\"page_count\":100}");
        x.http().post(requestParams, new Callback.CacheCallback<List<StoreVo>>() {
            @Override
            public void onSuccess(List<StoreVo> result) {
                for(StoreVo storeVo:result){
                    Log.e("tag",storeVo.toString());
                    mList.add(storeVo);
                }

                StoreAdapter storeAdapter=new StoreAdapter(StoreActivity.this,mList);
                mListView.setAdapter(storeAdapter);
                animationDrawable.stop();
                mRunIv.setVisibility(View.GONE);
                mRunTv.setVisibility(View.GONE);

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
            public boolean onCache(List<StoreVo> result) {
                return false;
            }
        });

    }



    class StoreAdapter extends MyBaseAdapter{

        public StoreAdapter(Context context, List list) {
            super(context, list);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            StoreHolder storeHolder=null;
            if(convertView==null){
                storeHolder=new StoreHolder();
                convertView= LayoutInflater.from(StoreActivity.this).inflate(R.layout.item_store,null);
                storeHolder.mPicIv= (ImageView) convertView.findViewById(R.id.item_store_iv);
                storeHolder.mNameTv= (TextView) convertView.findViewById(R.id.item_store_name_tv);
                storeHolder.mAddressTv= (TextView) convertView.findViewById(R.id.item_store_address_tv);
                storeHolder.mTimeTv= (TextView) convertView.findViewById(R.id.item_store_time_tv);
                convertView.setTag(storeHolder);
            }else {
                storeHolder= (StoreHolder) convertView.getTag();
            }
            new AsyncImageLoader().loadBitMap
                    (mListView, storeHolder.mPicIv,"http://103.244.59.105:8014/paopaoserver"+mList.get(position).getSmall_pic());
            storeHolder.mNameTv.setText(mList.get(position).getShop_name());
            storeHolder.mAddressTv.setText(mList.get(position).getShop_address());
            storeHolder.mTimeTv.setText
                    (mList.get(position).getStart_time_str()+"-"+mList.get(position).getEnd_time_str());
            return convertView;
        }
    }
    class StoreHolder {
       private ImageView mPicIv;
        private TextView mNameTv;
        private TextView mAddressTv;
        private TextView mTimeTv;
    }


}
