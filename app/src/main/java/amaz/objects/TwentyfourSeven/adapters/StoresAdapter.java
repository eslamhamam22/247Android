package amaz.objects.TwentyfourSeven.adapters;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import amaz.objects.TwentyfourSeven.R;
import amaz.objects.TwentyfourSeven.data.models.Store;
import amaz.objects.TwentyfourSeven.viewholders.StoresViewHolder;

import java.util.ArrayList;

public class StoresAdapter extends RecyclerView.Adapter<StoresViewHolder> {

    private Context context;
    private ArrayList<Store> stores;
    private String categoryImage;
    private double currentLatitude;
    private double currentLongitude;


    public StoresAdapter(Context context, ArrayList<Store> stores, String categoryImage){
        this.context = context;
        this.stores = stores;
        this.categoryImage = categoryImage;
    }

    @NonNull
    @Override
    public StoresViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_store, viewGroup, false);
        StoresViewHolder vh = new StoresViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull StoresViewHolder storesViewHolder, int i) {
        storesViewHolder.setData(stores.get(i), categoryImage, currentLatitude, currentLongitude);
    }

    @Override
    public int getItemCount() {
        return stores.size();
    }

    public void setCurrentLatitude(double currentLatitude) {
        this.currentLatitude = currentLatitude;
    }

    public void setCurrentLongitude(double currentLongitude) {
        this.currentLongitude = currentLongitude;
    }
}
