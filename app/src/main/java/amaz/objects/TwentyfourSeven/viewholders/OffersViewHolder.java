package amaz.objects.TwentyfourSeven.viewholders;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.Locale;

import amaz.objects.TwentyfourSeven.MApplication;
import amaz.objects.TwentyfourSeven.R;
import amaz.objects.TwentyfourSeven.data.models.Offer;
import amaz.objects.TwentyfourSeven.listeners.OnOfferClickListener;
import amaz.objects.TwentyfourSeven.utilities.Fonts;
import amaz.objects.TwentyfourSeven.utilities.LocalSettings;

public class OffersViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    /*private TextView delegateNameTv, totalTv, addressTv, distanceTv, acceptTv;
    private ImageView delegateImageIv;
    private RatingBar delegateRateRb;
    private Fonts fonts;

    private OnOfferClickListener listener;
    private Offer offer;

    public OffersViewHolder(@NonNull View itemView, OnOfferClickListener listener) {
        super(itemView);
        this.listener = listener;
        initializeViews();
        setFonts();
    }

    private void initializeViews(){
        delegateNameTv = itemView.findViewById(R.id.tv_delegate_name);
        delegateNameTv.setOnClickListener(this);
        totalTv = itemView.findViewById(R.id.tv_total);
        addressTv = itemView.findViewById(R.id.tv_address);
        distanceTv = itemView.findViewById(R.id.tv_distance);
        acceptTv = itemView.findViewById(R.id.tv_accept);
        acceptTv.setOnClickListener(this);
        delegateImageIv = itemView.findViewById(R.id.iv_delegate_image);
        delegateImageIv.setOnClickListener(this);
        delegateRateRb = itemView.findViewById(R.id.rb_delegate_rate);
    }

    private void setFonts(){
        fonts = MApplication.getInstance().getFonts();
        delegateNameTv.setTypeface(fonts.customFont());
        totalTv.setTypeface(fonts.customFontBD());
        addressTv.setTypeface(fonts.customFont());
        distanceTv.setTypeface(fonts.customFont());
        acceptTv.setTypeface(fonts.customFont());
    }

    public void setData(Offer offer){
        this.offer = offer;
        delegateNameTv.setText(offer.getDelegate().getName());
        if (offer.getDelegate().getDelegateRating() == 0){
            delegateRateRb.setRating(5);
        }
        else {
            delegateRateRb.setRating(offer.getDelegate().getDelegateRating());
        }

        totalTv.setText(String.format("%.2f", (offer.getShippingCost()+offer.getVat()))+" "+itemView.getContext().getResources().getString(R.string.sar));
        addressTv.setText(offer.getDelegateAddress());
        if (offer.getDistanceToPickup()<1){
            distanceTv.setText(String.format("%.1f",(offer.getDistanceToPickup()*1000))+" "+itemView.getContext().getResources().getString(R.string.meter));
        }
        else {
            distanceTv.setText(String.format("%.1f",(offer.getDistanceToPickup()))+" "+itemView.getContext().getResources().getString(R.string.kilometer));
        }
        if (offer.getDelegate().getImage() != null){
            Picasso.with(itemView.getContext()).load(offer.getDelegate().getImage().getMedium()).placeholder(R.drawable.avatar)
                    .error(R.drawable.avatar).into(delegateImageIv);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tv_accept:
                listener.onAcceptOfferClick(offer);
                break;

            case R.id.tv_delegate_name:
                listener.onDelegateClick(offer.getDelegate().getId());
                break;

            case R.id.iv_delegate_image:
                listener.onDelegateClick(offer.getDelegate().getId());
                break;
        }

    }*/

    private TextView delegateNameTv, deliveryDurationTv, timeTv, shippingCostTv, priceTv, distanceTv, awayTv, acceptTv, rejectTv;
    private ImageView delegateImageIv;
    private RatingBar delegateRateRb;
    private Fonts fonts;

    private OnOfferClickListener listener;
    private Offer offer;
    private LocalSettings localSettings;

    public OffersViewHolder(@NonNull View itemView, OnOfferClickListener listener) {
        super(itemView);
        this.listener = listener;
        localSettings = new LocalSettings(itemView.getContext());
        initializeViews();
        setFonts();
    }

    private void initializeViews(){
        delegateNameTv = itemView.findViewById(R.id.tv_delegate_name);
        delegateNameTv.setOnClickListener(this);
        deliveryDurationTv = itemView.findViewById(R.id.tv_delivery_duration);
        timeTv = itemView.findViewById(R.id.tv_time);
        shippingCostTv = itemView.findViewById(R.id.tv_shipping_cost);
        priceTv = itemView.findViewById(R.id.tv_price);
        distanceTv = itemView.findViewById(R.id.tv_distance);
        awayTv = itemView.findViewById(R.id.tv_away);
        acceptTv = itemView.findViewById(R.id.tv_accept);
        acceptTv.setOnClickListener(this);
        rejectTv = itemView.findViewById(R.id.tv_reject);
        rejectTv.setOnClickListener(this);
        delegateImageIv = itemView.findViewById(R.id.iv_delegate_image);
        delegateImageIv.setOnClickListener(this);
        delegateRateRb = itemView.findViewById(R.id.rb_delegate_rate);
    }

    private void setFonts(){
        fonts = MApplication.getInstance().getFonts();

        delegateNameTv.setTypeface(fonts.customFont());
        deliveryDurationTv.setTypeface(fonts.customFontBD());
        timeTv.setTypeface(fonts.customFont());
        shippingCostTv.setTypeface(fonts.customFontBD());
        priceTv.setTypeface(fonts.customFont());
        distanceTv.setTypeface(fonts.customFontBD());
        awayTv.setTypeface(fonts.customFont());
        acceptTv.setTypeface(fonts.customFont());
        rejectTv.setTypeface(fonts.customFont());
    }

    public void setData(Offer offer, int deliveryDuration){
        this.offer = offer;
        delegateNameTv.setText(offer.getDelegate().getName());
        if (offer.getDelegate().getDelegateRating() == 0){
            delegateRateRb.setRating(5);
        }
        else {
            delegateRateRb.setRating(offer.getDelegate().getDelegateRating());
        }

        shippingCostTv.setText(String.format(Locale.ENGLISH,"%.2f", (offer.getShippingCost()))+" "+itemView.getContext().getResources().getString(R.string.sar));
        deliveryDurationTv.setText(String.format(Locale.ENGLISH,"%02d", deliveryDuration)+" "+itemView.getContext().getResources().getString(R.string.houre));
        if (offer.getDistanceToPickup()<1){
            distanceTv.setText(String.format(Locale.ENGLISH,"%.1f",(offer.getDistanceToPickup()*1000))+" "+itemView.getContext().getResources().getString(R.string.meter));
        }
        else {
            distanceTv.setText(String.format(Locale.ENGLISH,"%.1f",(offer.getDistanceToPickup()))+" "+itemView.getContext().getResources().getString(R.string.kilometer));
        }
        if (offer.getDelegate().getImage() != null){
            Picasso.with(itemView.getContext()).load(offer.getDelegate().getImage().getMedium()).placeholder(R.drawable.avatar)
                    .error(R.drawable.avatar).into(delegateImageIv);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tv_accept:
                listener.onAcceptOfferClick(offer);
                break;

            case R.id.tv_reject:
                listener.onRejectOfferClick(offer);
                break;

            case R.id.tv_delegate_name:
                listener.onDelegateClick(offer.getDelegate().getId());
                break;

            case R.id.iv_delegate_image:
                listener.onDelegateClick(offer.getDelegate().getId());
                break;
        }

    }
}
