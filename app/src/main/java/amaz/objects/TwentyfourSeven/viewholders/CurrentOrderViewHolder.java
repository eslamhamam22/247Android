package amaz.objects.TwentyfourSeven.viewholders;

import android.content.Context;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.Locale;

import amaz.objects.TwentyfourSeven.MApplication;
import amaz.objects.TwentyfourSeven.R;
import amaz.objects.TwentyfourSeven.data.models.Order;
import amaz.objects.TwentyfourSeven.listeners.OnOrderClickListener;
import amaz.objects.TwentyfourSeven.utilities.Fonts;
import amaz.objects.TwentyfourSeven.utilities.LocalSettings;
import amaz.objects.TwentyfourSeven.utilities.ValidationsUtilities;


public class CurrentOrderViewHolder extends RecyclerView.ViewHolder  implements View.OnClickListener {

    private TextView orderNum, tv_date, tv_store_name,tv_driver_name,toAddress,tv_status,noDataTxt;
    private Fonts fonts;
    private Order order;
    private ConstraintLayout dataLayout;
    private RelativeLayout nodata_layout;
    private ImageView image_drive, noDataImage,image_loc,image_loc_ar,image_loc_en;
    private Context context;
    private OnOrderClickListener onOrderClickListener;
    private LocalSettings localSettings;

    public CurrentOrderViewHolder(View itemView, Context context, OnOrderClickListener onOrderClickListener) {
        super(itemView);
        this.context = context;
        this.onOrderClickListener = onOrderClickListener;
        localSettings = new LocalSettings(context);

        initializeViews();
        setFonts();
    }

    private void initializeViews() {
        orderNum = itemView.findViewById(R.id.tv_order_num);
        tv_date = itemView.findViewById(R.id.tv_date);
        tv_status = itemView.findViewById(R.id.tv_status);
        nodata_layout =  itemView.findViewById(R.id.nodata_layout);
        dataLayout=  itemView.findViewById(R.id.dataLayout);
        tv_store_name = itemView.findViewById(R.id.tv_store_name);
        tv_driver_name = itemView.findViewById(R.id.tv_driver_name);
        toAddress = itemView.findViewById(R.id.tv_to_address);
        image_drive = itemView.findViewById(R.id.image_drive);
        noDataImage = itemView.findViewById(R.id.no_data);
        noDataTxt = itemView.findViewById(R.id.noDataTxt);
        image_loc =  itemView.findViewById(R.id.image_loc_3);
        image_loc_ar =  itemView.findViewById(R.id.image_loc_ar);
        image_loc_en =  itemView.findViewById(R.id.image_loc_en);

        dataLayout.setOnClickListener(this);

    }

    private void setFonts() {
        fonts = MApplication.getInstance().getFonts();
      //  orderNum.setTypeface(fonts.customFont());
        tv_date.setTypeface(fonts.customFont());
        tv_store_name.setTypeface(fonts.customFont());
        tv_status.setTypeface(fonts.customFont());
        tv_driver_name.setTypeface(fonts.customFont());
        toAddress.setTypeface(fonts.customFont());
        noDataTxt.setTypeface(fonts.customFont());

    }
    public void setData(String title) {
        nodata_layout.setVisibility(View.VISIBLE);
        dataLayout.setVisibility(View.GONE);
        if (title.equals(context.getResources().getString(R.string.current_request))) {
           noDataTxt.setText(context.getResources().getString(R.string.no_current_order));

        }else{
            noDataTxt.setText(context.getResources().getString(R.string.no_history_order));

        }

    }

    public void setData(Order order) {
        this.order = order;
        nodata_layout.setVisibility(View.GONE);
        dataLayout.setVisibility(View.VISIBLE);
        orderNum.setText(order.getId() + "");
        if (order.getStore_name() == null){
            tv_store_name.setText(order.getFrom_address());

        }else{
            tv_store_name.setText(order.getStore_name());

        }
        if (localSettings.getLocale().equals("ar")){
            image_loc_ar.setVisibility(View.VISIBLE);
            image_loc_en.setVisibility(View.INVISIBLE);

        }else{
            image_loc_ar.setVisibility(View.GONE);
            image_loc_en.setVisibility(View.VISIBLE);
        }
        if (order.getDelegate() != null){
            tv_driver_name.setText(order.getDelegate().getName());
            image_drive.setVisibility(View.VISIBLE);
            tv_driver_name.setVisibility(View.VISIBLE);
           image_loc.setImageResource(R.drawable.line_dashes_big);
        }else{
            image_drive.setVisibility(View.GONE);
            tv_driver_name.setVisibility(View.GONE);
//            if (localSettings.getLocale().equals("ar")){
//                image_loc.setImageResource(R.drawable.from_to_pins_01);
//
//            }else{
               image_loc.setImageResource(R.drawable.line_dashes_sm);
//
//            }
        }
        setStatusColor(order.getStatus());
       // tv_status.setText(order.getStatus());
        toAddress.setText(order.getTo_address());

        tv_date.setText(ValidationsUtilities.reformatTime(order.getCreated_at()));

    }

    public void setStatusColor(String status){
        if (status.equals("new")){
            tv_status.setText(context.getResources().getString(R.string.newstaus));
            tv_status.setTextColor(context.getResources().getColor(R.color.hint_blue));

        } else if(status.equals("in_progress")){
            tv_status.setText(context.getResources().getString(R.string.in_progress));
            tv_status.setTextColor(context.getResources().getColor(R.color.in_progres_color));

        } else if(status.equals("delivery_in_progress")){
            tv_status.setText(context.getResources().getString(R.string.being_delivered));
            tv_status.setTextColor(context.getResources().getColor(R.color.progress_color));

        } else if(status.equals("cancelled")){
            tv_status.setText(context.getResources().getString(R.string.cancelled));
            tv_status.setTextColor(context.getResources().getColor(R.color.app_color));
        }else if(status.equals("pending")){
            tv_status.setText(context.getResources().getString(R.string.pending));
            tv_status.setTextColor(context.getResources().getColor(R.color.pending_color));
        }else if(status.equals("assigned")){
            tv_status.setText(context.getResources().getString(R.string.assigned));
            tv_status.setTextColor(context.getResources().getColor(R.color.assigned_color));
        }else if(status.equals("delivered")){
            tv_status.setText(context.getResources().getString(R.string.delivered));
            tv_status.setTextColor(context.getResources().getColor(R.color.light_green));

        }

    }


    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.dataLayout){
            onOrderClickListener.onOrderClick(order);
        }
    }
}
