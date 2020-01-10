package com.example.inco;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class SettingActivity extends AppCompatActivity implements View.OnClickListener {

    public FirebaseAuth mAuth;
    public Intent lntent;
    public Intent dntent;
    public Intent tntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);


        findViewById(R.id.action_list).setOnClickListener(this);
        findViewById(R.id.action_chat).setOnClickListener(this);
        findViewById(R.id.action_setting).setOnClickListener(this);
        findViewById(R.id.action_logout).setOnClickListener(this);

    }


    public void onClick(View v) {
        if (v != null) {
            switch (v.getId()) {
                case R.id.notification:
                    Intent intent = new Intent(SettingActivity.this, NotificationActivity.class);
                    intent.putExtra("check", true);
                    startActivity(intent);
                    finish();
                    // クリック処理
                    break;

                case R.id.privacy:
                    Intent tntent = new Intent(SettingActivity.this, PraivacyActivity.class);
                    tntent.putExtra("check", true);
                    startActivity(tntent);
                    finish();
                    // クリック処理
                    break;
                case R.id.action_list:
                    lntent = new Intent(SettingActivity.this, ItemActivity.class);
                    lntent.putExtra("check", true);
                    startActivity(lntent);
                    finish();
                    break;
                case R.id.action_chat:
                    dntent = new Intent(SettingActivity.this, ChatActivity.class);
                    dntent.putExtra("check", true);
                    startActivity(dntent);
                    finish();
                    break;
                case R.id.action_logout:
                    mAuth = FirebaseAuth.getInstance();
                    mAuth.signOut();
                    tntent = new Intent(SettingActivity.this, MainActivity.class);
                    tntent.putExtra("check", true);
                    startActivity(tntent);
                    finish();
                    break;
            }
        }
    }
}




