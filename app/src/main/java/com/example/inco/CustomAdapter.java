package com.example.inco;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import androidx.annotation.NonNull;
import com.google.firebase.database.annotations.Nullable;
import java.util.ArrayList;
import java.util.List;
public class CustomAdapter extends ArrayAdapter<ItemData> {
    private List<ItemData> mCards;
    public CustomAdapter(Context context, int layoutResourceId, ArrayList<ItemData> itemData) {
        super(context, layoutResourceId, itemData);
        this.mCards = itemData;
    }
    @Override
    public int getCount() {
        return mCards.size();
    }
    @Nullable
    @Override
    public ItemData getItem(int position) {
        return mCards.get(position);
    }
    @NonNull
    @Override
    public android.view.View getView(final int position, @Nullable View convertView, @NonNull final ViewGroup parent) {
        final ViewHolder viewHolder;
        if (convertView != null) {
            viewHolder = (ViewHolder) convertView.getTag();
        } else {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.card_view, null);
            viewHolder = new ViewHolder();
            viewHolder.titleTextView = (TextView) ((android.view.View) convertView).findViewById(R.id.title_text_view);
            viewHolder.contentTextView = (TextView) ((android.view.View) convertView).findViewById(R.id.content_text_view);
            viewHolder.dateStrTextView = (TextView) ((android.view.View) convertView).findViewById(R.id.dateStr_text_view);
            ((android.view.View) convertView).setTag(viewHolder);
        }
        ItemData itemData = mCards.get(position);
        viewHolder.titleTextView.setText(itemData.getTitle());
        viewHolder.contentTextView.setText(itemData.getContent());
        viewHolder.dateStrTextView.setText(itemData.getDateStr());
        return convertView;
    }
    public ItemData getItemDataKey(String key) {
        for (ItemData itemData : mCards) {
            if (itemData.getFirebaseKey().equals(key)) {
                return itemData;
            }
        }
        return null;
    }
    static class ViewHolder {
        TextView titleTextView;
        TextView contentTextView;
        TextView dateStrTextView;
    }
}