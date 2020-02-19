package activity.xbl.com.paopaotakeout;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import com.xbl.fragment.CompletedFragment;
import com.xbl.fragment.HomeFragment;
import com.xbl.fragment.UnFishedFragment;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;

/**
 * Created by HG on 2016/11/7.
 */


public class MainActicity extends FragmentActivity {
    @ViewInject(R.id.main_rb)
    private RadioGroup mRg;
    @ViewInject(R.id.main_home_rb)
    private RadioButton mHomeRb;
    @ViewInject(R.id.main_completed_rb)
    private RadioButton mCompletesRb;
    @ViewInject(R.id.main_unfinished_rb)
    private RadioButton mUnfinishedRb;
    private HomeFragment homeFragment;
    private CompletedFragment completedFragment;
    private UnFishedFragment unFishedFragment;
    private FragmentManager fm;
    private FragmentTransaction ft;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        x.view().inject(this);
        initFragment();



        mRg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                ft = fm.beginTransaction();
                switch (checkedId) {
                    case R.id.main_home_rb:
                        mHomeRb.setTextColor(Color.RED);
                        mUnfinishedRb.setTextColor(Color.WHITE);
                        mCompletesRb.setTextColor(Color.WHITE);
                        ft.replace(R.id.main_fragment, homeFragment);
                        Log.d("TAG", "main_home_rb");
                        break;
                    case R.id.main_completed_rb:
                        mHomeRb.setTextColor(Color.WHITE);
                        mUnfinishedRb.setTextColor(Color.WHITE);
                        mCompletesRb.setTextColor(Color.RED);
                        ft.replace(R.id.main_fragment, completedFragment);
                        break;
                    case R.id.main_unfinished_rb:
                        mHomeRb.setTextColor(Color.WHITE);
                        mUnfinishedRb.setTextColor(Color.RED);
                        mCompletesRb.setTextColor(Color.WHITE);
                        ft.replace(R.id.main_fragment, unFishedFragment);
                        break;
                }
                //只能commit一次，要不得重新创建一个事物
                ft.commit();
            }
        });
        if (savedInstanceState == null) {
            mHomeRb.setChecked(true);
        }



    }

    private void initFragment() {
        fm = getSupportFragmentManager();
        homeFragment = new HomeFragment();
        completedFragment = new CompletedFragment();
        unFishedFragment = new UnFishedFragment();
    }







    public interface  MyTouchListener {
        public void onTouchEvent(MotionEvent event);

    }
    // 保存MyTouchListener接口的列表
    private ArrayList<MyTouchListener> myTouchListeners = new ArrayList<MyTouchListener>();

    /**
     * 提供给Fragment通过getActivity()方法来注册自己的触摸事件的方法
     * @param listener
     */
    public void registerMyTouchListener(MyTouchListener listener) {
        myTouchListeners.add(listener);
    }

    /**
     * 提供给Fragment通过getActivity()方法来取消注册自己的触摸事件的方法
     * @param listener
     */
    public void unRegisterMyTouchListener(MyTouchListener listener) {
        myTouchListeners.remove( listener );
    }

    /**
     * 分发触摸事件给所有注册了MyTouchListener的接口
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        for (MyTouchListener listener : myTouchListeners) {
            listener.onTouchEvent(ev);
        }
        return super.dispatchTouchEvent(ev);
    }



}
