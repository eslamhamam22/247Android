package amaz.objects.TwentyfourSeven.viewholders;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import amaz.objects.TwentyfourSeven.MApplication;
import amaz.objects.TwentyfourSeven.R;
import amaz.objects.TwentyfourSeven.data.models.BankAccount;
import amaz.objects.TwentyfourSeven.utilities.Fonts;


public class BankTransferViewHolder extends RecyclerView.ViewHolder {

    private TextView tv_bank_name, tv_accountNum;
    private Fonts fonts;
    private ImageView transactionTypeIV;
    private Context context;

    public BankTransferViewHolder(View itemView, Context context) {
        super(itemView);
        this.context = context;
        initializeViews();
        setFonts();
    }

    private void initializeViews() {
        tv_bank_name = itemView.findViewById(R.id.tv_bank_name);
        tv_accountNum = itemView.findViewById(R.id.tv_accountNum);
    }

    private void setFonts() {
        fonts = MApplication.getInstance().getFonts();
        tv_bank_name.setTypeface(fonts.customFont());
        tv_accountNum.setTypeface(fonts.customFont());
    }

    public void setData(BankAccount bankAccount) {
        tv_accountNum.setText(context.getResources().getString(R.string.account_no) + " " + bankAccount.getAccount_number());
        tv_bank_name.setText(bankAccount.getBank_name());
    }


}
