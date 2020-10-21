package amaz.objects.TwentyfourSeven.viewholders;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import amaz.objects.TwentyfourSeven.R;
import amaz.objects.TwentyfourSeven.api.APIURLs;
import amaz.objects.TwentyfourSeven.data.models.DelegateImageData;
import amaz.objects.TwentyfourSeven.data.models.ImageChat;
import amaz.objects.TwentyfourSeven.data.models.ImageSizes;
import amaz.objects.TwentyfourSeven.listeners.OnImageClickListener;

public class ImageOfGridViewHolder extends RecyclerView.ViewHolder  {

    private ImageView chatImage;
    private OnImageClickListener listener;
    private ImageChat imageData;
    private OnImageClickListener onImageClickListener;
// upda
    public ImageOfGridViewHolder(@NonNull View itemView,OnImageClickListener onImageClickListener) {
        super(itemView);
        this.listener = listener;
        this.onImageClickListener = onImageClickListener ;
        initializeViews();
    }

    private void initializeViews(){
        chatImage = itemView.findViewById(R.id.image_chat_iv);
        chatImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImageSizes imageSizes = new ImageSizes();
                imageSizes.setBig(imageData.url);
                onImageClickListener.onImageClick(imageSizes);
            }
        });
    }

    public void setData(ImageChat imageData){
        this.imageData = imageData;
        Picasso.with(itemView.getContext()).load(imageData.url).placeholder(R.drawable.louding_img).into(chatImage);
    }


}
