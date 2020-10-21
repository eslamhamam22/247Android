package amaz.objects.TwentyfourSeven.adapters;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import amaz.objects.TwentyfourSeven.R;
import amaz.objects.TwentyfourSeven.data.models.DelegateImageData;
import amaz.objects.TwentyfourSeven.listeners.OnImageClickListener;
import amaz.objects.TwentyfourSeven.viewholders.OrderImageViewHolder;

public class OrderImagesAdapter extends RecyclerView.Adapter<OrderImageViewHolder> {

    private Context context;
    private ArrayList<DelegateImageData> orderImages;
    private OnImageClickListener listener;

    public OrderImagesAdapter(Context context, ArrayList<DelegateImageData> orderImages, OnImageClickListener listener) {
        this.context = context;
        this.orderImages = orderImages;
        this.listener = listener;
    }

    @NonNull
    @Override
    public OrderImageViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_order_image, viewGroup, false);
        OrderImageViewHolder vh = new OrderImageViewHolder(view, listener);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull OrderImageViewHolder orderImageViewHolder, int i) {
        orderImageViewHolder.setData(orderImages.get(i));
    }

    @Override
    public int getItemCount() {
        return orderImages.size();
    }
}
