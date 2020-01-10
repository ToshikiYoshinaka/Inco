package com.example.inco;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.ActionProvider;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.Navigation;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.annotations.Nullable;

import java.util.ArrayList;

public class ItemActivity extends AppCompatActivity implements ListView.OnItemLongClickListener, OnClickListener {
    public FirebaseUser user;
    public String uid;
    public FirebaseAuth mAuth;
    public FirebaseDatabase database;
    public DatabaseReference reference;
    public CustomAdapter mCustomAdapter;
    public ListView mListView;
    public Intent lntent;
    public Intent dntent;
    public Intent tntent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item);


        //ログイン情報を取得
        user = FirebaseAuth.getInstance().getCurrentUser();
        //user id = Uid を取得する
        uid = user.getUid();
        database = FirebaseDatabase.getInstance();
        reference = database.getReference("users").child(uid).child("items");
        mListView = (ListView) findViewById(R.id.list_view);
        //CustomAdapterをセット
        mCustomAdapter = new CustomAdapter(getApplicationContext(), R.layout.card_view, new ArrayList<ItemData>());
        mListView.setAdapter(mCustomAdapter);
        //LongListenerを設定
        mListView.setOnItemLongClickListener(this);
        findViewById(R.id.action_list).setOnClickListener(this);
        findViewById(R.id.action_chat).setOnClickListener(this);
        findViewById(R.id.action_setting).setOnClickListener(this);
        findViewById(R.id.action_logout).setOnClickListener(this);
        //firebaseと同期するリスナー
        reference.addChildEventListener(new ChildEventListener() {
            //            データを読み込むときはイベントリスナーを登録して行う。


            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
//                アイテムのリストを取得するか、アイテムのリストへの追加がないかリッスンします。
                ItemData itemData = dataSnapshot.getValue(ItemData.class);
                mCustomAdapter.add(itemData);
                mCustomAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
//                リスト内のアイテムに対する変更がないかリッスンします。
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
//                リストから削除されるアイテムがないかリッスンします。
                Log.d("ItemActivity", "onChildRemoved:" + dataSnapshot.getKey());
                ItemData result = dataSnapshot.getValue(ItemData.class);
                if (result == null) return;
                ItemData item = mCustomAdapter.getItemDataKey(result.getFirebaseKey());
                mCustomAdapter.remove(item);
                mCustomAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
//                並べ替えリストの項目順に変更がないかリッスンします。
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
//                ログを記録するなどError時の処理を記載する。
            }
        });
    }


    @Override
    public void onClick(View v) {
//                Intent intent = new Intent(ItemActivity.this, ItemActivity.class);
//                intent.putExtra("check", true);
//                startActivity(intent);
//                finish();
        switch (v.getId()) {
            case R.id.action_chat:
                lntent = new Intent(ItemActivity.this, ChatActivity.class);
                lntent.putExtra("check", true);
                startActivity(lntent);
                finish();
                break;
            case R.id.action_setting:
                dntent = new Intent(ItemActivity.this, SettingActivity.class);
                dntent.putExtra("check", true);
                startActivity(dntent);
                finish();
                break;
            case R.id.action_logout:
                mAuth = FirebaseAuth.getInstance();
                mAuth.signOut();
                tntent = new Intent(ItemActivity.this, MainActivity.class);
                tntent.putExtra("check", true);
                startActivity(tntent);
                finish();
                break;

        }

    }


    public void addButton(View v) {
        Intent intent = new Intent(this, AddActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
        final ItemData itemData = mCustomAdapter.getItem(position);
        uid = user.getUid();
        new AlertDialog.Builder(this)
                .setMessage("この商品を削除しますか？")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // OK button pressed
                        reference.child(itemData.getFirebaseKey()).removeValue();
//                        mCustomAdapter.remove(itemData);
                    }
                })
                .setNegativeButton("No", null)
                .show();
        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
//main.xmlの内容を読み込む
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.bottom_nav_menu, menu);
        return true;


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.side_chat:
                Intent intent = new Intent(ItemActivity.this, ChatActivity.class);
                intent.putExtra("check", true);
                startActivity(intent);
                finish();
                // User chose the "Settings" item, show the app settings UI...
               return true;
            case R.id.side_list:
                Intent lntent = new Intent(ItemActivity.this, ItemActivity.class);
                lntent.putExtra("check", true);
                startActivity(lntent);
                finish();
                // User chose the "Favorite" action, mark the current item
                // as a favorite...
                return true;
            case R.id.side_setting:
                return true;
            case R.id.side_logout:
                mAuth = FirebaseAuth.getInstance();
                mAuth.signOut();
                Intent tntent = new Intent(ItemActivity.this, MainActivity.class);
                tntent.putExtra("check", true);
                startActivity(tntent);
                finish();
                return true;
            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);
        }
    }
}