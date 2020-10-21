package amaz.objects.TwentyfourSeven.viewholders;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.zakariya.stickyheaders.SectioningAdapter;

import amaz.objects.TwentyfourSeven.MApplication;
import amaz.objects.TwentyfourSeven.R;
import amaz.objects.TwentyfourSeven.listeners.OnOrderClickListener;
import amaz.objects.TwentyfourSeven.utilities.Fonts;

/**
 * Created by Asmaa Gaber on 02/07/19.
 */

public class OrderSectionViewHolder extends SectioningAdapter.HeaderViewHolder {


    private TextView title,tv_all_orders;
    private Fonts fonts;
    private ImageView sectionImage;
    private Context context;
    private OnOrderClickListener onOrderClickListener;


    public OrderSectionViewHolder(View itemView, Context context, OnOrderClickListener onOrderClickListener) {
        super(itemView);
        this.context = context;
        this.onOrderClickListener = onOrderClickListener;
        initializeViews();
        setFonts();
    }

    private void initializeViews(){

        title = itemView.findViewById(R.id.tv_section_title);
        sectionImage = itemView.findViewById(R.id.section_image);
        tv_all_orders = itemView.findViewById(R.id.tv_all_orders);
        tv_all_orders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onOrderClickListener.onShowMore();
            }
        });

    }

    private void setFonts(){
        fonts = MApplication.getInstance().getFonts();
        title.setTypeface(fonts.customFontBD());
        tv_all_orders.setTypeface(fonts.customFont());
    }

    public void setData(String sectionTitle,int size_data,int sizeSection){
        title.setText(sectionTitle);
        if (sectionTitle.equals(context.getResources().getString(R.string.current_request))) {
            sectionImage.setImageResource(R.drawable.current_orders);
            if (size_data >2 ) {
                tv_all_orders.setVisibility(View.VISIBLE);
            }else{
                tv_all_orders.setVisibility(View.GONE);

            }

        }else {
            sectionImage.setImageResource(R.drawable.history_ic);
            tv_all_orders.setVisibility(View.GONE);
        }
    }


}
