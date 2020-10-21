package amaz.objects.TwentyfourSeven.viewholders;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;

import amaz.objects.TwentyfourSeven.R;
import amaz.objects.TwentyfourSeven.data.models.DelegateImageData;
import amaz.objects.TwentyfourSeven.listeners.OnImageClickListener;

public class OrderImageViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    private ImageView orderImage;
    private OnImageClickListener listener;
    private DelegateImageData imageData;

    public OrderImageViewHolder(@NonNull View itemView, OnImageClickListener listener) {
        super(itemView);
        this.listener = listener;
        initializeViews();
    }

    private void initializeViews(){
        orderImage = itemView.findViewById(R.id.iv_order_image);
        orderImage.setOnClickListener(this);
    }

    public void setData(DelegateImageData imageData){
        this.imageData = imageData;
        Picasso.with(itemView.getContext()).load(imageData.getImage().getSmall()).placeholder(R.drawable.default_image).into(orderImage);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.iv_order_image){
            listener.onImageClick(this.imageData.getImage());
        }
    }
}
