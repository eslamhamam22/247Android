package amaz.objects.TwentyfourSeven.adapters;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import amaz.objects.TwentyfourSeven.R;
import amaz.objects.TwentyfourSeven.viewholders.StoreWorkingHouresViewHolder;

public class StoreWorkingHouresAdapter extends RecyclerView.Adapter<StoreWorkingHouresViewHolder> {

    private Context context;
    private ArrayList<String> workingHouresList;

    public StoreWorkingHouresAdapter(Context context, ArrayList<String> workingHouresList) {
        this.context = context;
        this.workingHouresList = workingHouresList;
    }

    @NonNull
    @Override
    public StoreWorkingHouresViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_working_houres, viewGroup, false);
        StoreWorkingHouresViewHolder vh = new StoreWorkingHouresViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull StoreWorkingHouresViewHolder storeWorkingHouresViewHolder, int i) {
        storeWorkingHouresViewHolder.setData(workingHouresList.get(i));
    }

    @Override
    public int getItemCount() {
        return workingHouresList.size();
    }
}
