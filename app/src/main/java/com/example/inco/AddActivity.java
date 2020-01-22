package com.example.inco;
import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.media.MediaScannerConnection;
import android.media.tv.TvContract;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AddActivity extends AppCompatActivity {
    static final int REQUEST_CODE_CAMERA = 1; /* カメラを判定するコード */
    static final int REQUEST_CODE_GALLERY = 2; /* ギャラリーを判定するコード */
    private static final int RESULT_PICK_IMAGEFILE = 1000;
    private static final int REQUEST_IMAGE_CAPTURE = 2000;


    private Bitmap bitmap;
    private Uri bitmapUri;
    private File bitmapFile;
    private ImageView image_view;
    private Date data;





    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference reference = database.getReference();
    EditText titleEditText, contentEditText;
    ImageView imageview;
    Uri uri = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);


        image_view = (ImageView)findViewById(R.id.image_view);
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

    public void Picture(View v) {
        //選択項目を準備する。
        String[] str_items = {"カメラで撮影", "ギャラリーの選択", "キャンセル"};

        new AlertDialog.Builder(this)
                .setTitle("写真をアップロード")
                .setItems(str_items, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which) {
                                    case 0:
                                        wakeupCamera(); // カメラ起動
                                        break;
                                    case 1:
                                        wakeupGallery(); // ギャラリー起動
                                        break;
                                    default:
                                        // カメラ・ギャラリー以外(キャンセル)を想定
                                        break;
                                }
                            }
                        }
                ).show();
    }

    protected void wakeupCamera() {
        int permissionCheck = ContextCompat.checkSelfPermission(AddActivity.this, Manifest.permission.CAMERA);

        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            // Android 6.0 のみ、該当パーミッションが許可されていない場合
            final int REQUEST_CODE = 1;
            ActivityCompat.requestPermissions(AddActivity.this, new String[]{
                    Manifest.permission.CAMERA
            }, REQUEST_CODE);

        }
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);

    }

    protected void wakeupGallery() {
        final int REQUEST_CODE = 2;
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*");
        startActivityForResult(intent, RESULT_PICK_IMAGEFILE);
    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode ){
            case REQUEST_IMAGE_CAPTURE:
                if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
                    Bitmap bitmap;
                    ImageView imageView = findViewById(R.id.image_view);

                    if( data.getExtras() != null){
                        bitmap = (Bitmap) data.getExtras().get("data");
                        if(bitmap != null){
                            imageView.setImageBitmap(bitmap);
                        }
                        break;
                    }



                }
            case RESULT_PICK_IMAGEFILE:
                if (requestCode == RESULT_PICK_IMAGEFILE && resultCode == RESULT_OK) {
                    Uri uri = null;
                    if (data != null) {
                        uri = data.getData();

                        try {
                            Bitmap bmp = getBitmapFromUri(uri);
                            image_view.setImageBitmap(bmp);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }break;
                    }
                }
            }
        }





    private Bitmap getBitmapFromUri(Uri uri) throws IOException {
        ParcelFileDescriptor parcelFileDescriptor =
                getContentResolver().openFileDescriptor(uri, "r");
        FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
        Bitmap image = BitmapFactory.decodeFileDescriptor(fileDescriptor);
        parcelFileDescriptor.close();
        return image;
    }


    public void setTextView(String value) {
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
        ItemData itemData = new ItemData(key, title, content, CustomDialogFlagment.dateStr);
        reference.child("users").child(uid).child("items").child(key).setValue(itemData).addOnSuccessListener(new OnSuccessListener<Void>() {

            @Override
            public void onSuccess(Void v) {
                finish();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
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



