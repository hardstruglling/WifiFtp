package com.hipad.swiftp.gui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.hipad.swiftp.R;

public class ConnectActivity extends AppCompatActivity implements
        View.OnClickListener {

    private TextView tv_setting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect);
        initView();
    }

    private void initView() {
        tv_setting = ((TextView) findViewById(R.id.tv_setting));

        tv_setting.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_setting:
                startActivity(new Intent(this,SettingActivity.class));
                break;
        }
    }
}
