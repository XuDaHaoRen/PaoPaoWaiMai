package activity.xbl.com.paopaotakeout;

import android.app.Application;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.xbl.com.xbl.vo.FoodVo;

import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

public class MainApplication extends Application {
  //放美食的全局变量
    public static List<FoodVo> CAR_LIST=new ArrayList<>();
    public static MainApplication instance;


    @Override
    public void onCreate() {
        super.onCreate();
        //框架实例化
        x.Ext.init(this);
        x.Ext.setDebug(BuildConfig.DEBUG);
        //实例化MainApplication
        instance = this;


    }
}
