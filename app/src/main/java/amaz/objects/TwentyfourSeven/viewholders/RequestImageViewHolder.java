package amaz.objects.TwentyfourSeven.viewholders;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.squareup.picasso.Picasso;

import amaz.objects.TwentyfourSeven.R;
import amaz.objects.TwentyfourSeven.data.models.DelegateImageData;
import amaz.objects.TwentyfourSeven.data.models.ImageSizes;
import amaz.objects.TwentyfourSeven.listeners.OnDeleteImageClickListener;

public class RequestImageViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    private OnDeleteImageClickListener listener;
    private ImageView uploadedImageIv, deleteImageIv;

    private DelegateImageData imageData;

    public RequestImageViewHolder(@NonNull View itemView, OnDeleteImageClickListener listener) {
        super(itemView);
        this.listener = listener;
        initializeViews();
    }

    private void initializeViews(){
        uploadedImageIv = itemView.findViewById(R.id.iv_uploaded_image);
        deleteImageIv = itemView.findViewById(R.id.iv_delete);
        deleteImageIv.setOnClickListener(this);

    }

    public void setData(DelegateImageData imageData){

        if (imageData != null){
            this.imageData = imageData;
            Glide.with(itemView.getContext()).load(imageData.getImage().getMedium())
                    .apply(new RequestOptions()
                            .placeholder(R.drawable.default_image)
                            .dontAnimate())
                    .into(uploadedImageIv);
                    //.placeholder(R.drawable.default_image).dontAnimate().into(uploadedImageIv);
            deleteImageIv.setVisibility(View.VISIBLE);
        }
        else {
            uploadedImageIv.setImageResource(R.drawable.default_image);
            deleteImageIv.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.iv_delete){
            listener.onDeleteImageClick(imageData);
        }
    }
}
