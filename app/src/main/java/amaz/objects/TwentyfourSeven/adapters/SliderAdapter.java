package amaz.objects.TwentyfourSeven.adapters;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import amaz.objects.TwentyfourSeven.MApplication;
import amaz.objects.TwentyfourSeven.R;
import amaz.objects.TwentyfourSeven.data.models.Slider;
import amaz.objects.TwentyfourSeven.utilities.Constants;
import amaz.objects.TwentyfourSeven.utilities.Fonts;

public class SliderAdapter extends PagerAdapter {

    private Context context;
    private String locale;
    private ArrayList<Slider> pagerItems;

    private ImageView sliderImage;
    private TextView sliderTitle, sliderDescription;

    private Fonts fonts;


    public SliderAdapter(Context context, String locale, ArrayList<Slider> pagerItems){
        this.context = context;
        this.locale = locale;
        this.pagerItems = pagerItems;
    }

    @Override
    public int getCount() {
        return pagerItems.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {

        return (view == (LinearLayout)o);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View pagerItemView = LayoutInflater.from(context).inflate(R.layout.item_slider,container,false);
        sliderImage = pagerItemView.findViewById(R.id.iv_slider_image);
        sliderTitle = pagerItemView.findViewById(R.id.tv_slider_title);
        sliderDescription = pagerItemView.findViewById(R.id.tv_slider_description);
        if (locale.equals(Constants.ARABIC)){
            sliderImage.setRotationY(180);
            sliderTitle.setRotationY(180);
            sliderDescription.setRotationY(180);
        }
        Picasso.with(context).load(pagerItems.get(position).getImage().getBig()).placeholder(R.drawable.howtouse_ic).into(sliderImage);
        sliderTitle.setText(pagerItems.get(position).getTitle());
        sliderDescription.setText(pagerItems.get(position).getDescription());
        setFonts();
        container.addView(pagerItemView,0);
        return pagerItemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    private void setFonts(){
        fonts = MApplication.getInstance().getFonts();
        sliderTitle.setTypeface(fonts.customFontBD());
        sliderDescription.setTypeface(fonts.customFont());
    }
}
