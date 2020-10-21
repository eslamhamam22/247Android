package amaz.objects.TwentyfourSeven.adapters;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import java.util.ArrayList;
import amaz.objects.TwentyfourSeven.R;
import amaz.objects.TwentyfourSeven.data.models.BankAccount;
import amaz.objects.TwentyfourSeven.viewholders.BankTransferViewHolder;


/**
 * Created by objects on 17/09/17.
 */

public class BankAccountsAdapter extends RecyclerView.Adapter<BankTransferViewHolder> {
    private LayoutInflater inflater;
    private Context context;
    private ArrayList<BankAccount> bankAccounts;

    public BankAccountsAdapter(Context context, ArrayList<BankAccount> bankAccounts){
        this.context = context;
        this.bankAccounts = bankAccounts;

    }

    @Override
    public BankTransferViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.item_bank_transfer,parent,false);
        BankTransferViewHolder vh = new BankTransferViewHolder(v,context);
        return vh;
    }

    @Override
    public void onBindViewHolder(BankTransferViewHolder holder, int position) {
    holder.setData(this.bankAccounts.get(position));

    }

    @Override
    public int getItemCount() {
        return  bankAccounts.size();
    }

}
