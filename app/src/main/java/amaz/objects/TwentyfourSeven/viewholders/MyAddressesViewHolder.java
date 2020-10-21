package amaz.objects.TwentyfourSeven.viewholders;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import amaz.objects.TwentyfourSeven.MApplication;
import amaz.objects.TwentyfourSeven.R;
import amaz.objects.TwentyfourSeven.data.models.Address;
import amaz.objects.TwentyfourSeven.listeners.OnAddressClickListener;
import amaz.objects.TwentyfourSeven.utilities.Fonts;

public class MyAddressesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    private RelativeLayout itemAddressRl;
    private TextView addressTitleTv,addressContentTv;
    private ImageView deleteAddressIv, selectIv;
    private OnAddressClickListener listener;
    private boolean fromDestination;

    private Address currentAddress;

    private Fonts fonts;

    public MyAddressesViewHolder(@NonNull View itemView, OnAddressClickListener listener, boolean fromDestination) {
        super(itemView);
        this.listener = listener;
        this.fromDestination = fromDestination;
        initializeViews();
        setFonts();
    }

    private void initializeViews(){
        itemAddressRl = itemView.findViewById(R.id.rl_item_address);
        itemAddressRl.setOnClickListener(this);
        addressTitleTv = itemView.findViewById(R.id.tv_addresss_title);
        addressContentTv = itemView.findViewById(R.id.tv_address_content);
        deleteAddressIv = itemView.findViewById(R.id.iv_delete);
        deleteAddressIv.setOnClickListener(this);
        selectIv = itemView.findViewById(R.id.iv_select);
        addressTitleTv.setTextColor(ContextCompat.getColor(itemView.getContext(),R.color.black));
        selectIv.setVisibility(View.GONE);
        if (fromDestination){
            deleteAddressIv.setVisibility(View.GONE);
        }
        else {
            deleteAddressIv.setVisibility(View.VISIBLE);
        }
    }

    private void setFonts(){
        fonts = MApplication.getInstance().getFonts();
        addressTitleTv.setTypeface(fonts.customFont());
        addressContentTv.setTypeface(fonts.customFont());
    }

    public void setData(Address address){
        this.currentAddress = address;
        addressTitleTv.setText(address.getAddressTitle());
        addressContentTv.setText(address.getAddressDetails());
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.iv_delete:
                listener.onDeleteAddressClick(this.currentAddress);
                break;

            case R.id.rl_item_address:
                if (fromDestination){
                    addressTitleTv.setTextColor(ContextCompat.getColor(itemView.getContext(),R.color.background_blue));
                    selectIv.setVisibility(View.VISIBLE);
                    listener.onAddressClick(this.currentAddress);
                }
                break;
        }
    }
}
