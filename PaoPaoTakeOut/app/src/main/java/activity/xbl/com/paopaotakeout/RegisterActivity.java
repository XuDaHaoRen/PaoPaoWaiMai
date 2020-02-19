package activity.xbl.com.paopaotakeout;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.xbl.db.MyDBHelper;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

/**
 * Created by HG on 2016/11/7.
 */
public class RegisterActivity extends Activity {
    @ViewInject(R.id.register_back_btn)
    private Button mBackBtn;
    @ViewInject(R.id.register_phone_et)
    private EditText mPhoneEt;
    @ViewInject(R.id.register_pass_et)
    private EditText mPassEt;
    @ViewInject(R.id.register_nickname_et)
    private EditText mNicknameEt;
    @ViewInject(R.id.register_rg)
    private RadioGroup mRg;
    @ViewInject(R.id.register_man_rb)
    private RadioButton mManRb;
    @ViewInject(R.id.register_woman_rb)
    private RadioButton mWomanRb;
    @ViewInject(R.id.register_city_et)
    private EditText mCityEt;
    @ViewInject(R.id.register_counties_et)
    private EditText mCountiesEt;
    @ViewInject(R.id.register_street_et)
    private EditText mStreetEt;
    @ViewInject(R.id.register_commit_btn)
    private Button mCommitBtn;
    private MyDBHelper myDBHelper;
    private SQLiteDatabase db;
    String phone;
    String pass;
    String nickname;
    String sex;
    String address;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        x.view().inject(this);
        //提交数据保存到数据库,页面跳转到登录
        mCommitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerData();
              //  queryData();
            }
        });

    }

    private void registerData() {
        long row=(long)-1;//判断是否插入成功数据
         phone=mPhoneEt.getText().toString();
         pass=mPassEt.getText().toString();
         nickname=mNicknameEt.getText().toString();
         address=mCityEt.getText().toString()+mCountiesEt.getText().toString()+mStreetEt.getText().toString();
        switch (mRg.getCheckedRadioButtonId()){
            case R.id.register_man_rb:
                sex="男";
                break;
            case R.id.register_woman_rb:
                sex="女";
                break;
        }
        if (isFalse()){
            myDBHelper =new MyDBHelper(this,"paopao_register_db",null,1);
            db=myDBHelper.getReadableDatabase();
            ContentValues values=new ContentValues();
            values.put("phone",phone);
            values.put("pass",pass);
            values.put("nickname",nickname);
            values.put("sex",sex);
            values.put("address",address);
            if (!hadName()){
                row= db.insert("user",null,values);//增加数据
            }
            if(row>0){
                Toast.makeText(this,"注册成功",Toast.LENGTH_SHORT).show();
                Intent intent=new Intent();
                intent.setClass(RegisterActivity.this,LoginActivity.class);
                startActivity(intent);
            }else{
                Toast.makeText(this,"注册失败,本账号被注册",Toast.LENGTH_SHORT).show();
            }
        }else {
            Toast.makeText(this,"请将信息填写完整",Toast.LENGTH_SHORT).show();
        }

    }
    //判断EditText的内容是不是有空的
    private boolean isFalse(){
        String telRegex = "[1][3578]\\d{9}";
        if(phone.matches(telRegex)){
            return  true;
        }else {
            return false;
        }

    }
    private void queryData(){
        Cursor cursor=db.query("user",null,null,null,null,null,null);
        cursor.moveToFirst();
        do{
            String phone=cursor.getString(cursor.getColumnIndex("phone"));
            String pass=cursor.getString(cursor.getColumnIndex("pass"));
            Log.e("TAG","phone:"+phone+"  pass:"+pass);
        }while (cursor.moveToNext());
    }
    //判断是否有重名
    private boolean hadName(){
        Cursor cursor=db.query("user",null,"phone=?",new String[]{phone},null,null,null);
        return cursor.moveToFirst();//查询不道返回false，则可以增加数据

    }

}
