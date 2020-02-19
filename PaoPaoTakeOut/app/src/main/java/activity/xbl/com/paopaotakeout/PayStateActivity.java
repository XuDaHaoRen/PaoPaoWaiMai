package activity.xbl.com.paopaotakeout;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.xbl.com.xbl.vo.FoodVo;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by HG on 2016/11/23.
 */
public class PayStateActivity extends Activity {
    private Button mOkBtn;
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_state);
        MainApplication.CAR_LIST =new ArrayList<>();
        new PayStateAsync().execute();
        mOkBtn= (Button) findViewById(R.id.state_ok_btn);
        mOkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(PayStateActivity.this,MainActicity.class);
                startActivity(intent);

            }
        });

    }
    private void getHttpData(){
        String str_url="http://103.244.59.105:8014/paopaoserver/clearcart?params={\"user_id\":8888888}";
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


    class PayStateAsync extends AsyncTask<Void,Void,Void>{

        @Override
        protected Void doInBackground(Void... params) {
            getHttpData();
            return null;
        }
    }
}
