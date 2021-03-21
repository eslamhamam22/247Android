package amaz.objects.TwentyfourSeven.ui.OrderDetails;

import androidx.annotation.NonNull;

import android.content.Context;
import android.util.Log;

import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.LocationCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.google.maps.android.SphericalUtil;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Arrays;

import amaz.objects.TwentyfourSeven.api.APIURLs;
import amaz.objects.TwentyfourSeven.data.models.CancelationReason;
import amaz.objects.TwentyfourSeven.data.models.DelegateGeoFireLocation;
import amaz.objects.TwentyfourSeven.data.models.Offer;
import amaz.objects.TwentyfourSeven.data.models.Order;
import amaz.objects.TwentyfourSeven.data.models.Route;
import amaz.objects.TwentyfourSeven.data.models.responses.AddOfferResponse;
import amaz.objects.TwentyfourSeven.data.models.responses.CancelationReasonsResponse;
import amaz.objects.TwentyfourSeven.data.models.responses.CustomerOrderDetailsResponse;
import amaz.objects.TwentyfourSeven.data.models.responses.DirectionsResponse;
import amaz.objects.TwentyfourSeven.data.repositories.GoogleRepository;
import amaz.objects.TwentyfourSeven.data.repositories.OrderRepository;
import amaz.objects.TwentyfourSeven.listeners.OnResponseListener;
import amaz.objects.TwentyfourSeven.presenter.BasePresenter;
import amaz.objects.TwentyfourSeven.ui.OrderDetails.CustomerOrderDetails.CustomerOrderDetailsActivity;
import amaz.objects.TwentyfourSeven.utilities.LocalSettings;
import retrofit2.Response;

public class OrderDetailsPresenter extends BasePresenter {

    private WeakReference<OrderDetailsView> view = new WeakReference<>(null);
    private OrderRepository orderRepository;
    private GoogleRepository googleRepository;
    private int counter = 1;
    private boolean isFirstTime = true;
    private double prevLat, prevLng;

    public OrderDetailsPresenter(OrderRepository orderRepository, GoogleRepository googleRepository) {
        this.orderRepository = orderRepository;
        this.googleRepository = googleRepository;
    }

    public void setView(OrderDetailsView view) {
        this.view = new WeakReference<>(view);
    }

    public void getCustomerOrderDetails(String token, String locale, final int orderId) {
        final OrderDetailsView orderDetailsView = view.get();
        orderDetailsView.showLoading();
        orderRepository.getCustomerOrderDetails(token, locale, orderId, new OnResponseListener() {
            @Override
            public void onSuccess(Response response) {
                orderDetailsView.hideLoading();
                Order order = ((CustomerOrderDetailsResponse) response.body()).getData().getOrder();
                orderDetailsView.showOrderDetails(order);
                ArrayList<String> delegateTokens = ((CustomerOrderDetailsResponse) response.body()).getData().getDelegateTokens();
                orderDetailsView.saveDelegateTokens(delegateTokens);
                orderDetailsView.showMapData(order.getFromLat(), order.getFromLng(), order.getToLat(), order.getToLng());
            }

            @Override
            public void onFailure() {
                orderDetailsView.hideLoading();
                orderDetailsView.showScreenNetworkError();
            }

            @Override
            public void onAuthError() {
                orderDetailsView.hideLoading();
                orderDetailsView.showInvalideTokenError();
            }

            @Override
            public void onInvalidTokenError() {
                orderDetailsView.hideLoading();
            }

            @Override
            public void onServerError() {
                orderDetailsView.hideLoading();
                orderDetailsView.showServerError();
            }

            @Override
            public void onValidationError(String errorMessage) {
                orderDetailsView.hideLoading();
            }

            @Override
            public void onSuspendedUserError(String errorMessage) {
                orderDetailsView.hideLoading();
                orderDetailsView.showSuspededUserError(errorMessage);
            }
        });
    }

    public void getDelegateOrderDetails(String token, String locale, int orderId) {
        final OrderDetailsView orderDetailsView = view.get();
        orderDetailsView.showLoading();
        orderRepository.getDelegateOrderDetails(token, locale, orderId, new OnResponseListener() {
            @Override
            public void onSuccess(Response response) {
                orderDetailsView.hideLoading();
                Order order = ((CustomerOrderDetailsResponse) response.body()).getData().getOrder();
                boolean freeCommission = ((CustomerOrderDetailsResponse) response.body()).getData().isFreeCommission();
                orderDetailsView.showFreeCommission(freeCommission);
                orderDetailsView.showOrderDetails(order);
                orderDetailsView.showMapData(order.getFromLat(), order.getFromLng(), order.getToLat(), order.getToLng());
                ArrayList<String> customerTokens = ((CustomerOrderDetailsResponse) response.body()).getData().getCustomerTokens();
                orderDetailsView.saveCustomerTokens(customerTokens);
            }

            @Override
            public void onFailure() {
                orderDetailsView.hideLoading();
                orderDetailsView.showScreenNetworkError();
            }

            @Override
            public void onAuthError() {
                orderDetailsView.hideLoading();
                orderDetailsView.showInvalideTokenError();
            }

            @Override
            public void onInvalidTokenError() {
                orderDetailsView.hideLoading();
            }

            @Override
            public void onServerError() {
                orderDetailsView.hideLoading();
                orderDetailsView.showServerError();
            }

            @Override
            public void onValidationError(String errorMessage) {
                orderDetailsView.hideLoading();
                orderDetailsView.showNotAvailableOrderError(errorMessage);
            }

            @Override
            public void onSuspendedUserError(String errorMessage) {
                orderDetailsView.hideLoading();
                orderDetailsView.showSuspededUserError(errorMessage);
            }
        });
    }

    public void getDirections(String origin, String destination, String mode, String serverKey, final boolean isDelegateDirection) {
        final OrderDetailsView orderDetailsView = view.get();
        googleRepository.getDirections(origin, destination, mode, serverKey, new OnResponseListener() {
            @Override
            public void onSuccess(Response response) {
                DirectionsResponse directionsResponse = (DirectionsResponse) response.body();
                if (directionsResponse.getRoutes() != null && directionsResponse.getRoutes().size() > 0) {
                    orderDetailsView.showOrderDirections(directionsResponse.getRoutes().get(0), isDelegateDirection);
                } else {
                    orderDetailsView.showDefaultDistance(isDelegateDirection);
                }

            }

            @Override
            public void onFailure() {

            }

            @Override
            public void onAuthError() {

            }

            @Override
            public void onInvalidTokenError() {

            }

            @Override
            public void onServerError() {

            }

            @Override
            public void onValidationError(String errorMessage) {

            }

            @Override
            public void onSuspendedUserError(String errorMessage) {

            }
        });
    }

    public void addOffer(String token, String locale, int orderId, double shippingCost, double distToPickUp, double distToDelivery,
                         double delegateLat, double delegateLng) {

        final OrderDetailsView orderDetailsView = view.get();
        orderDetailsView.showPopupLoading();
        orderRepository.addOffer(token, locale, orderId, shippingCost, distToPickUp, distToDelivery, delegateLat, delegateLng,
                new OnResponseListener() {
                    @Override
                    public void onSuccess(Response response) {
                        orderDetailsView.hidePopupLoading();
                        orderDetailsView.showSuccessAddOffer(((AddOfferResponse) response.body()).getOffer());
                    }

                    @Override
                    public void onFailure() {
                        orderDetailsView.hidePopupLoading();
                        orderDetailsView.showToastNetworkError();
                    }

                    @Override
                    public void onAuthError() {
                        orderDetailsView.hidePopupLoading();
                        orderDetailsView.showInvalideTokenError();
                    }

                    @Override
                    public void onInvalidTokenError() {
                        orderDetailsView.hidePopupLoading();
                    }

                    @Override
                    public void onServerError() {
                        orderDetailsView.hidePopupLoading();
                        orderDetailsView.showServerError();
                    }

                    @Override
                    public void onValidationError(String errorMessage) {
                        orderDetailsView.hidePopupLoading();
                        //orderDetailsView.showNotAvailableOrderError(errorMessage);
                        orderDetailsView.showServerErrorMessage(errorMessage);
                    }

                    @Override
                    public void onSuspendedUserError(String errorMessage) {
                        orderDetailsView.hidePopupLoading();
                        orderDetailsView.showSuspededUserError(errorMessage);
                    }
                });
    }

    public void cancelOffer(String token, String locale, String orderId) {

        final OrderDetailsView orderDetailsView = view.get();
        orderDetailsView.showLoading();
        orderRepository.cancelOffer(token, locale, orderId,
                new OnResponseListener() {
                    @Override
                    public void onSuccess(Response response) {
                        orderDetailsView.hideLoading();
                        orderDetailsView.sucessCancel();

                    }

                    @Override
                    public void onFailure() {
                        orderDetailsView.hideLoading();
                        orderDetailsView.showToastNetworkError();
                    }

                    @Override
                    public void onAuthError() {
                        orderDetailsView.hideLoading();
                        orderDetailsView.showInvalideTokenError();
                    }

                    @Override
                    public void onInvalidTokenError() {
                        orderDetailsView.hideLoading();
                    }

                    @Override
                    public void onServerError() {
                        orderDetailsView.hideLoading();
                        orderDetailsView.showServerError();
                    }

                    @Override
                    public void onValidationError(String errorMessage) {
                        orderDetailsView.hideLoading();
                        orderDetailsView.showNotAvailableOrderError(errorMessage);
                    }

                    @Override
                    public void onSuspendedUserError(String errorMessage) {
                        orderDetailsView.hideLoading();
                        orderDetailsView.showSuspededUserError(errorMessage);
                    }
                });
    }

    public void cancelOrder(String token, String locale, String orderId, boolean isDelegate, int reason) {
        final OrderDetailsView orderDetailsView = view.get();
        orderDetailsView.showLoading();
        orderRepository.cancelOrder(token, locale, orderId, isDelegate, reason,
                new OnResponseListener() {
                    @Override
                    public void onSuccess(Response response) {
                        orderDetailsView.hideLoading();
                        orderDetailsView.showOrderCancelledSucessfully();

                    }

                    @Override
                    public void onFailure() {
                        orderDetailsView.hideLoading();
                        orderDetailsView.showToastNetworkError();
                    }

                    @Override
                    public void onAuthError() {
                        orderDetailsView.hideLoading();
                        orderDetailsView.showInvalideTokenError();
                    }

                    @Override
                    public void onInvalidTokenError() {
                        orderDetailsView.hideLoading();
                    }

                    @Override
                    public void onServerError() {
                        orderDetailsView.hideLoading();
                        orderDetailsView.showServerError();
                    }

                    @Override
                    public void onValidationError(String errorMessage) {
                        orderDetailsView.hideLoading();
                        orderDetailsView.showNotAvailableOrderError(errorMessage);
                    }

                    @Override
                    public void onSuspendedUserError(String errorMessage) {
                        orderDetailsView.hideLoading();
                        orderDetailsView.showSuspededUserError(errorMessage);
                    }
                });
    }

    public void acceptOffer(String token, String locale, long offerId) {

        final OrderDetailsView orderDetailsView = view.get();
        orderDetailsView.showLoading();
        orderRepository.acceptOffer(token, locale, offerId,
                new OnResponseListener() {
                    @Override
                    public void onSuccess(Response response) {
                        orderDetailsView.hideLoading();
                        Order order = ((CustomerOrderDetailsResponse) response.body()).getData().getOrder();
                        ArrayList<String> delegateTokens = ((CustomerOrderDetailsResponse) response.body()).getData().getDelegateTokens();
                        orderDetailsView.showSuccessAcceptOffer(order);
                        orderDetailsView.saveDelegateTokens(delegateTokens);
                    }

                    @Override
                    public void onFailure() {
                        orderDetailsView.hideLoading();
                        orderDetailsView.showToastNetworkError();
                    }

                    @Override
                    public void onAuthError() {
                        orderDetailsView.hideLoading();
                        orderDetailsView.showInvalideTokenError();
                    }

                    @Override
                    public void onInvalidTokenError() {
                        orderDetailsView.hideLoading();
                    }

                    @Override
                    public void onServerError() {
                        orderDetailsView.hideLoading();
                        orderDetailsView.showServerError();
                    }

                    @Override
                    public void onValidationError(String errorMessage) {
                        orderDetailsView.hideLoading();
                        orderDetailsView.showAcceptOfferError(errorMessage);
                    }

                    @Override
                    public void onSuspendedUserError(String errorMessage) {
                        orderDetailsView.hideLoading();
                        orderDetailsView.showSuspededUserError(errorMessage);
                    }
                });
    }

    public void rejectOffer(String token, String locale, long offerId) {

        final OrderDetailsView orderDetailsView = view.get();
        orderDetailsView.showLoading();
        orderRepository.rejectOffer(token, locale, offerId,
                new OnResponseListener() {
                    @Override
                    public void onSuccess(Response response) {
                        orderDetailsView.hideLoading();
                        Order order = ((CustomerOrderDetailsResponse) response.body()).getData().getOrder();
                        orderDetailsView.showSuccessRejectOffer(order);
                    }

                    @Override
                    public void onFailure() {
                        orderDetailsView.hideLoading();
                        orderDetailsView.showToastNetworkError();
                    }

                    @Override
                    public void onAuthError() {
                        orderDetailsView.hideLoading();
                        orderDetailsView.showInvalideTokenError();
                    }

                    @Override
                    public void onInvalidTokenError() {
                        orderDetailsView.hideLoading();
                    }

                    @Override
                    public void onServerError() {
                        orderDetailsView.hideLoading();
                        orderDetailsView.showServerError();
                    }

                    @Override
                    public void onValidationError(String errorMessage) {
                        orderDetailsView.hideLoading();
                        orderDetailsView.showAcceptOfferError(errorMessage);
                    }

                    @Override
                    public void onSuspendedUserError(String errorMessage) {
                        orderDetailsView.hideLoading();
                        orderDetailsView.showSuspededUserError(errorMessage);
                    }
                });
    }

    public void researchDelegates(String token, String locale, int orderId) {
        final OrderDetailsView orderDetailsView = view.get();
        orderDetailsView.showLoading();
        orderRepository.researchDelegates(token, locale, orderId, new OnResponseListener() {
            @Override
            public void onSuccess(Response response) {
                orderDetailsView.hideLoading();
                Order order = ((CustomerOrderDetailsResponse) response.body()).getData().getOrder();
                orderDetailsView.showOrderDetails(order);
            }

            @Override
            public void onFailure() {
                orderDetailsView.hideLoading();
                orderDetailsView.showToastNetworkError();
            }

            @Override
            public void onAuthError() {
                orderDetailsView.hideLoading();
                orderDetailsView.showInvalideTokenError();
            }

            @Override
            public void onInvalidTokenError() {
                orderDetailsView.hideLoading();
            }

            @Override
            public void onServerError() {
                orderDetailsView.hideLoading();
                orderDetailsView.showServerError();
            }

            @Override
            public void onValidationError(String errorMessage) {
                orderDetailsView.hideLoading();
                Log.e("errormessage", "error: " + errorMessage);
                orderDetailsView.showServerErrorMessage(errorMessage);
            }

            @Override
            public void onSuspendedUserError(String errorMessage) {
                orderDetailsView.hideLoading();
                orderDetailsView.showSuspededUserError(errorMessage);
            }
        });
    }

    public void delegateRateOrder(String token, String locale, int orderId, int rateNum, String comment, boolean isDelegateRate) {
        final OrderDetailsView orderDetailsView = view.get();
        orderDetailsView.showRatePopupLoading();
        orderRepository.delegateRateOrder(token, locale, orderId, rateNum, comment, isDelegateRate, new OnResponseListener() {
            @Override
            public void onSuccess(Response response) {
                orderDetailsView.hideRatePopupLoading();
                Order order = ((CustomerOrderDetailsResponse) response.body()).getData().getOrder();
                orderDetailsView.showSuccessOrderRate(order);
            }

            @Override
            public void onFailure() {
                orderDetailsView.hideRatePopupLoading();
                orderDetailsView.showToastNetworkError();
            }

            @Override
            public void onAuthError() {
                orderDetailsView.hideRatePopupLoading();
                orderDetailsView.showInvalideTokenError();
            }

            @Override
            public void onInvalidTokenError() {
                orderDetailsView.hideRatePopupLoading();
            }

            @Override
            public void onServerError() {
                orderDetailsView.hideRatePopupLoading();
                orderDetailsView.showServerError();
            }

            @Override
            public void onValidationError(String errorMessage) {
                orderDetailsView.hideRatePopupLoading();
                orderDetailsView.showNotAvailableOrderError(errorMessage);
            }

            @Override
            public void onSuspendedUserError(String errorMessage) {
                orderDetailsView.hideRatePopupLoading();
                orderDetailsView.showSuspededUserError(errorMessage);
            }
        });
    }

    public void createOrder(String token, String locale, String orderDescription, int fromType, double fromLat, double fromLng, String fromAddress,
                            double toLat, double toLng, String toAddress, String storeName, String imagesIds, int deliveryDuration) {
        final OrderDetailsView orderDetailsView = view.get();
        orderDetailsView.showLoading();
        orderRepository.createOrder(token, locale, orderDescription, fromType, fromLat, fromLng, fromAddress, toLat, toLng, toAddress,
                storeName, imagesIds, deliveryDuration, "", true,
                new OnResponseListener() {
                    @Override
                    public void onSuccess(Response response) {
                        orderDetailsView.hideLoading();
                        Order order = ((CustomerOrderDetailsResponse) response.body()).getData().getOrder();
                        orderDetailsView.showSuccessReorder(order);
                    }

                    @Override
                    public void onFailure() {
                        orderDetailsView.hideLoading();
                        orderDetailsView.showToastNetworkError();
                    }

                    @Override
                    public void onAuthError() {
                        orderDetailsView.hideLoading();
                        orderDetailsView.showInvalideTokenError();
                    }

                    @Override
                    public void onInvalidTokenError() {
                        orderDetailsView.hideLoading();
                    }

                    @Override
                    public void onServerError() {
                        orderDetailsView.hideLoading();
                        orderDetailsView.showServerError();
                    }

                    @Override
                    public void onValidationError(String errorMessage) {
                        orderDetailsView.hideLoading();
                        //orderDetailsView.showBlockedAreaError();
                        orderDetailsView.showServerErrorMessage(errorMessage);

                    }

                    @Override
                    public void onSuspendedUserError(String errorMessage) {
                        orderDetailsView.hideLoading();
                        orderDetailsView.showSuspededUserError(errorMessage);
                    }
                });
    }

    public void getOrderDelegateLocation(int delegateId) {
        final OrderDetailsView orderDetailsView = view.get();
        DatabaseReference delegatesLocationsDBRef = FirebaseDatabase.getInstance().getReference().child(APIURLs.DELEGATES_LOCATIONS)
                .child(String.valueOf(delegateId));
        delegatesLocationsDBRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                /*if (isFirstTime){
                    for (DataSnapshot child : dataSnapshot.getChildren()) {
                        if (child.getKey().equals("l")) {
                            GenericTypeIndicator<ArrayList<Double>> t = new GenericTypeIndicator<ArrayList<Double>>() {
                            };
                            ArrayList<Double> delegateLatLng = child.getValue(t);
                            Log.e("omnia1", delegateLatLng.toString());
                            orderDetailsView.showDelegateCurrentLocation(delegateLatLng.get(0), delegateLatLng.get(1));
                        }
                    }
                    isFirstTime = false;
                }
                else {
                    if (counter == 1){
                        for (DataSnapshot child : dataSnapshot.getChildren()) {
                            if (child.getKey().equals("l")) {
                                GenericTypeIndicator<ArrayList<Double>> t = new GenericTypeIndicator<ArrayList<Double>>() {
                                };
                                ArrayList<Double> delegateLatLng = child.getValue(t);
                                Log.e("omnia2", delegateLatLng.toString());
                                orderDetailsView.showDelegateCurrentLocation(delegateLatLng.get(0), delegateLatLng.get(1));
                            }
                        }
                        counter++;
                    }
                }
                if (counter == 2){
                    counter = 1;
                }*/


                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    Log.e("keylocation", child.getKey());
                    if (child.getKey().equals("l")) {
                        GenericTypeIndicator<ArrayList<Double>> t = new GenericTypeIndicator<ArrayList<Double>>() {
                        };
                        ArrayList<Double> delegateLatLng = child.getValue(t);
                        /*Log.e("omnia1", delegateLatLng.toString());
                        if (prevLat == 0 && prevLng == 0){
                            prevLat = delegateLatLng.get(0);
                            prevLng = delegateLatLng.get(1);
                            Log.e("updateloc1","updateloc1");*/
                        orderDetailsView.showDelegateCurrentLocation(delegateLatLng.get(0), delegateLatLng.get(1));
                        /*} else {
                            if (SphericalUtil.computeDistanceBetween(new LatLng(prevLat, prevLng),
                                    new LatLng(delegateLatLng.get(0), delegateLatLng.get(1))) > 15){
                                    orderDetailsView.showDelegateCurrentLocation(delegateLatLng.get(0), delegateLatLng.get(1));
                                    prevLat = delegateLatLng.get(0);
                                    prevLng = delegateLatLng.get(1);
                                Log.e("updateloc2","updateloc2");
                            }
                        }
                    }*/
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void getOrderDelegateLocationOnce(int delegateId) {
        final OrderDetailsView orderDetailsView = view.get();
        DatabaseReference delegatesLocationsDBRef = FirebaseDatabase.getInstance().getReference().child(APIURLs.DELEGATES_LOCATIONS)
                .child(String.valueOf(delegateId));
        delegatesLocationsDBRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    Log.e("keylocation", child.getKey());
                    if (child.getKey().equals("l")) {
                        GenericTypeIndicator<ArrayList<Double>> t = new GenericTypeIndicator<ArrayList<Double>>() {
                        };
                        ArrayList<Double> delegateLatLng = child.getValue(t);
                        orderDetailsView.showDelegateCurrentLocation(delegateLatLng.get(0), delegateLatLng.get(1));
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void getCancelationReasons(String locale, int customerOrDelegate) {
        final OrderDetailsView orderDetailsView = view.get();
        orderDetailsView.showLoading();
        orderRepository.getCancelationReasons(locale, customerOrDelegate,
                new OnResponseListener() {
                    @Override
                    public void onSuccess(Response response) {
                        orderDetailsView.hideLoading();
                        ArrayList<CancelationReason> cancelationReasons = ((CancelationReasonsResponse) response.body()).getData();
                        orderDetailsView.showCancelationReasons(cancelationReasons);
                    }

                    @Override
                    public void onFailure() {
                        orderDetailsView.hideLoading();
                        orderDetailsView.showToastNetworkError();
                    }

                    @Override
                    public void onAuthError() {
                        orderDetailsView.hideLoading();

                    }

                    @Override
                    public void onInvalidTokenError() {
                        orderDetailsView.hideLoading();
                    }

                    @Override
                    public void onServerError() {
                        orderDetailsView.hideLoading();
                        orderDetailsView.showServerError();
                    }

                    @Override
                    public void onValidationError(String errorMessage) {
                        orderDetailsView.hideLoading();
                    }

                    @Override
                    public void onSuspendedUserError(String errorMessage) {
                        orderDetailsView.hideLoading();
                    }
                });
    }

    public void ignoreOrder(String token, String locale, int orderId) {

        final OrderDetailsView orderDetailsView = view.get();
        orderDetailsView.showLoading();
        orderRepository.ignoreOrder(token, locale, orderId,
                new OnResponseListener() {
                    @Override
                    public void onSuccess(Response response) {
                        orderDetailsView.hideLoading();
                        orderDetailsView.showSuccessIgnoreOrder();

                    }

                    @Override
                    public void onFailure() {
                        orderDetailsView.hideLoading();
                        orderDetailsView.showToastNetworkError();
                    }

                    @Override
                    public void onAuthError() {
                        orderDetailsView.hideLoading();
                        orderDetailsView.showInvalideTokenError();
                    }

                    @Override
                    public void onInvalidTokenError() {
                        orderDetailsView.hideLoading();
                    }

                    @Override
                    public void onServerError() {
                        orderDetailsView.hideLoading();
                        orderDetailsView.showServerError();
                    }

                    @Override
                    public void onValidationError(String errorMessage) {
                        orderDetailsView.hideLoading();
                        orderDetailsView.showServerErrorMessage(errorMessage);
                        orderDetailsView.showSuccessIgnoreOrder();

                    }

                    @Override
                    public void onSuspendedUserError(String errorMessage) {
                        orderDetailsView.hideLoading();
                        orderDetailsView.showSuspededUserError(errorMessage);
                    }
                });
    }


    @Override
    public void onResume() {

    }

    @Override
    public void onPause() {

    }

    @Override
    public void onDestroy() {

    }

    public interface OrderDetailsView {

        void showLoading();

        void hideLoading();

        void showPopupLoading();

        void hidePopupLoading();

        void showRatePopupLoading();

        void hideRatePopupLoading();

        void showInvalideTokenError();

        void showScreenNetworkError();

        void showToastNetworkError();

        void showServerError();

        void showSuspededUserError(String errorMessage);

        void showNotAvailableOrderError(String errorMessage);

        void showAcceptOfferError(String errorMessage);

        void showOrderDetails(Order order);

        void showFreeCommission(boolean freeCommission);

        void showMapData(final double fromLat, final double fromLng, final double toLat, final double toLng);

        void showOrderDirections(Route route, boolean isDelegateDirection);

        void showDefaultDistance(boolean isDelegateDirection);

        void showSuccessAddOffer(Offer offer);

        void sucessCancel();

        void showOrderCancelledSucessfully();

        void showSuccessAcceptOffer(Order order);

        void showSuccessRejectOffer(Order order);

        void showSuccessOrderRate(Order order);

        void showSuccessReorder(Order order);

        void showDelegateCurrentLocation(double latitue, double longitude);

        void showBlockedAreaError();

        void showCancelationReasons(ArrayList<CancelationReason> cancelationReasons);

        void showSuccessIgnoreOrder();

        void showServerErrorMessage(String errorMessage);

        void saveCustomerTokens(ArrayList<String> tokens);

        void saveDelegateTokens(ArrayList<String> tokens);

    }

}
