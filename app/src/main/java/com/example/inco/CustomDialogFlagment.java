package com.example.inco;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.widget.DatePicker;
import androidx.fragment.app.DialogFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.Calendar;
public class CustomDialogFlagment extends DialogFragment {
    static String dateStr;
    // ダイアログが生成された時に呼ばれるメソッド ※必須
    public Dialog onCreateDialog(Bundle savedInstanceState){
        // 今日の日付のカレンダーインスタンスを取得
        final Calendar calendar = Calendar.getInstance();
        // ダイアログ生成  DatePickerDialogのBuilderクラスを指定してインスタンス化します
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference reference = database.getReference();
        DatePickerDialog dateBuilder = new DatePickerDialog(
                getActivity(),
                new DatePickerDialog.OnDateSetListener(){
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        // 選択された年・月・日を整形 ※月は0-11なので+1している
                        dateStr = year + "年" + (month + 1) + "月" + dayOfMonth + "日";
                        // MainActivityのインスタンスを取得
                        AddActivity addActivity = (AddActivity) getActivity();
                        addActivity.setTextView(dateStr);
                        //FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                        //AddActivity.uid = user.getUid();
                        //AddActivity.key = reference.push().getKey();
                        //reference.child("users").child(AddActivity.uid).child(AddActivity.key).setValue(dateStr);
                    }
                },
                calendar.get(Calendar.YEAR), // 初期選択年
                calendar.get(Calendar.MONTH), // 初期選択月
                calendar.get(Calendar.DAY_OF_MONTH)// 初期選択日
                //reference.child("users").child(AddActivity.uid).child(AddActivity.key).setValue(dateStr);
        );
        // dateBulderを返す
        return dateBuilder;
    }
}