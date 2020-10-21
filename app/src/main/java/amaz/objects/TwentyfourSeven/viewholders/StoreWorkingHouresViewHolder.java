package amaz.objects.TwentyfourSeven.viewholders;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import amaz.objects.TwentyfourSeven.MApplication;
import amaz.objects.TwentyfourSeven.R;
import amaz.objects.TwentyfourSeven.utilities.Fonts;

public class StoreWorkingHouresViewHolder extends RecyclerView.ViewHolder {

    private TextView weekDayTv, workingHoureTv;

    private Fonts fonts;

    public StoreWorkingHouresViewHolder(@NonNull View itemView) {
        super(itemView);
        initializeViews();
        setFonts();
    }

    private void initializeViews(){
        weekDayTv = itemView.findViewById(R.id.tv_week_day);
        workingHoureTv = itemView.findViewById(R.id.tv_houres);
    }

    private void setFonts(){
        fonts = MApplication.getInstance().getFonts();
        weekDayTv.setTypeface(fonts.customFont());
        workingHoureTv.setTypeface(fonts.customFont());
    }

    public void setData(String workingHoure){
        weekDayTv.setText(workingHoure.substring(0,workingHoure.indexOf(":")));
        workingHoureTv.setText(workingHoure.substring(workingHoure.indexOf(":")+1));
    }
}
