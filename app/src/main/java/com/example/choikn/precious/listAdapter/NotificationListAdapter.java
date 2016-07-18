package com.example.choikn.precious.listAdapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.choikn.precious.R;
import com.example.choikn.precious.server.Notification;

import java.util.List;

/**
 * Created by yoo2001818 on 16. 7. 18.
 */

public class NotificationListAdapter extends RecyclerView.Adapter implements View.OnClickListener {

    private List<Notification> notifications;
    private int resource;
    private LayoutInflater inflater;
    private RecyclerView view;
    private ItemClickListener listener;

    public NotificationListAdapter(Context context, List<Notification> notifications, RecyclerView view) {
        inflater = LayoutInflater.from(context);
        this.resource = R.layout.notification_entry;
        this.notifications = notifications;
        this.view = view;
    }

    public RecyclerView getView() {
        return view;
    }

    public void setView(RecyclerView view) {
        this.view = view;
    }

    public void setListener(ItemClickListener listener) {
        this.listener = listener;
    }

    public Notification getItem(int position) {
        return notifications.get(position);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(View v) {
            super(v);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = inflater.inflate(resource, viewGroup, false);
        view.setOnClickListener(this);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
        TextView title = (TextView) viewHolder.itemView.findViewById(R.id.title);
        TextView description = (TextView) viewHolder.itemView.findViewById(R.id.description);
        Notification entry = getItem(i);
        title.setText(entry.getTitle());
        description.setText(entry.getMessage());
        Log.e("MyTag", entry.getMessage());

    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return notifications.size();
    }

    public List<Notification> getNotifications() {
        return notifications;
    }

    public void setNotifications(List<Notification> notifications) {
        this.notifications = notifications;
    }

    @Override
    public void onClick(View v) {
        int itemPosition = this.view.getChildLayoutPosition(v);
        Notification item = this.getItem(itemPosition);
        // Clicked!
        if (listener != null) listener.onClick(item);

    }

    public interface ItemClickListener {
        void onClick(Notification item);
    }
}
