package amaz.objects.TwentyfourSeven.adapters;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import amaz.objects.TwentyfourSeven.R;
import amaz.objects.TwentyfourSeven.data.models.UserTransaction;
import amaz.objects.TwentyfourSeven.viewholders.TransactionViewHolder;


/**
 * Created by objects on 17/09/17.
 */

public class BalanceAdapter extends RecyclerView.Adapter<TransactionViewHolder> {
    private LayoutInflater inflater;
    private Context context;
    private ArrayList<UserTransaction> userTransactions;

    public BalanceAdapter(Context context, ArrayList<UserTransaction> userTransactions){
        this.context = context;
        this.userTransactions = userTransactions;

    }

    @Override
    public TransactionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.item_my_transaction,parent,false);
        TransactionViewHolder vh = new TransactionViewHolder(v,context);
        return vh;
    }

    @Override
    public void onBindViewHolder(TransactionViewHolder holder, int position) {
        holder.setData(this.userTransactions.get(position));

    }

    @Override
    public int getItemCount() {
        return userTransactions.size();
    }

}
