package activity.xbl.com.paopaotakeout;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.xbl.adapter.MyBaseAdapter;
import com.xbl.cache.AsyncImageLoader;
import com.xbl.com.xbl.vo.FoodVo;
import com.xbl.db.MyDBHelper;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.header.StoreHouseHeader;

/**
 * Created by HG on 2016/11/8.
 */
public class FoodActivity extends Activity {
    @ViewInject(R.id.food_list_view)
    private ListView mListView;
    @ViewInject(R.id.food_back_iv)
    private ImageView mBackIv;
    @ViewInject(R.id.food_ptr)
    private PtrFrameLayout foodPtr;
    @ViewInject(R.id.food_buy_car_iv)
    private ImageView mCarIv;
    private List<FoodVo>mList=new ArrayList<>();
    private ViewGroup animLayout;
    //小球的ImageView
    private ImageView imageView;
    private int shop_id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food);
        x.view().inject(this);
        // header
        initHead();
        //进行自动刷新
        foodPtr.autoRefresh(true);
        foodPtr.setPtrHandler(new PtrDefaultHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                getHttpData();
            }
        });
        mBackIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Intent intent=new Intent(FoodActivity.this,StoreActivity.class);
                startActivity(intent);
            }
        });
        mCarIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyDBHelper myDBHelper =new MyDBHelper(FoodActivity.this,"paopao_register_db",null,1);
                SQLiteDatabase db=myDBHelper.getReadableDatabase();
                Cursor cursor=db.query("user",null,null,null,null,null,null);
                boolean flag= cursor.moveToFirst();//如果查询不到会返回一个false
                Log.e("TAG2","flag"+flag);
                if (flag){
                    Intent intent =new Intent(FoodActivity.this,CarActivity.class);
                    intent.putExtra("check_car",true);
                    intent.putExtra("shop_id",shop_id);
                    startActivity(intent);
                }else{
                    Intent intent =new Intent(FoodActivity.this,LoginActivity.class);
                    startActivity(intent);
                }
            }
        });
        getHttpData();


    }
    private void initHead(){
        final StoreHouseHeader header = new StoreHouseHeader(FoodActivity.this);
        //设置头部的内间距，据顶部20px
        header.setPadding(0, 20, 0, 0);
        //字体默认为白色，背景默认为白色，所以将字体改成黑
        header.setTextColor(Color.BLACK);
        //写下拉刷新后的字符串（这里的字符串要是英文的不然会报错）
        header.initWithString("Waiting");
        //添加一个头部布局
        foodPtr.setHeaderView(header);
        foodPtr.addPtrUIHandler(header);
    }
    //在布局上面添加一层透明动画，并将这个动画层返回出去
    private ViewGroup creatAnimLayout(){
        //得到根布局
        ViewGroup viewGroup= (ViewGroup) this.getWindow().getDecorView();
        //创建一个透明层
        LinearLayout animLayout=new LinearLayout(this);
        //设置宽高属性
        LinearLayout .LayoutParams  lp=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT) ;
        animLayout.setLayoutParams(lp);
        //设置背景为透明
        animLayout.setBackgroundResource(android.R.color.transparent);
        //添加动画
        viewGroup.addView(animLayout);
        return  animLayout;
    }
    //添加小球视图到动画层
    public void addViewToLayout(ViewGroup viewGroup,View view,int []location){
        viewGroup.addView(view);
        //得到小球开始运动时的坐标，设置动画起点
        int x=location[0];
        int y=location[1];
        LinearLayout.LayoutParams lp=new LinearLayout.LayoutParams(70, 70);
        //对小球的xy坐标进行设置
        lp.leftMargin=x;
        lp.topMargin=y;
        //设置到View中
        view.setLayoutParams(lp);
    }
    //实现动画
    private void initAnim(final View view, int [] location){
        //构造动画层
        animLayout=creatAnimLayout();
        addViewToLayout(animLayout,view,location);
        //小球运动轨迹，x匀速运动，y加速运动
        //获得终点的x,y轴坐标
        int []end_location=new int [2];
        //把控件xy的坐标放到数组里面
        mCarIv.getLocationInWindow(end_location);
        //计算位移
        int dx=end_location[0]-location[0];
        int dy=end_location[1]-location[1];
        //构造x轴平移动画--补间动画
        TranslateAnimation translateAnimationX=new TranslateAnimation(0,dx,0,0);
        translateAnimationX.setInterpolator(new LinearInterpolator());//设置速度匀速运动
        translateAnimationX.setFillAfter(true);//停在当前的位置
        TranslateAnimation translateAnimationY=new TranslateAnimation(0,0,0,dy);
        translateAnimationY.setInterpolator(new AccelerateInterpolator());//加速运动
        translateAnimationY.setFillAfter(true);
        //合成动画
        AnimationSet animationSet=new AnimationSet(false);//需不需要公用一个加速器---速度不一样不需要
        animationSet.addAnimation(translateAnimationX);
        animationSet.addAnimation(translateAnimationY);
        animationSet.setDuration(800);
        //动画监听器
        animationSet.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                view.setVisibility(View.VISIBLE);

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                view.setVisibility(View.GONE);

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        //将ImageView加载上动画
        view.setAnimation(animationSet);

    }

    private List<FoodVo> getHttpData() {
        //根据不同的商铺id，加载不同的美食数据
        Intent intent = this.getIntent();    //获得当前的Intent
        Bundle bundle = intent.getExtras();  //获得全部数据
         shop_id = bundle.getInt("shop_id");
        Log.e("shop_id",shop_id+"");

        RequestParams requestParams = new RequestParams("http://103.244.59.105:8014/paopaoserver/findfood");

        requestParams
                .addQueryStringParameter("params", "{\"shop_id\":"+shop_id+",\"page\":1,\"page_count\":5}");

        Log.e("tag",requestParams.toString());
        x.http().get(requestParams, new Callback.CacheCallback<List<FoodVo>>() {

            @Override
            public void onSuccess(List<FoodVo> result) {
                for (FoodVo foodVo :result){
                    Log.e("tag",foodVo.toString());
                    mList.add(foodVo);
                }
                FoodBaseAdapter foodBaseAdapter =new FoodBaseAdapter(FoodActivity.this,mList);
                mListView.setAdapter(foodBaseAdapter);
                foodPtr.refreshComplete();//关闭下拉刷新

                for (FoodVo foodVo:mList){
                    Log.e("tag",foodVo.toString()+"111");
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
            public boolean onCache(List<FoodVo> result) {
                return false;
            }
        });
        return mList;
    }
    class FoodBaseAdapter extends MyBaseAdapter{

        public FoodBaseAdapter(Context context, List list) {
            super(context, list);
        }
        AsyncImageLoader imageLoader=new AsyncImageLoader();

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder=null;
            if(convertView==null){
                viewHolder=new ViewHolder();
                convertView= LayoutInflater.from(FoodActivity.this).inflate(R.layout.item_food_falvour,null);
                viewHolder.mNameTv= (TextView) convertView.findViewById(R.id.item_food_name_tv);
                viewHolder.mLine2Tv= (TextView) convertView.findViewById(R.id.item_food_line2_tv);
                viewHolder.mMoneyTv= (TextView) convertView.findViewById(R.id.item_food_money_tv);
                viewHolder.mPicIv= (ImageView) convertView.findViewById(R.id.item_food_pic_iv);
                viewHolder.mAddIv= (ImageView) convertView.findViewById(R.id.item_food_add_iv);
                viewHolder.mLesIv= (ImageView) convertView.findViewById(R.id.item_food_les_iv);
                viewHolder.mBuyBtn= (Button) convertView.findViewById(R.id.item_food_buy_btn);
                convertView.setTag(viewHolder);
            }else{
                viewHolder= (ViewHolder) convertView.getTag();
            }
            viewHolder.mNameTv.setText(mList.get(position).getProduct_name()+"");
            viewHolder.mLine2Tv.setText(mList.get(position).getProduct_address());
            viewHolder.mMoneyTv.setText(mList.get(position).getNowprice()+"");
            imageLoader.loadBitMap(mListView,viewHolder.mPicIv,
                    "http://103.244.59.105:8014/paopaoserver"+mList.get(position).getSmall_pic());
            viewHolder.mBuyBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //获得按钮的x,y轴坐标
                    int []start_location=new int [2];
                    //把控件xy的坐标放到数组里面
                    v.getLocationInWindow(start_location);
                    //创建一个ImageView
                    imageView=new ImageView(FoodActivity.this);
                    //为ImageView添加图片
                    imageView.setImageResource(R.drawable.qiuqiu);
                    //为图片添加动画
                    initAnim(imageView,start_location);
                    MainApplication.CAR_LIST.add(mList.get(position));
                }
            });
            return convertView;
        }
    }
    class ViewHolder{

        private ImageView mAddIv;
        private ImageView mLesIv;
        private TextView mNumTv;
        private ImageView mPicIv;
        private TextView mNameTv;
        private TextView mLine2Tv;
        private Button mBuyBtn;
        private TextView mMoneyTv;

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("shop_id", shop_id);

    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        shop_id=savedInstanceState.getInt("shop_id");
    }
}



