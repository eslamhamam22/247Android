package amaz.objects.TwentyfourSeven.viewholders;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.Locale;

import amaz.objects.TwentyfourSeven.MApplication;
import amaz.objects.TwentyfourSeven.R;
import amaz.objects.TwentyfourSeven.data.models.Review;
import amaz.objects.TwentyfourSeven.utilities.Fonts;
import amaz.objects.TwentyfourSeven.utilities.LocalSettings;
import amaz.objects.TwentyfourSeven.utilities.ValidationsUtilities;

public class ReviewsViewHolder extends RecyclerView.ViewHolder {

    private ImageView userImageIv, userRateIv;
    private TextView userNameTv, userCommentTv, commentTimeTv;

    private Fonts fonts;
    private LocalSettings localSettings;

    public ReviewsViewHolder(@NonNull View itemView) {
        super(itemView);
        localSettings = new LocalSettings(itemView.getContext());
        initializeViews();
        setFonts();
    }

    private void initializeViews(){
        userImageIv = itemView.findViewById(R.id.iv_user_image);
        userRateIv = itemView.findViewById(R.id.iv_rate);
        userNameTv = itemView.findViewById(R.id.tv_user_name);
        userCommentTv = itemView.findViewById(R.id.tv_user_review);
        commentTimeTv = itemView.findViewById(R.id.tv_time);
    }

    private void setFonts(){
        fonts = MApplication.getInstance().getFonts();
        userNameTv.setTypeface(fonts.customFont());
        userCommentTv.setTypeface(fonts.customFont());
        commentTimeTv.setTypeface(fonts.customFont());
    }

    public void setData(Review review){
        if (review.getCreatedBy().getImage() != null){
            Picasso.with(itemView.getContext()).load(review.getCreatedBy().getImage().getMedium()).placeholder(R.drawable.avatar)
                    .error(R.drawable.avatar).into(userImageIv);
        }
        if (review.getRate() == 1){
            userRateIv.setImageResource(R.drawable.very_bad);
        }
        else if (review.getRate() == 2){
            userRateIv.setImageResource(R.drawable.bad);
        }
        else if (review.getRate() == 3){
            userRateIv.setImageResource(R.drawable.normal);
        }
        else if (review.getRate() == 4){
            userRateIv.setImageResource(R.drawable.good);
        }
        else if (review.getRate() == 5){
            userRateIv.setImageResource(R.drawable.very_good);
        }
        userNameTv.setText(review.getCreatedBy().getName());
        userCommentTv.setText(review.getComment());
        commentTimeTv.setText(ValidationsUtilities.reformatTimeToTwoLines(review.getCreatedAt()));
    }

}
