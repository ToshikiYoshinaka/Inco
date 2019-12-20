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

public class SettingActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    public FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);


        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


    }

    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.action_list) {
            Intent intent = new Intent(SettingActivity.this, ItemActivity.class);
            intent.putExtra("check", true);
            startActivity(intent);
            finish();
            // Handle the camera action
        } else if (id == R.id.action_chat) {
            Intent lntent = new Intent(SettingActivity.this, ChatActivity.class);
            lntent.putExtra("check", true);
            startActivity(lntent);
            finish();
        } else if (id == R.id.navigation_notifications) {
            Intent dntent = new Intent(SettingActivity.this, SettingActivity.class);
            dntent.putExtra("check", true);
            startActivity(dntent);
            finish();
        } else if (id == R.id.action_logout) {
            mAuth = FirebaseAuth.getInstance();
            mAuth.signOut();
            Intent tntent = new Intent(SettingActivity.this, MainActivity.class);
            tntent.putExtra("check", true);
            startActivity(tntent);
            finish();
        }
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;


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
            }
        }
    }
}


