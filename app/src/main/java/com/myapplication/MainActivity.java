package com.myapplication;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private List<SignView> mSignViews;
    private TextView tv_sign;//签到按钮
    private int signNum = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        mSignViews = new ArrayList<>();
        mSignViews.add((SignView) findViewById(R.id.sv_1));
        mSignViews.add((SignView) findViewById(R.id.sv_2));
        mSignViews.add((SignView) findViewById(R.id.sv_3));
        mSignViews.add((SignView) findViewById(R.id.sv_4));
        mSignViews.add((SignView) findViewById(R.id.sv_5));
        mSignViews.add((SignView) findViewById(R.id.sv_6));
        mSignViews.add((SignView) findViewById(R.id.sv_7));
        initSignView();

        tv_sign = findViewById(R.id.tv_sign);
        tv_sign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setSignContent(++signNum);
            }
        });
    }

    private void initSignView() {
        //设置SignView
        for (int i = 0; i < mSignViews.size(); i++) {
            String textTop = String.valueOf(i);
            String textBottom = i == 0 ? "今天" : "Day" + (i + 1);
            mSignViews.get(i).setTextTopAndBottom(textTop, textBottom, 0, 2);
        }
    }

    //设置签到内容
    private void setSignContent(int sign) {
        if (sign > 0) {
            if (sign <= 7) {
                tv_sign.setText("已签到" + sign + "天");
                tv_sign.setTextColor(Color.parseColor("#606060"));
                tv_sign.setBackgroundResource(R.drawable.bt_signed);
                setSignView(sign);
            } else {
                Toast.makeText(this, "本周已签到7天", Toast.LENGTH_SHORT).show();
            }

        } else {
            tv_sign.setText("今日未签到");
            tv_sign.setTextColor(ContextCompat.getColor(this, R.color.red));
            tv_sign.setBackgroundResource(R.drawable.group_item_name);
        }

    }

    private void setSignView(int sign) {
        //设置SignView
        for (int i = 0; i < sign; i++) {
            String textTop = String.valueOf(sign);
            String textBottom = sign == 1 ? "今天" : "Day" + (i + 1);
            //int colorStyle = i < today ? 0 : (i == today ? (sign.IsSign ? 3 : 1) : 2);

            // lineStyle  显示的横线款式,0：都有线条，1左边无线条，2右边无线条；
            // colorStyle 显示的颜色款式,0：都是红色，1左红右灰，2都是灰色,3线左红右灰，圈是红色的
          if (i>0 && i < 6) {//i比sign小1
                mSignViews.get(i).setTextTopAndBottom(textTop, textBottom, 0, 0);//textTop文字宽度(已写死)  textBottom底部文字
            } else if (i == 6) {
                mSignViews.get(i).setTextTopAndBottom(textTop, textBottom, 2, 0);
            }else if(i==0){
                mSignViews.get(i).setTextTopAndBottom(textTop, textBottom, 1, 0);
            }
        }
    }
}
