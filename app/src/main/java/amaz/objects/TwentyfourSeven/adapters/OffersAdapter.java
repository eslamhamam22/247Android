package amaz.objects.TwentyfourSeven.adapters;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import amaz.objects.TwentyfourSeven.R;
import amaz.objects.TwentyfourSeven.data.models.Offer;
import amaz.objects.TwentyfourSeven.listeners.OnOfferClickListener;
import amaz.objects.TwentyfourSeven.viewholders.OffersViewHolder;

public class OffersAdapter extends RecyclerView.Adapter<OffersViewHolder> {

    private Context context;
    private ArrayList<Offer> offers;
    private int deliveryDuration;
    private OnOfferClickListener listener;

    public OffersAdapter(Context context, ArrayList<Offer> offers, OnOfferClickListener listener) {
        this.context = context;
        this.offers = offers;
        this.listener = listener;
    }

    @NonNull
    @Override
    public OffersViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_offer_2, viewGroup, false);
        OffersViewHolder vh = new OffersViewHolder(view, listener);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull OffersViewHolder offersViewHolder, int i) {
        offersViewHolder.setData(offers.get(i), deliveryDuration);
    }

    @Override
    public int getItemCount() {
        return offers.size();
    }

    public void setDeliveryDuration(int deliveryDuration) {
        this.deliveryDuration = deliveryDuration;
    }
}
