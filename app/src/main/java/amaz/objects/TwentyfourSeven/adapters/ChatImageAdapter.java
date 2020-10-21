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
import amaz.objects.TwentyfourSeven.data.models.ImageChat;
import amaz.objects.TwentyfourSeven.listeners.OnImageClickListener;
import amaz.objects.TwentyfourSeven.viewholders.ImageOfGridViewHolder;
import amaz.objects.TwentyfourSeven.viewholders.OrderImageViewHolder;

public class ChatImageAdapter extends RecyclerView.Adapter<ImageOfGridViewHolder> {

    private Context context;
    private ArrayList<ImageChat> images;
    private OnImageClickListener onImageClickListener;

    public ChatImageAdapter(Context context, ArrayList<ImageChat> images,OnImageClickListener onImageClickListener) {
        this.context = context;
        this.images = images;
        this.onImageClickListener = onImageClickListener;
    }

    @NonNull
    @Override
    public ImageOfGridViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_image_of_grid_chat, viewGroup, false);
        ImageOfGridViewHolder vh = new ImageOfGridViewHolder(view,onImageClickListener);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ImageOfGridViewHolder orderImageViewHolder, int i) {
       orderImageViewHolder.setData(images.get(i));
    }

    @Override
    public int getItemCount() {

            return images.size();

    }
}
