package amaz.objects.TwentyfourSeven.adapters;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import amaz.objects.TwentyfourSeven.R;
import amaz.objects.TwentyfourSeven.data.models.Notification;
import amaz.objects.TwentyfourSeven.listeners.OnNotificationClickListnner;
import amaz.objects.TwentyfourSeven.viewholders.NotificationsViewHolder;


/**
 * Created by objects on 17/09/17.
 */

public class NotificationsAdapter extends RecyclerView.Adapter<NotificationsViewHolder> {
    private LayoutInflater inflater;
    private Context context;
    private boolean isLoading;
    private boolean isFirstTime = true;
    private ArrayList<Notification> notifications;
    private boolean moreDataAvailable = true;
    private OnNotificationClickListnner onNotificationClickListnner;

    public NotificationsAdapter(Context context, ArrayList<Notification> notifications, OnNotificationClickListnner onNotificationClickListnner){
        this.context = context;
        this.notifications = notifications;
        this.onNotificationClickListnner = onNotificationClickListnner;

    }
    @Override
    public NotificationsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.item_notification,parent,false);
        NotificationsViewHolder vh = new NotificationsViewHolder(v,context,onNotificationClickListnner);
        return vh;
    }

    @Override
    public void onBindViewHolder(NotificationsViewHolder holder, int position) {
        holder.setData(this.notifications.get(position),position);

    }

    @Override
    public int getItemCount() {
        return notifications.size();
    }

}
