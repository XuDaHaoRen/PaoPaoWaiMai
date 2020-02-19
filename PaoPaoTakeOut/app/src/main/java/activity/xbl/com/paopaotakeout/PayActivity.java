package activity.xbl.com.paopaotakeout;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

/**
 * Created by HG on 2016/11/8.
 */
public class PayActivity extends Activity {
    @ViewInject(R.id.pay_allmoney_tv)
    private TextView mAllMoneyTv;
    @ViewInject(R.id.pay_pay_btn)
    private TextView mPayBtn;
    @ViewInject(R.id.pay_back_iv)
    private ImageView mBackIv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay);
        x.view().inject(this);
        final Intent intent =getIntent();
        String allMoney=intent.getStringExtra("all_money");
        mAllMoneyTv.setText(allMoney);
        mPayBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 =new Intent(PayActivity.this,PayStateActivity.class);
                startActivity(intent1);
            }
        });
        mBackIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1=new Intent(PayActivity.this, CarActivity.class);
                intent1.putExtra("is_pay",true);
                startActivity(intent1);
            }
        });
    }
}
