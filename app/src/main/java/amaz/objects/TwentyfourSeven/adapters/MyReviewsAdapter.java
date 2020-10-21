package amaz.objects.TwentyfourSeven.adapters;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import amaz.objects.TwentyfourSeven.R;
import amaz.objects.TwentyfourSeven.data.models.Review;
import amaz.objects.TwentyfourSeven.viewholders.ReviewsViewHolder;

public class MyReviewsAdapter extends RecyclerView.Adapter<ReviewsViewHolder> {

    private Context context;
    private ArrayList<Review> reviews;

    public MyReviewsAdapter(Context context, ArrayList<Review> reviews) {
        this.context = context;
        this.reviews = reviews;
    }

    @NonNull
    @Override
    public ReviewsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_my_reviews,viewGroup,false);
        ReviewsViewHolder vh = new ReviewsViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewsViewHolder reviewsViewHolder, int i) {
        reviewsViewHolder.setData(reviews.get(i));
    }

    @Override
    public int getItemCount() {
        return reviews.size();
    }
}
