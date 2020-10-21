package amaz.objects.TwentyfourSeven.viewholders;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import amaz.objects.TwentyfourSeven.listeners.OnCategoryClickListener;
import com.squareup.picasso.Picasso;

import amaz.objects.TwentyfourSeven.MApplication;
import amaz.objects.TwentyfourSeven.R;
import amaz.objects.TwentyfourSeven.data.models.Category;
import amaz.objects.TwentyfourSeven.utilities.Fonts;

public class CategoriesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    private RelativeLayout categoryRl;
    private TextView categoryNameTv;
    private ImageView categoryImageIv;
    private View horizontalSeparatorV, verticalSeparatorV;

    private Fonts fonts;

    private OnCategoryClickListener listener;
    private Category category;

    public CategoriesViewHolder(@NonNull View itemView, OnCategoryClickListener listener) {
        super(itemView);
        this.listener = listener;
        initializeViews();
        setFonts();
    }

    private void initializeViews(){
        categoryRl = itemView.findViewById(R.id.rl_category);
        categoryRl.setOnClickListener(this);
        categoryNameTv = itemView.findViewById(R.id.tv_category_name);
        categoryImageIv = itemView.findViewById(R.id.iv_category_image);
        horizontalSeparatorV = itemView.findViewById(R.id.v_horizontal_separator);
        verticalSeparatorV = itemView.findViewById(R.id.v_vertical_separator);
    }

    private void setFonts(){
        fonts = MApplication.getInstance().getFonts();
        categoryNameTv.setTypeface(fonts.customFont());
    }

    public void setData(Category category, int position){
        this.category = category;
        categoryNameTv.setText(category.getName());
        if (category.getIcon() != null){
            Picasso.with(itemView.getContext()).load(category.getIcon().getSmall())
                      .placeholder(R.drawable.grayscale)
                      .into(categoryImageIv);

        }
        if (position == 2 || position == 5){
            verticalSeparatorV.setVisibility(View.GONE);
        }

        if (position == 3 || position == 4 || position == 5){
            horizontalSeparatorV.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.rl_category){
            listener.onCategoryClick(this.category);
        }
    }
}
