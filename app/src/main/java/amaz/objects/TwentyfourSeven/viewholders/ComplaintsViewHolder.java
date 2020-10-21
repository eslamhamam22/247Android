package amaz.objects.TwentyfourSeven.viewholders;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import java.util.Locale;

import amaz.objects.TwentyfourSeven.MApplication;
import amaz.objects.TwentyfourSeven.R;
import amaz.objects.TwentyfourSeven.data.models.Complaint;
import amaz.objects.TwentyfourSeven.utilities.Fonts;
import amaz.objects.TwentyfourSeven.utilities.LocalSettings;
import amaz.objects.TwentyfourSeven.utilities.ValidationsUtilities;

public class ComplaintsViewHolder extends RecyclerView.ViewHolder {

    private TextView orderNoContentTv, complaintTimeTv, complaintTitleTv, complaintStatusTv, complaintDescriptionTv;
    private Fonts fonts;
    private LocalSettings localSettings;

    public ComplaintsViewHolder(@NonNull View itemView) {
        super(itemView);
        localSettings = new LocalSettings(itemView.getContext());
        initializeViews();
        setFonts();
    }

    private void initializeViews(){
        orderNoContentTv = itemView.findViewById(R.id.tv_order_no_content);
        complaintTimeTv = itemView.findViewById(R.id.tv_time);
        complaintTitleTv = itemView.findViewById(R.id.tv_complaint_title);
        complaintStatusTv = itemView.findViewById(R.id.tv_complaint_status);
        complaintDescriptionTv = itemView.findViewById(R.id.tv_complaint_description);
    }

    private void setFonts(){
        fonts = MApplication.getInstance().getFonts();

        orderNoContentTv.setTypeface(fonts.customFont());
        complaintTimeTv.setTypeface(fonts.customFont());
        complaintTitleTv.setTypeface(fonts.customFont());
        complaintStatusTv.setTypeface(fonts.customFont());
        complaintDescriptionTv.setTypeface(fonts.customFont());
    }

    public void setData(Complaint complaint){
        orderNoContentTv.setText(String.valueOf(complaint.getRelatedOrder().getId()));
        complaintTimeTv.setText(ValidationsUtilities.reformatTime(complaint.getCreatedAt()));
        complaintTitleTv.setText(complaint.getTitle());
        complaintDescriptionTv.setText(complaint.getDescription());
        if (complaint.getStatus().equals("pending")){
            complaintStatusTv.setTextColor(ContextCompat.getColor(itemView.getContext(), R.color.app_color));
            complaintStatusTv.setText(R.string.in_progress_complaint);
        }
        else {
            complaintStatusTv.setTextColor(ContextCompat.getColor(itemView.getContext(), R.color.kelly_green));
            complaintStatusTv.setText(R.string.resolved);
        }
    }
}
