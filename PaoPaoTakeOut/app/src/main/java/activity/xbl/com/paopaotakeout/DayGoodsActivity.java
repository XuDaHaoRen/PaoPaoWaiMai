package activity.xbl.com.paopaotakeout;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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

import com.xbl.adapter.MyBaseAdapter;

import com.xbl.com.xbl.vo.DayGoodsVo;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by HG on 2016/11/8.
 */
public class DayGoodsActivity extends Activity {
    @ViewInject(R.id.day_goods_lv)
    private ListView mListView;
    @ViewInject(R.id.goods_back_iv)
    private ImageView mBackIV;
    private List<DayGoodsVo> mList=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_day_goods);
        x.view().inject(this);
        getHttpData();

        mBackIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent intent=new Intent();
                        intent.setClass(DayGoodsActivity.this,FlavourActivity.class);
                        intent.putExtra("classify_id",mList.get(position).getClassify_id());
                        intent.putExtra("classify_name",mList.get(position).getClassify_name());
                        Log.e("classify_id",+mList.get(position).getClassify_id()+"");
                        startActivity(intent);

                }

        });


    }
    private  void getHttpData() {
        RequestParams requestParams =new RequestParams
                ("http://103.244.59.105:8014/paopaoserver/articles");
        requestParams.addQueryStringParameter("params","{\"page\":1,\"page_count\":10}");
        x.http().get(requestParams, new Callback.CacheCallback<List<DayGoodsVo>>() {
            @Override
            public void onSuccess(List<DayGoodsVo> result) {
                for(DayGoodsVo dayGoods :result){
                    Log.e("tag", dayGoods.toString());
                    mList.add(dayGoods);
                }
                mListView.setAdapter(new DayBaseAdapter(DayGoodsActivity.this,mList));
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
            public boolean onCache(List<DayGoodsVo> result) {
                return false;
            }
        });



    }

    class DayBaseAdapter extends MyBaseAdapter{

        public DayBaseAdapter(Context context, List list) {
            super(context, list);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view=LayoutInflater.from(DayGoodsActivity.this).inflate(R.layout.item_day_goods,null);
            TextView mNameTv= (TextView) view.findViewById(R.id.item_day_goods_tv);
            mNameTv.setText(mList.get(position).getClassify_name());
            return view;
        }
    }


    }


