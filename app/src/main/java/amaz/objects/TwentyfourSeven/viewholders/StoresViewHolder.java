package amaz.objects.TwentyfourSeven.viewholders;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.net.Uri;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.signature.MediaStoreSignature;
import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.SphericalUtil;

import amaz.objects.TwentyfourSeven.MApplication;
import amaz.objects.TwentyfourSeven.R;
import amaz.objects.TwentyfourSeven.api.APIURLs;
import amaz.objects.TwentyfourSeven.data.models.Store;
import amaz.objects.TwentyfourSeven.ui.StoreDetails.StoreDetailsActivity;
import amaz.objects.TwentyfourSeven.utilities.Constants;
import amaz.objects.TwentyfourSeven.utilities.Fonts;

public class StoresViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    private RelativeLayout storeItemRl;
    private ImageView storeImageIv;
    private TextView storeNameTv, storeAddressTv, openNowTv, distanceTv;

    private Fonts fonts;

    private Store store;
    private double latitude, longitude;
    private String categoryImage;
    private boolean isFirstTime = true;

    public StoresViewHolder(@NonNull View itemView) {
        super(itemView);
        initializeViews();
        setFonts();
    }

    private void initializeViews(){
        storeItemRl = itemView.findViewById(R.id.rl_store_item);
        storeItemRl.setOnClickListener(this);
        storeImageIv = itemView.findViewById(R.id.iv_store_image);
        storeNameTv = itemView.findViewById(R.id.tv_store_name);
        storeAddressTv = itemView.findViewById(R.id.tv_store_address);
        openNowTv = itemView.findViewById(R.id.tv_open_now);
        distanceTv = itemView.findViewById(R.id.tv_distance);
    }

    private void setFonts(){
        fonts = MApplication.getInstance().getFonts();
        storeNameTv.setTypeface(fonts.customFont());
        storeAddressTv.setTypeface(fonts.customFont());
        openNowTv.setTypeface(fonts.customFont());
        distanceTv.setTypeface(fonts.customFont());
    }

    public void setData(final Store store, final String categoryImage, double currentLatitude, double currentLongitude){

        this.store = store;
        this.latitude = currentLatitude;
        this.longitude = currentLongitude;
        this.categoryImage = categoryImage;
        Log.e("store",store.isLoaded()+" isvalid");
//        Glide.with(itemView.getContext()).load(categoryImage)
//        .apply(new RequestOptions().placeholder(R.drawable.grayscale).
//                dontAnimate()).into(storeImageIv);
        if(!store.isLoaded()){
            Glide.with(itemView.getContext())
                    .load(APIURLs.BASE_URL+APIURLs.STORE_IMAGES+store.getPlaceId())
                    .signature(new MediaStoreSignature("", System.currentTimeMillis(),0))
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            store.setLoaded(true);
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            store.setLoaded(false);
                            return false;
                        }
                    })
                    .apply(new RequestOptions()
                    .placeholder(R.drawable.grayscale).dontAnimate())
                    .error(Glide.with(itemView.getContext()).load(categoryImage).apply(new RequestOptions().placeholder(R.drawable.grayscale).dontAnimate()))
                    .thumbnail(Glide
                            .with(itemView.getContext())
                            .load(categoryImage)
                    )
                    .into(storeImageIv);
        }
        else {
            Glide.with(itemView.getContext())
                    .load(categoryImage)
                    .apply(new RequestOptions().placeholder(R.drawable.grayscale).dontAnimate()
                    .error(R.drawable.grayscale).dontAnimate())
                    .into(storeImageIv);
        }

       // }
//        else {
//            Picasso.with(itemView.getContext()).load(categoryImage).placeholder(R.drawable.grayscale).into(storeImageIv);
//
//        }

        /*if(store.getPlacePhotos() != null && !store.getPlacePhotos().isEmpty()){
            Picasso.with(itemView.getContext())
                    .load("https://maps.googleapis.com/maps/api/place/photo?maxwidth=800&photoreference="+store.getPlacePhotos().get(0).getPhotoReference()
                            +"&key="+itemView.getContext().getString(R.string.google_server_key))
                    .placeholder(R.drawable.grayscale)
                    .error(R.drawable.grayscale)
                    .resize(40,40)
                    .into(storeImageIv);
        }
        else {
            Picasso.with(itemView.getContext()).load(categoryImage).placeholder(R.drawable.grayscale).into(storeImageIv);
        }*/

        storeNameTv.setText("\u200E"+store.getName()+"\u200E");
        storeAddressTv.setText("\u200E"+store.getAddress()+"\u200E");
        if (store.getOpeningHours() != null){
            if (store.getOpeningHours().isOpenNow() != null){
                if (store.getOpeningHours().isOpenNow()){
                    openNowTv.setTextColor(ContextCompat.getColor(itemView.getContext(),R.color.light_green));
                    openNowTv.setText(R.string.open_now);
                }
                else {
                    openNowTv.setTextColor(ContextCompat.getColor(itemView.getContext(),R.color.app_color));
                    openNowTv.setText(R.string.closed);
                }
            }

        }

        /*Location storeLocation = new Location("storeLocation");
        storeLocation.setLatitude(store.getGeometry().getLocation().getLatitude());
        storeLocation.setLongitude(store.getGeometry().getLocation().getLongitude());

        Location currentLocation = new Location("currentLocation");
        currentLocation.setLatitude(currentLatitude);
        currentLocation.setLongitude(currentLongitude);

        float distance = currentLocation.distanceTo(storeLocation);*/
        LatLng currentLatlng = new LatLng(currentLatitude, currentLongitude);
        LatLng storeLatlng = new LatLng(store.getGeometry().getLocation().getLatitude(), store.getGeometry().getLocation().getLongitude());
        double distance = SphericalUtil.computeDistanceBetween(currentLatlng, storeLatlng);

        if (distance<1000){
            distanceTv.setText((int)distance+" "+itemView.getContext().getResources().getString(R.string.meter));
        }
        else {
            distanceTv.setText((int)(distance/1000)+" "+itemView.getContext().getResources().getString(R.string.kilometer));
        }


    }

    private void switchToStoreDetails(){
        Intent intent = new Intent(itemView.getContext(),StoreDetailsActivity.class);
        intent.putExtra(Constants.STORE,store);
        intent.putExtra(Constants.LATITUDE, latitude);
        intent.putExtra(Constants.LONGITUDE, longitude);
        intent.putExtra(Constants.CATEGORY, categoryImage);
        itemView.getContext().startActivity(intent);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.rl_store_item){
            switchToStoreDetails();
        }
    }
}
