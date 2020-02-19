package activity.xbl.com.paopaotakeout;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.xbl.db.MyDBHelper;

import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

/**
 * Created by HG on 2016/11/7.
 */
public class LoginActivity extends Activity {
    @ViewInject(R.id.login_remember_pass)
    private CheckBox mRememberPass;
    @ViewInject(R.id.login_auto_login)
    private CheckBox mAutoLogin;
    @ViewInject(R.id.login_phone_et)
    private EditText mPhoneEt;
    @ViewInject(R.id.login_pass_et)
    private EditText mPassEt;
    @ViewInject(R.id.login_login_btn)
    private Button mLoginBtn;
    @ViewInject(R.id.login_register_btn)
    private Button mRegisterBtn;
    private SharedPreferences.Editor editor;
    private String phone;
    private String pass;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        x.view().inject(this);
        //得到用户输入的信息
        enterState();


    }
    @Event(value = {R.id.login_login_btn,R.id.login_register_btn})
    private void onClick(View v){
        switch (v.getId()){
            case R.id.login_login_btn:
                //判断用户名密码是否一样，一样就跳转不一样就提示
                if (judgeUser()){
                    Intent intent =new Intent();
                    intent.setClass(LoginActivity.this,CarActivity.class);
                    intent.putExtra("check_car",true);
                    startActivity(intent);
                }
                //判断是否自动登录
                break;
            case R.id.login_register_btn:
                //跳转到注册页面
                Intent intent =new Intent();
                intent.setClass(LoginActivity.this,RegisterActivity.class);
                startActivity(intent);
                break;
        }

    }

    private void saveUserData() {
        //如果记住密码了就保存此用户记住密码的信息
        SharedPreferences sharedPreferences=getSharedPreferences("user_data",MODE_PRIVATE);
        editor=sharedPreferences.edit();
        if (mRememberPass.isChecked()){
            editor.putString("user_phone",phone);
            editor.putString("user_pass",pass);
            editor.putBoolean("remember_pass",true);
            editor.commit();
            if (mAutoLogin.isChecked()){
                editor.putBoolean("auto",true);
                editor.commit();
            }else {
                editor.putBoolean("auto",false);
                editor.commit();
            }
        }else{
            editor.clear();
            editor.commit();
        }

    }
    private boolean judgeUser(){
        phone=mPhoneEt.getText().toString();
        pass=mPassEt.getText().toString();
      MyDBHelper  myDBHelper =new MyDBHelper(this,"paopao_register_db",null,1);
      SQLiteDatabase db=myDBHelper.getReadableDatabase();
        Cursor cursor=db.query("user",null,"phone=?",new String[]{phone},null,null,null);
        Log.e("AAA",phone);
        boolean flag= cursor.moveToFirst();//如果查询不到会返回一个false
        if (flag){//如果应户名存在，判断密码是否一致
            if (pass.equals(cursor.getString(cursor.getColumnIndex("pass")))){//如果密码也相等，登录成功
                Toast.makeText(this,"登录成功",Toast.LENGTH_LONG).show();
                saveUserData();
                return  true;
            }else{
                Toast.makeText(this,"密码不正确",Toast.LENGTH_LONG).show();
                return  false;
            }

        }else {
            Toast.makeText(this,"用户名不存在",Toast.LENGTH_LONG).show();
            return false;
        }
    }
    //查看在一进入跳转的时候是什么状态，时候自动登录，是否记住用户名密码
    private void enterState(){
        SharedPreferences sharedPreferences=getSharedPreferences("user_data",MODE_PRIVATE);//打开一个存储数据的对象
        boolean isAutoLogin=sharedPreferences.getBoolean("auto_login",false);
        boolean isRemember =sharedPreferences.getBoolean("remember_pass",false);//如果没有存入这个值默认返回false
        //如果这个用户曾经保存过密码就对文本控件进行设置
        if (isAutoLogin){
            Intent intent=new Intent();
            intent.setClass(LoginActivity.this,MainActicity.class);
            startActivity(intent);
        }
        if (isRemember){
            String user_phone =sharedPreferences.getString("user_phone","");
            String user_pass=sharedPreferences.getString("user_pass","");
            mPhoneEt.setText(user_phone);
            mPassEt.setText(user_pass);
            mRememberPass.setChecked(true);
        }





    }

}
