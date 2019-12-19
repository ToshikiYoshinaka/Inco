package com.example.inco;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {
    EditText emailFormEditText, passwordFormEditText;
    public Intent data;
    public FirebaseAuth mAuth;
    private static final String TAG = "EmailPassword";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //onCreate(画面が起動された時に最初に呼ばれるメソッド)内にFirebaseAuthオブジェクトの共有インスタンスを取得します。
        super.onCreate(savedInstanceState);//親クラスのメソッドを呼び出す
        setContentView(R.layout.activity_main);//activity_main.xmlの画面レイアウトを読み込む

        emailFormEditText = (EditText) findViewById(R.id.email_log_in_edit_text);
        passwordFormEditText = (EditText) findViewById(R.id.password_log_in_edit_text);

        mAuth = FirebaseAuth.getInstance();
    }

    public boolean checkEmpty() {
        if (TextUtils.isEmpty(emailFormEditText.getText())) {
            //Eメールが入力されていないときfalseを返す。
            Log.d("MainActivity", "何も記入されていません");
            return false;
        }

        if (TextUtils.isEmpty(passwordFormEditText.getText())) {
            //パスワードが入力されていないときfalseを返す。
            Log.d("MainActivity", "何も記入されていません");
            return false;
        }
        return true;
    }

    public void loginMailButton(View v) {
        signIn(emailFormEditText.getText().toString(), passwordFormEditText.getText().toString());
        setResult(RESULT_OK, data);
    }

    public void addMailButton(View v) {
        createAccount(emailFormEditText.getText().toString(), passwordFormEditText.getText().toString());
        setResult(RESULT_OK, data);
    }

    private void createAccount(String email, String password) {
        Log.d(TAG, "createAccount:" + email);

        if (!checkEmpty()) {
            return;
        }

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // ログインに成功したら、ログインしたユーザーの情報でUIを更新します。
                            Log.d(TAG, "createUserWithEmail:success");
                            Toast.makeText(MainActivity.this, "新規作成に成功しました！", Toast.LENGTH_SHORT).show();
                            changeActivity();
                        } else {
                            // サインインに失敗した場合は、ユーザーにメッセージを表示します。
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(MainActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }

    private void signIn(String email, String password) {
        Log.d(TAG, "signIn:" + email);

        if (!checkEmpty()) {
            return;
        }

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // ログインに成功したら、ログインしたユーザーの情報でUIを更新します。
                            Log.d(TAG, "signInWithEmail:success");
                            Toast.makeText(MainActivity.this, "ログインに成功しました！", Toast.LENGTH_SHORT).show();
                            changeActivity();
                        } else {
                            // サインインに失敗した場合は、ユーザーにメッセージを表示します。
                            Log.w(TAG, "signInWithEmail:failure", task.getException());

                            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                            builder.setMessage(task.getException().getMessage())
                                    .setTitle("Error!")
                                    .setPositiveButton(android.R.string.ok, null);
                            AlertDialog dialog = builder.create();
                            dialog.show();
                        }

                    }
                });
    }

    private void changeActivity() {
        Intent intent = new Intent(this, ItemActivity.class);
        startActivity(intent);
        finish();
    }

}