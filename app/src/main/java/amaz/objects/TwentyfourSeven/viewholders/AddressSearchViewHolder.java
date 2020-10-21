package amaz.objects.TwentyfourSeven.viewholders;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import amaz.objects.TwentyfourSeven.MApplication;
import amaz.objects.TwentyfourSeven.R;
import amaz.objects.TwentyfourSeven.adapters.AddressSearchAdapter;
import amaz.objects.TwentyfourSeven.utilities.Fonts;

public class AddressSearchViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    private TextView placeTitleTv,placeContentTv;
    private AddressSearchAdapter.PlaceAutocomplete place;

    private Fonts fonts;

    public AddressSearchViewHolder(@NonNull View itemView) {
        super(itemView);
        initializeViews();
        setFonts();
    }

    private void initializeViews(){
        placeTitleTv = itemView.findViewById(R.id.tv_place_title);
        placeContentTv = itemView.findViewById(R.id.tv_place_content);
    }

    private void setFonts(){
        fonts = MApplication.getInstance().getFonts();
        placeTitleTv.setTypeface(fonts.customFont());
        placeContentTv.setTypeface(fonts.customFont());
    }

    public void setData(AddressSearchAdapter.PlaceAutocomplete place){
        this.place = place;
        placeContentTv.setText("\u200E"+place.getDescription()+"\u200E");
        placeTitleTv.setText("\u200E"+place.getTitle()+"\u200E");
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){

        }
    }
}
