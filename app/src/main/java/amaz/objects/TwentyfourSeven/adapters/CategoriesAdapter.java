package amaz.objects.TwentyfourSeven.adapters;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import amaz.objects.TwentyfourSeven.R;
import amaz.objects.TwentyfourSeven.data.models.Category;
import amaz.objects.TwentyfourSeven.listeners.OnCategoryClickListener;
import amaz.objects.TwentyfourSeven.viewholders.CategoriesViewHolder;

public class CategoriesAdapter extends RecyclerView.Adapter<CategoriesViewHolder> {

    private Context context;
    private ArrayList<Category> categories;
    private OnCategoryClickListener listener;

    public CategoriesAdapter(Context context, ArrayList<Category> categories, OnCategoryClickListener listener){
        this.context = context;
        this.categories = categories;
        this.listener = listener;
    }

    @NonNull
    @Override
    public CategoriesViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_category, viewGroup, false);

        ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
        layoutParams.height = (viewGroup.getHeight() / 2);
        view.setLayoutParams(layoutParams);

        CategoriesViewHolder vh = new CategoriesViewHolder(view, listener);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull CategoriesViewHolder categoriesViewHolder, int i) {
        categoriesViewHolder.setData(categories.get(i), i);
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }
}
