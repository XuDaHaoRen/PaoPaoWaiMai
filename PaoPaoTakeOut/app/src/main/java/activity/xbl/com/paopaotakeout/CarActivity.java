package activity.xbl.com.paopaotakeout;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.xbl.adapter.MyBaseAdapter;
import com.xbl.cache.AsyncImageLoader;
import com.xbl.com.xbl.vo.FlavourVo;
import com.xbl.com.xbl.vo.FoodVo;
import com.xbl.db.MyDBHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by HG on 2016/11/20.
 */
public class CarActivity extends Activity {
    @ViewInject(R.id.car_list_view)
    private ListView mListView;
    private TextView mFoodTv;
    private TextView mShopTv;
    private TextView mPriceTv;
    private ImageView mPicIv;
    @ViewInject(R.id.car_allmoney_tv)
    private TextView mAllTv;
    @ViewInject(R.id.car_account_btn)
    private Button mAccountBtn;
    @ViewInject(R.id.car_back_iv)
    private ImageView mBackIv;
    private int all_money=0;
    private Boolean is_pay;
    private int shop_id;
    private ImageView mDeleteIv;
    private List<Integer> mFlavourList=new ArrayList<>();
    private  CarAdapter carAdapter;
    @Nullable

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car);
        x.view().inject(this);
        Intent intent=getIntent();
        is_pay= intent.getBooleanExtra("is_pay",false);//判断是不是从pay页面跳转过来
        shop_id=intent.getIntExtra("shop_id",0);
        if(!is_pay){//如果是从pay页面跳转过来会返回一个true,返回true就不会重新再加载数据了
            new CarAsycnTask().execute();
        }


        mAccountBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(CarActivity.this,PayActivity.class);
                intent.putExtra("all_money",all_money+"");
                startActivity(intent);
            }
        });
        mBackIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1=new Intent (CarActivity.this,FoodActivity.class);
                intent1.putExtra("shop_id",shop_id);
                startActivity(intent1);
            }
        });
    }

    class CarAdapter extends MyBaseAdapter{

        public CarAdapter(Context context, List list) {
            super(context, list);
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            AsyncImageLoader imageLoader=new AsyncImageLoader();
            View view =LayoutInflater.from(CarActivity.this).inflate(R.layout.item_car,null);
            mFoodTv= (TextView) view.findViewById(R.id.item_car_name_tv);
            mShopTv= (TextView) view.findViewById(R.id.item_car_shop_tv);
            mPriceTv= (TextView) view.findViewById(R.id.item_car_money_tv);
            mPicIv= (ImageView) view.findViewById(R.id.item_carf_iv);
            mDeleteIv= (ImageView) view.findViewById(R.id.car_delete_iv);
            mFoodTv.setText(MainApplication.CAR_LIST.get(position).getProduct_name());
            mShopTv.setText(MainApplication.CAR_LIST.get(position).getProduct_address());
            mPriceTv.setText(MainApplication.CAR_LIST.get(position).getNowprice()+"");
            imageLoader.loadBitMap(mListView,mPicIv,"http://103.244.59.105:8014/paopaoserver"
                    +MainApplication.CAR_LIST.get(position).getSmall_pic());
           mDeleteIv.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                  new DeleteAsync().execute(position);

               }
           });
            return view ;
        }
    }
    private void getHttpData(){
        String str_car="http://103.244.59.105:8014/paopaoserver/getcart?params={\"user_id\":\"8888888\"}";
        try {
            URL url=new URL(str_car);
            HttpURLConnection connection= (HttpURLConnection) url.openConnection();
            InputStream inputStream=connection.getInputStream();
            BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder sb=new StringBuilder();
            String tmp="";
            while((tmp=bufferedReader.readLine())!=null){
                sb.append(tmp);

            }
            String str_json=sb.toString();
            JSONObject jsonObject=  new JSONObject(str_json);
            JSONArray jsonArray=jsonObject.getJSONArray("datas");
            for(int i=0;i<jsonArray.length();i++){
                JSONObject object=jsonArray.getJSONObject(i);
                String product_name=object.getString("product_name");
                int nowprice=object.getInt("nowprice");
                String category_name=object.getString("category_name");
                String small_pic=object.getString("small_pic");
                int product_id=object.getInt("product_id");
                mFlavourList.add(product_id);
                FoodVo vo=new FoodVo();//保存在List集合当中
                vo.setNowprice(nowprice);
                vo.setProduct_address(category_name);
                vo.setSmall_pic(small_pic);
                vo.setProduct_name(product_name);
                MainApplication.CAR_LIST.add(vo);

            }


        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    class CarAsycnTask extends AsyncTask<Void,Integer,Void>{

        @Override
        protected Void doInBackground(Void... params) {
            getHttpData();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
             carAdapter=new CarAdapter(CarActivity.this,MainApplication.CAR_LIST);
            mListView.setAdapter(carAdapter);
            for(int i=0;i<MainApplication.CAR_LIST.size();i++){
                all_money=all_money+MainApplication.CAR_LIST.get(i).getNowprice();
            }
            mAllTv.setText(all_money+"");

        }
    }
    class DeleteAsync extends AsyncTask<Integer,Void,Void>{


        @Override
        protected Void doInBackground(Integer... params) {
            String str_url="http://103.244.59.105:8014/paopaoserver/deletecart?params={\"user_id\":\"8888888\",\"product_id\":\""
                    +mFlavourList.get(params[0])+"\"}";
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
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
//            carAdapter.notify();
        }
    }


}
