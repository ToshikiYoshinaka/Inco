package com.example.inco;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
public class AddActivity extends AppCompatActivity {
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference reference = database.getReference();
    EditText titleEditText, contentEditText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        titleEditText = (EditText) findViewById(R.id.title);
        contentEditText = (EditText) findViewById(R.id.content);
        // idがdialogButtonのButtonを取得
        Button dialogBtn = (Button) findViewById(R.id.dialogButton);
        // clickイベント追加
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        dialogBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            // クリックしたらダイアログを表示する処理
            public void onClick(View v) {
                // ダイアログクラスをインスタンス化
                CustomDialogFlagment dialog = new CustomDialogFlagment();
                // 表示  getFagmentManager()は固定、sampleは識別タグ
                dialog.show(getSupportFragmentManager(), "sample");
            }
        });
        //
    }
    public void setTextView(String value){
        TextView textView = (TextView) findViewById(R.id.text);
        textView.setText(value);
    }
    //static String key;
    //static String uid;
    public void save(View v) {
        String title = titleEditText.getText().toString();
        String content = contentEditText.getText().toString();
        String key = reference.push().getKey();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String uid = user.getUid();
//    引数のToDoDataの内容をデータベースに送る。
        ItemData itemData = new ItemData(key, title, content,CustomDialogFlagment.dateStr);
        reference.child("users").child(uid).child("items").child(key).setValue(itemData).addOnSuccessListener(new OnSuccessListener<Void>() {

            @Override
            public void onSuccess(Void v) {
                finish();
            }
        });
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(AddActivity.this, ItemActivity.class);
                intent.putExtra("check", true);
                startActivity(intent);
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}