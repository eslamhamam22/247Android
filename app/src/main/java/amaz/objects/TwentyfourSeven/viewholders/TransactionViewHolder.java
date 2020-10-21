package amaz.objects.TwentyfourSeven.viewholders;

import android.content.Context;

import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Locale;

import amaz.objects.TwentyfourSeven.MApplication;
import amaz.objects.TwentyfourSeven.R;
import amaz.objects.TwentyfourSeven.data.models.UserTransaction;
import amaz.objects.TwentyfourSeven.utilities.Constants;
import amaz.objects.TwentyfourSeven.utilities.Fonts;
import amaz.objects.TwentyfourSeven.utilities.LocalSettings;
import amaz.objects.TwentyfourSeven.utilities.ValidationsUtilities;


public class TransactionViewHolder extends RecyclerView.ViewHolder  {

    private TextView dateTV, timeTv, transactionTypeTV, commentTV,amountTV;
    private Fonts fonts;
    private ImageView transactionTypeIV;
    private Context context;
    private LocalSettings localSettings;

    public TransactionViewHolder(View itemView,Context context) {
        super(itemView);
        this.context = context;
        localSettings = new LocalSettings(itemView.getContext());
        initializeViews();
        setFonts();
    }

    private void initializeViews() {
        dateTV = itemView.findViewById(R.id.tv_date);
        timeTv = itemView.findViewById(R.id.tv_time);
        transactionTypeTV = itemView.findViewById(R.id.tv_type_transaction);
        amountTV =  itemView.findViewById(R.id.tv_amount);
        commentTV= itemView.findViewById(R.id.tv_comment);
        transactionTypeIV =  itemView.findViewById(R.id.iv_type_transaction);

    }

    private void setFonts() {
        fonts = MApplication.getInstance().getFonts();

        dateTV.setTypeface(fonts.customFont());
        timeTv.setTypeface(fonts.customFont());
        transactionTypeTV.setTypeface(fonts.customFont());
        amountTV.setTypeface(fonts.customFont());
        commentTV.setTypeface(fonts.customFont());
    }

    public void setData(UserTransaction userTransaction) {

        dateTV.setText(ValidationsUtilities.reformatDateOnly(userTransaction.getCreated_at()));
        timeTv.setText(ValidationsUtilities.reformatTimeOnly(userTransaction.getCreated_at()));
        Log.e("omnia", userTransaction.getPayment_method());
        if (userTransaction.getPayment_method().equals(String.valueOf(Constants.PAYMENT_METHOD_CASH_ON_DELIVERY))){
            transactionTypeIV.setImageResource(R.drawable.withdraw_ic);
        }else if (userTransaction.getPayment_method().equals(String.valueOf(Constants.PAYMENT_METHOD_BANK_TRANSFER))){
            transactionTypeIV.setImageResource(R.drawable.transfer_ic);
        }else if (userTransaction.getPayment_method().equals(String.valueOf(Constants.PAYMENT_METHOD_SADAD))){
            transactionTypeIV.setImageResource(R.drawable.sadad);

        }else if (userTransaction.getPayment_method().equals(String.valueOf(Constants.PAYMENT_METHOD_CREDIT_CARD))){
            transactionTypeIV.setImageResource(R.drawable.credit);

        }
        transactionTypeTV.setText(userTransaction.getTitle());
        if (userTransaction.getAmount() >= 0){
            amountTV.setTextColor(context.getResources().getColor(R.color.kelly_green));
        }else{
            amountTV.setTextColor(context.getResources().getColor(R.color.app_color));

        }
        amountTV.setText(String.format(Locale.ENGLISH,"%.2f", userTransaction.getAmount()) + " "+ context.getResources().getString(R.string.sar));
        commentTV.setText(userTransaction.getDescription());

    }



}
