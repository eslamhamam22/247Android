package amaz.objects.TwentyfourSeven.adapters;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.compat.AutocompleteFilter;
import com.google.android.libraries.places.compat.AutocompletePrediction;
import com.google.android.libraries.places.compat.AutocompletePredictionBufferResponse;
import com.google.android.libraries.places.compat.Place;
import com.google.android.libraries.places.compat.Places;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

import java.util.ArrayList;
import java.util.Iterator;

import amaz.objects.TwentyfourSeven.R;
import amaz.objects.TwentyfourSeven.ui.AddressSearch.AddressSearchActivity;
import amaz.objects.TwentyfourSeven.utilities.NetworkUtilities;
import amaz.objects.TwentyfourSeven.viewholders.AddressSearchViewHolder;

public class AddressSearchAdapter extends RecyclerView.Adapter<AddressSearchViewHolder> {

    private Context context;
    private double longitude, latitude;
    private String countryNameCode;
    private boolean fromAddress;
    private ArrayList<PlaceAutocomplete> places = new ArrayList<>();
    private boolean fromNearBy;

    public AddressSearchAdapter(Context context, String countryNameCode, boolean fromAddress) {
        this.context = context;
        this.countryNameCode = countryNameCode;
        this.fromAddress = fromAddress;
    }

    public AddressSearchAdapter(Context context, double latitude, double longitude, String countryNameCode, boolean fromAddress) {
        this.context = context;
        this.latitude = latitude;
        this.longitude = longitude;
        this.countryNameCode = countryNameCode;
        this.fromAddress = fromAddress;
    }

    @NonNull
    @Override
    public AddressSearchViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_address_search, viewGroup, false);
        AddressSearchViewHolder vh = new AddressSearchViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull AddressSearchViewHolder addressSearchViewHolder, int i) {
        addressSearchViewHolder.setData(places.get(i));
    }

    @Override
    public int getItemCount() {
        if (places != null) {
            return places.size();
        } else {
            return 0;
        }

    }

    public PlaceAutocomplete getItem(int position) {
        return places.get(position);
    }

    private void getAutoCompletePlaces(final String searchQuery) {
        Task<AutocompletePredictionBufferResponse> task;
        if (countryNameCode != null && !countryNameCode.isEmpty()) {
            if (fromAddress) {
                task = Places.getGeoDataClient(context).getAutocompletePredictions(searchQuery,
                        new LatLngBounds(new LatLng(-0, -0), new LatLng(0, 0)),
                        new AutocompleteFilter.Builder().setTypeFilter(Place.TYPE_COUNTRY).setCountry(countryNameCode)
                                .build());
            } else {
                if (fromNearBy) {
                    task = Places.getGeoDataClient(context).getAutocompletePredictions(searchQuery,
                            new LatLngBounds(new LatLng(-0, -0), new LatLng(0, 0)),
                            new AutocompleteFilter.Builder().setTypeFilter(AutocompleteFilter.TYPE_FILTER_ADDRESS).setCountry(countryNameCode)
                                    .build());
                } else {
                    task = Places.getGeoDataClient(context).getAutocompletePredictions(searchQuery,
                            new LatLngBounds(new LatLng(latitude, longitude), new LatLng(latitude, longitude)),
                            new AutocompleteFilter.Builder().setTypeFilter(Place.TYPE_ESTABLISHMENT).setCountry(countryNameCode)
                                    .build());
                }

            }
        } else {
            if (fromAddress) {
                task = Places.getGeoDataClient(context).getAutocompletePredictions(searchQuery,
                        new LatLngBounds(new LatLng(-0, -0), new LatLng(0, 0)),
                        new AutocompleteFilter.Builder().setTypeFilter(Place.TYPE_COUNTRY).setCountry("SA")
                                .build());
            } else {
                if (fromNearBy) {
                    task = Places.getGeoDataClient(context).getAutocompletePredictions(searchQuery,
                            new LatLngBounds(new LatLng(-0, -0), new LatLng(0, 0)),
                            new AutocompleteFilter.Builder().setTypeFilter(AutocompleteFilter.TYPE_FILTER_ADDRESS).setCountry("SA")
                                    .build());
                } else {
                    task = Places.getGeoDataClient(context).getAutocompletePredictions(searchQuery,
                            new LatLngBounds(new LatLng(latitude, longitude), new LatLng(latitude, longitude)),
                            new AutocompleteFilter.Builder().setTypeFilter(Place.TYPE_ESTABLISHMENT).setCountry("SA")
                                    .build());
                }
            }
        }
        task.addOnSuccessListener(new OnSuccessListener<AutocompletePredictionBufferResponse>() {
            @Override
            public void onSuccess(AutocompletePredictionBufferResponse autocompletePredictions) {
                Iterator<AutocompletePrediction> iterator = autocompletePredictions.iterator();
                places.clear();
                while (iterator.hasNext()) {
                    AutocompletePrediction prediction = iterator.next();
                    places.add(new PlaceAutocomplete(prediction.getPlaceId(), prediction.getPrimaryText(null), prediction.getFullText(null)));
                }
                if (places.size() == 0) {
                    Log.e("zero", "zero count");
                    notifyDataSetChanged();
                    if (!searchQuery.isEmpty()) {
                        ((AddressSearchActivity) context).hideNetworkError();
                        ((AddressSearchActivity) context).showNoData();
                    } else {
                        ((AddressSearchActivity) context).hideNetworkError();
                        ((AddressSearchActivity) context).hideNodata();
                    }
                } else {
                    ((AddressSearchActivity) context).hideNodata();
                    ((AddressSearchActivity) context).hideNetworkError();
                    Log.e("zero", "not zero count");
                    notifyDataSetChanged();
                }
                autocompletePredictions.release();
            }
        });

        task.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                if (NetworkUtilities.isInternetConnection(context)) {
                    ((AddressSearchActivity) context).showNoData();
                    ((AddressSearchActivity) context).hideNetworkError();
                } else {
                    ((AddressSearchActivity) context).hideNodata();
                    ((AddressSearchActivity) context).showNetworkError();
                }
            }
        });

    }

    public void filterData(String searchQuery) {
        if (searchQuery != null) {
            getAutoCompletePlaces(searchQuery);
        }

    }


    public void setLatitude(double latitude) {
        this.latitude = latitude;
        if (fromNearBy) {
            if (places != null) {
                places.clear();
                notifyDataSetChanged();
            }
        }
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
        if (fromNearBy) {
            if (places != null) {
                places.clear();
                notifyDataSetChanged();
            }

        }

    }

    public void setCountryNameCode(String countryNameCode) {
        this.countryNameCode = countryNameCode;
    }

    public void setFromNearBy(boolean fromNearBy) {
        this.fromNearBy = fromNearBy;
    }

    public class PlaceAutocomplete {

        private CharSequence placeId;
        private CharSequence title;
        private CharSequence description;

        PlaceAutocomplete(CharSequence placeId, CharSequence title, CharSequence description) {
            this.placeId = placeId;
            this.title = title;
            this.description = description;
        }

        public CharSequence getPlaceId() {
            return placeId;
        }

        public CharSequence getTitle() {
            return title;
        }

        public CharSequence getDescription() {
            return description;
        }
    }
}
