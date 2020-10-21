package amaz.objects.TwentyfourSeven.adapters;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import amaz.objects.TwentyfourSeven.R;
import amaz.objects.TwentyfourSeven.data.models.Complaint;
import amaz.objects.TwentyfourSeven.viewholders.ComplaintsViewHolder;

public class ComplaintsAdapter extends RecyclerView.Adapter<ComplaintsViewHolder> {
    private Context context;
    private ArrayList<Complaint> complaints;

    public ComplaintsAdapter(Context context, ArrayList<Complaint> complaints) {
        this.context = context;
        this.complaints = complaints;
    }

    @NonNull
    @Override
    public ComplaintsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_complaint,viewGroup,false);
        ComplaintsViewHolder vh = new ComplaintsViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ComplaintsViewHolder reviewsViewHolder, int i) {
        reviewsViewHolder.setData(complaints.get(i));
    }

    @Override
    public int getItemCount() {
        return complaints.size();
    }
}
