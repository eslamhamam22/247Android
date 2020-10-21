package amaz.objects.TwentyfourSeven.viewholders;

import android.content.Context;
import android.graphics.Color;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Locale;

import amaz.objects.TwentyfourSeven.MApplication;
import amaz.objects.TwentyfourSeven.R;
import amaz.objects.TwentyfourSeven.data.models.Notification;
import amaz.objects.TwentyfourSeven.listeners.OnNotificationClickListnner;
import amaz.objects.TwentyfourSeven.utilities.Fonts;
import amaz.objects.TwentyfourSeven.utilities.LocalSettings;
import amaz.objects.TwentyfourSeven.utilities.ValidationsUtilities;


public class NotificationsViewHolder extends RecyclerView.ViewHolder {

    private TextView notificationTitle,notificationDate,notificationContent;
    private View notificationView;
    private Fonts fonts;
    private LinearLayout backLin;
    private View itemView;
    private Notification notification;
    private OnNotificationClickListnner onNotificationClickListnner;
    private LocalSettings localSettings;
    private ImageView notif_dot;
    Context context;
    public NotificationsViewHolder(View itemView, Context context, final OnNotificationClickListnner onNotificationClickListnner) {
        super(itemView);
        this.itemView = itemView;
        this.context = context;

        initializeViews();
        this.onNotificationClickListnner = onNotificationClickListnner;

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onNotificationClickListnner.onNotificationClicked(notification);

            }
        });
        setFonts();
    }

    private void initializeViews(){
        notificationDate = itemView.findViewById(R.id.notification_date);
        notificationContent = itemView.findViewById(R.id.notification_content);
        notif_dot = itemView.findViewById(R.id.notif_dot);

        backLin = itemView.findViewById(R.id.backViewNot);
         localSettings = new LocalSettings(context);
        fonts = MApplication.getInstance().getFonts();
    }
    private void setFonts(){
        notificationDate.setTypeface(fonts.customFont());
        notificationContent.setTypeface(fonts.customFont());
    }
    public void setData(Notification notification,int postion) {
        this.notification = notification;
        notificationContent.setText(notification.message);
        notificationDate.setText(ValidationsUtilities.reformatTime(notification.created_at));
        if (localSettings.getUser().getUnseen_notifications_count() > 0) {
            if (localSettings.getUser().getUnseen_notifications_count() > postion) {
                notificationContent.setTextColor(ContextCompat.getColor(context,R.color.title_black));
                notif_dot.setVisibility(View.VISIBLE);
//                backLin.setBackgroundResource(R.color.white);

            } else {
                //seen notification
                notificationContent.setTextColor(ContextCompat.getColor(context,R.color.notification_gray));
//                backLin.setBackgroundResource(R.color.smoke_white);

                notif_dot.setVisibility(View.INVISIBLE);

            }
        }
        else {
            notificationContent.setTextColor(ContextCompat.getColor(context,R.color.notification_gray));
//                backLin.setBackgroundResource(R.color.smoke_white);

            notif_dot.setVisibility(View.INVISIBLE);
        }
    }
}
