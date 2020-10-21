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
import amaz.objects.TwentyfourSeven.data.models.ImageSizes;
import amaz.objects.TwentyfourSeven.listeners.OnDeleteImageClickListener;
import amaz.objects.TwentyfourSeven.viewholders.RequestImageViewHolder;

public class RequestImageAdapter extends RecyclerView.Adapter<RequestImageViewHolder> {

    private Context context;
    private ArrayList<DelegateImageData> requestImages;
    private OnDeleteImageClickListener listener;
    private boolean isMax;

    public RequestImageAdapter(Context context, ArrayList<DelegateImageData> requestImages, OnDeleteImageClickListener listener){
        this.context = context;
        this.requestImages = requestImages;
        this.listener = listener;
    }

    @NonNull
    @Override
    public RequestImageViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_request_image, viewGroup, false);
        RequestImageViewHolder vh = new RequestImageViewHolder(view, listener);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull RequestImageViewHolder requestImageViewHolder, int i) {
        if (i == 0 && requestImages.size() == 0){
            requestImageViewHolder.setData(null);
        }
        else {
            requestImageViewHolder.setData(requestImages.get(i));
        }

    }

    @Override
    public int getItemCount() {
        if (requestImages.size() == 0){
            return 1;
        }
        else {
            return requestImages.size();
        }

    }

}
