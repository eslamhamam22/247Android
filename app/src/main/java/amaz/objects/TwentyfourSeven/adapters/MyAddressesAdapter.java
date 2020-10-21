package amaz.objects.TwentyfourSeven.adapters;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import amaz.objects.TwentyfourSeven.R;
import amaz.objects.TwentyfourSeven.data.models.Address;
import amaz.objects.TwentyfourSeven.listeners.OnAddressClickListener;
import amaz.objects.TwentyfourSeven.viewholders.MyAddressesViewHolder;

public class MyAddressesAdapter extends RecyclerView.Adapter<MyAddressesViewHolder> {

    private Context context;
    private ArrayList<Address> addresses;
    private OnAddressClickListener listener;
    private boolean fromDestination;

    public MyAddressesAdapter(Context context, ArrayList<Address> addresses, OnAddressClickListener listener, boolean fromDestination){
        this.context = context;
        this.addresses = addresses;
        this.listener = listener;
        this.fromDestination = fromDestination;
    }
    @NonNull
    @Override
    public MyAddressesViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_my_address,viewGroup,false);
        MyAddressesViewHolder vh = new MyAddressesViewHolder(view,listener, fromDestination);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull MyAddressesViewHolder myAddressesViewHolder, int i) {
        myAddressesViewHolder.setData(addresses.get(i));
    }

    @Override
    public int getItemCount() {
        return addresses.size();
    }
}
