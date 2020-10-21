package amaz.objects.TwentyfourSeven.adapters;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import amaz.objects.TwentyfourSeven.R;
import amaz.objects.TwentyfourSeven.data.models.Notification;
import amaz.objects.TwentyfourSeven.data.models.Order;
import amaz.objects.TwentyfourSeven.listeners.OnNotificationClickListnner;
import amaz.objects.TwentyfourSeven.listeners.OnOrderClickListener;
import amaz.objects.TwentyfourSeven.viewholders.CurrentOrderViewHolder;
import amaz.objects.TwentyfourSeven.viewholders.NotificationsViewHolder;
import amaz.objects.TwentyfourSeven.viewholders.OrderChildViewHolder;


/**
 * Created by objects on 17/09/17.
 */

public class CurrentOrderAdapter extends RecyclerView.Adapter<CurrentOrderViewHolder> {
    private LayoutInflater inflater;
    private Context context;
    private ArrayList<Order> orders;
    private OnOrderClickListener onOrderClickListener;

    public CurrentOrderAdapter(Context context, ArrayList<Order> orders, OnOrderClickListener onOrderClickListener){
        this.context = context;
        this.orders = orders;
        this.onOrderClickListener = onOrderClickListener;

    }
    @Override
    public CurrentOrderViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.item_my_order,parent,false);
        CurrentOrderViewHolder vh = new CurrentOrderViewHolder(v,context,onOrderClickListener);
        return vh;
    }

    @Override
    public void onBindViewHolder(CurrentOrderViewHolder holder, int position) {
        holder.setData(this.orders.get(position));

    }

    @Override
    public int getItemCount() {
        return orders.size();
    }

}
