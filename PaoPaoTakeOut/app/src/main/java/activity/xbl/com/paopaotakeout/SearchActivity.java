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
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.xbl.adapter.MyBaseAdapter;
import com.xbl.cache.AsyncImageLoader;
import com.xbl.com.xbl.vo.FoodVo;
import com.xbl.com.xbl.vo.StoreVo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by HG on 2016/11/22.
 */
public class SearchActivity extends Activity {
    private String product_name;
    private List<FoodVo> mFoodList = new ArrayList<>();
    private List<StoreVo> mStoreList = new ArrayList<>();
    private ImageView mPicIv;
    private TextView mNameTv;
    private TextView mLine2Tv;
    private TextView mPriceTv;
    private ListView listView;
    private String urlsou;
    private ImageView mBackIv;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        x.view().inject(this);
        listView= (ListView) findViewById(R.id.search_food_list_view);
        mBackIv= (ImageView) findViewById(R.id.search_back_iv);
        Intent intent = getIntent();
        product_name = intent.getStringExtra("product_name");
        try {
            urlsou=getutf(product_name);
        } catch (Exception e) {
            e.printStackTrace();
        }
        new SerachAsync().execute();
        mBackIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1=new Intent(SearchActivity.this,MainActicity.class);
                startActivity(intent1);
            }
        });


    }
    //转UTF-8
    private String getutf(String urlstr) throws Exception {
        urlsou = URLEncoder.encode(urlstr, "UTF-8");
        return urlsou;

    }

    private void getHttpData() {

        String str_url = "http://103.244.59.105:8014/paopaoserver/allsearch?params={\"product_name\":\""+urlsou+"\",\"page\":1,\"page_count\":10}";
        try {
            URL url = new URL(str_url);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            InputStream inputStream = connection.getInputStream();
            BufferedReader bf = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder sb = new StringBuilder();
            String tmp ="";
            while ((tmp = bf.readLine()) != null) {
                sb.append(tmp);
            }

            String str_json = sb.toString();
            Log.e("TAG",str_json);
            JSONObject jsonObject = new JSONObject(str_json);
            JSONArray array = jsonObject.getJSONArray("datas");
            for (int i = 0; i < array.length(); i++) {
                JSONObject object = array.getJSONObject(i);
                String product_name = object.getString("product_name");
                String product_address = object.getString("product_address");
                String small_pic = object.getString("small_pic");
                int nowprice = object.getInt("nowprice");
                FoodVo vo = new FoodVo();
                vo.setProduct_name(product_name);
                vo.setSmall_pic(small_pic);
                vo.setProduct_address(product_address);
                vo.setNowprice(nowprice);
                mFoodList.add(vo);
                Log.e("TAG",mFoodList.size()+"");
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    class SearchAdapter extends MyBaseAdapter {


        public SearchAdapter(Context context, List list) {
            super(context, list);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View v= LayoutInflater.from(SearchActivity.this).inflate(R.layout.item_store,null);
            mNameTv= (TextView) v.findViewById(R.id.item_store_name_tv);
            mLine2Tv= (TextView) v.findViewById(R.id.item_store_address_tv);
            mPriceTv= (TextView) findViewById(R.id.item_store_time_tv);
            mPicIv= (ImageView) v.findViewById(R.id.item_store_iv);
            mNameTv.setText(mFoodList.get(position).getProduct_name());
            mLine2Tv.setText(mFoodList.get(position).getProduct_address());
           // mPriceTv.setText(mFoodList.get(position).getNowprice()+"");
            AsyncImageLoader imageLoader=new AsyncImageLoader();
            imageLoader.loadBitMap(listView,mPicIv,
                    "http://103.244.59.105:8014/paopaoserver"+mFoodList.get(position).getSmall_pic());
            return v;
        }
    }
    class SerachAsync extends AsyncTask<Void,Integer,Void>{

        @Override
        protected Void doInBackground(Void... params) {
            getHttpData();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            SearchAdapter adapter=new SearchAdapter(SearchActivity.this,mFoodList);
            listView.setAdapter(adapter);
        }


    }
}
