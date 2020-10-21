package amaz.objects.TwentyfourSeven.ui.OrderChat;

import android.content.Context;
import androidx.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import amaz.objects.TwentyfourSeven.data.models.DelegateImageData;
import amaz.objects.TwentyfourSeven.data.models.Order;
import amaz.objects.TwentyfourSeven.api.APIURLs;
import amaz.objects.TwentyfourSeven.data.models.Message;
import amaz.objects.TwentyfourSeven.data.models.Route;
import amaz.objects.TwentyfourSeven.data.models.responses.CustomerOrderDetailsResponse;
import amaz.objects.TwentyfourSeven.data.models.responses.DirectionsResponse;
import amaz.objects.TwentyfourSeven.data.models.responses.UploadDelegateImagesResponse;
import amaz.objects.TwentyfourSeven.data.repositories.GoogleRepository;
import amaz.objects.TwentyfourSeven.data.repositories.OrderRepository;
import amaz.objects.TwentyfourSeven.listeners.OnResponseListener;
import amaz.objects.TwentyfourSeven.presenter.BasePresenter;
import amaz.objects.TwentyfourSeven.ui.OrderDetails.OrderDetailsPresenter;
import amaz.objects.TwentyfourSeven.ui.RequestFromStore.RequestFromStorePresenter;
import amaz.objects.TwentyfourSeven.utilities.Constants;
import amaz.objects.TwentyfourSeven.utilities.NetworkUtilities;
import okhttp3.internal.Util;
import retrofit2.Response;

public class OrderChatPresenter extends BasePresenter {

    private WeakReference<OrderChatView> view = new WeakReference<>(null);
    private OrderRepository orderRepository;
    private GoogleRepository googleRepository;
    private DatabaseReference chatDBRef;
    private ArrayList<Message> allMessages = new ArrayList<>();
    private Context context;

    public OrderChatPresenter(OrderRepository orderRepository, GoogleRepository googleRepository) {
        this.orderRepository = orderRepository;
        this.googleRepository = googleRepository;
        initializeDBReferences();

    }

    public void setView(OrderChatView view, Context context) {
        this.view = new WeakReference<>(view);
        this.context = context;
    }

    private void initializeDBReferences() {
        chatDBRef = FirebaseDatabase.getInstance().getReference().child(APIURLs.CHAT_TABLE);
    }

    public void startRide(String token, String locale, int orderId) {

        final OrderChatView orderChatView = view.get();
        orderChatView.showLoading();
        orderRepository.startRide(token, locale, orderId,
                new OnResponseListener() {
                    @Override
                    public void onSuccess(Response response) {
                        orderChatView.hideLoading();
                        orderChatView.showSuccessStartRide();

                    }

                    @Override
                    public void onFailure() {
                        orderChatView.hideLoading();
                        orderChatView.showToastNetworkError();
                    }

                    @Override
                    public void onAuthError() {
                        orderChatView.hideLoading();
                        orderChatView.showInvalideTokenError();
                    }

                    @Override
                    public void onInvalidTokenError() {
                        orderChatView.hideLoading();
                    }

                    @Override
                    public void onServerError() {
                        orderChatView.hideLoading();
                        orderChatView.showServerError();
                    }

                    @Override
                    public void onValidationError(String errorMessage) {
                        orderChatView.hideLoading();
                        orderChatView.showPerformActionError(errorMessage);
                    }

                    @Override
                    public void onSuspendedUserError(String errorMessage) {
                        orderChatView.hideLoading();
                        orderChatView.showSuspededUserError(errorMessage);
                    }
                });
    }

    public void pickItem(String token, String locale, int orderId, double itemPrice) {

        final OrderChatView orderChatView = view.get();
        orderChatView.showPickItemPopupLoading();
        orderRepository.pickItem(token, locale, orderId, itemPrice,
                new OnResponseListener() {
                    @Override
                    public void onSuccess(Response response) {
                        orderChatView.hidePickItemPopupLoading(true);
                        CustomerOrderDetailsResponse pickItemResponse = (CustomerOrderDetailsResponse) response.body();
                        orderChatView.showSuccessPickItem(pickItemResponse.getData().getOrder());

                    }

                    @Override
                    public void onFailure() {
                        orderChatView.hidePickItemPopupLoading(false);
                        orderChatView.showToastNetworkError();
                    }

                    @Override
                    public void onAuthError() {
                        orderChatView.hidePickItemPopupLoading(false);
                        orderChatView.showInvalideTokenError();
                    }

                    @Override
                    public void onInvalidTokenError() {
                        orderChatView.hidePickItemPopupLoading(false);
                    }

                    @Override
                    public void onServerError() {
                        orderChatView.hidePickItemPopupLoading(false);
                        orderChatView.showServerError();
                    }

                    @Override
                    public void onValidationError(String errorMessage) {
                        orderChatView.hidePickItemPopupLoading(false);
                        orderChatView.showPerformActionError(errorMessage);
                    }

                    @Override
                    public void onSuspendedUserError(String errorMessage) {
                        orderChatView.hidePickItemPopupLoading(false);
                        orderChatView.showSuspededUserError(errorMessage);
                    }
                });
    }

    public void pickItem(String token, String locale, int orderId) {

        final OrderChatView orderChatView = view.get();
        orderChatView.showPickItemPopupLoading();
        orderRepository.pickItem(token, locale, orderId,
                new OnResponseListener() {
                    @Override
                    public void onSuccess(Response response) {
                        orderChatView.hidePickItemPopupLoading(true);
                        CustomerOrderDetailsResponse pickItemResponse = (CustomerOrderDetailsResponse) response.body();
                        orderChatView.showSuccessPickItem(pickItemResponse.getData().getOrder());

                    }

                    @Override
                    public void onFailure() {
                        orderChatView.hidePickItemPopupLoading(false);
                        orderChatView.showToastNetworkError();
                    }

                    @Override
                    public void onAuthError() {
                        orderChatView.hidePickItemPopupLoading(false);
                        orderChatView.showInvalideTokenError();
                    }

                    @Override
                    public void onInvalidTokenError() {

                        orderChatView.hidePickItemPopupLoading(false);
                    }

                    @Override
                    public void onServerError() {
                        orderChatView.hidePickItemPopupLoading(false);
                        orderChatView.showServerError();
                    }

                    @Override
                    public void onValidationError(String errorMessage) {
                        orderChatView.hidePickItemPopupLoading(false);
                        orderChatView.showPerformActionError(errorMessage);
                    }

                    @Override
                    public void onSuspendedUserError(String errorMessage) {
                        orderChatView.hidePickItemPopupLoading(false);
                        orderChatView.showSuspededUserError(errorMessage);
                    }
                });
    }

    public void deliverOrder(String token, String locale, int orderId, String appVersion) {

        final OrderChatView orderChatView = view.get();
        orderChatView.showDeliverPopupLoading();
        orderRepository.deliverOrder(token, locale, orderId, appVersion,
                new OnResponseListener() {
                    @Override
                    public void onSuccess(Response response) {
                        orderChatView.hideDeliverPopupLoading();
                        orderChatView.showSuccessDeliverOrder();
                    }

                    @Override
                    public void onFailure() {
                        orderChatView.hideDeliverPopupLoading();
                        orderChatView.showToastNetworkError();
                    }

                    @Override
                    public void onAuthError() {
                        orderChatView.hideDeliverPopupLoading();
                        orderChatView.showInvalideTokenError();
                    }

                    @Override
                    public void onInvalidTokenError() {
                        orderChatView.hideDeliverPopupLoading();
                    }

                    @Override
                    public void onServerError() {
                        orderChatView.hideDeliverPopupLoading();
                        orderChatView.showServerError();
                    }

                    @Override
                    public void onValidationError(String errorMessage) {
                        orderChatView.hideDeliverPopupLoading();
                        orderChatView.showPerformActionError(errorMessage);
                    }

                    @Override
                    public void onSuspendedUserError(String errorMessage) {
                        orderChatView.hideDeliverPopupLoading();
                        orderChatView.showSuspededUserError(errorMessage);
                    }
                });
    }

    public void getMessages(final String orderId, final boolean isCustomer) {
        final OrderChatView chatView = view.get();
        chatView.showLoading();
        chatDBRef.child(orderId).addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                long size = dataSnapshot.getChildrenCount();
                long i = 1;
                allMessages.clear();
                if (size >0) {
                    for (DataSnapshot child : dataSnapshot.getChildren()) {

                        Message message = child.getValue(Message.class);
                        int recive_type = Constants.RECIPIENT_TYPE_DELEGATE;
                        if (isCustomer) {
                            recive_type = Constants.RECIPIENT_TYPE_CUSTOMER;
                        }
                        if (message.getRecipient_type() == Constants.RECIPIENT_TYPE_ALL || message.getRecipient_type() == recive_type) {
                            allMessages.add(message);
                        }

                    }
                    chatView.showMessagesList(allMessages);

                }

                chatView.hideLoading();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                chatView.hideLoading();
                //chatView.showToastNetworkError();
            }
        });
    }

    public void sendMessage(String orderId,Message sendingMessage) {

        final OrderChatView chatView = view.get();
        if (!NetworkUtilities.isInternetConnection(context)){
            chatView.showToastNetworkError();

        }else {
            chatView.clearMessageText();
            chatView.showLoading();
            String key = chatDBRef.child(orderId).push().getKey();
            chatDBRef.child(orderId).child(key).setValue(sendingMessage).addOnCompleteListener(new OnCompleteListener<Void>() {

                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        chatView.hideLoading();
                        chatView.showSuccessSend();
                    } else {
                        chatView.hideLoading();
                        chatView.showToastNetworkError();
                    }
                }

            });
        }

    }

    public void uploadChatImage(String token, String locale, File imageFile,String  orderId){
        final OrderChatView orderChatView = view.get();
        orderChatView.showPopUpLoading();
        orderRepository.uploadChatImage(token, locale, imageFile, orderId,new OnResponseListener() {
            @Override
            public void onSuccess(Response response) {
                orderChatView.hidePopUpLoading(true);
              orderChatView.showSuccessUploadImage((UploadDelegateImagesResponse)response.body());
            }

            @Override
            public void onFailure() {
                orderChatView.hidePopUpLoading(false);
                orderChatView.showToastNetworkError();
            }

            @Override
            public void onAuthError() {
                orderChatView.hidePopUpLoading(false);
                orderChatView.showInvalideTokenError();
            }

            @Override
            public void onInvalidTokenError() {

                orderChatView.hidePopUpLoading(false);
            }

            @Override
            public void onServerError() {
                orderChatView.hidePopUpLoading(false);
                orderChatView.showServerError();
            }

            @Override
            public void onValidationError(String errorMessage) {
                orderChatView.hidePopUpLoading(false);
            }

            @Override
            public void onSuspendedUserError(String errorMessage) {
                orderChatView.hidePopUpLoading(false);
                orderChatView.showSuspededUserError(errorMessage);
            }
        });
    }

    public void getDirections(String origin, String destination, String mode, String serverKey, final boolean isDelegateDirection) {
        final OrderChatView orderChatView = view.get();
        googleRepository.getDirections(origin, destination, mode, serverKey, new OnResponseListener() {
            @Override
            public void onSuccess(Response response) {
                DirectionsResponse directionsResponse = (DirectionsResponse) response.body();
                if (directionsResponse.getRoutes() != null && directionsResponse.getRoutes().size() > 0) {
                    orderChatView.showOrderDirections(directionsResponse.getRoutes().get(0), isDelegateDirection);
                } else {
                    orderChatView.showDefaultDistance(isDelegateDirection);
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

    public void getOrderDelegateLocation(int delegateId) {
        final OrderChatView orderChatView = view.get();
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
                        orderChatView.showDelegateCurrentLocation(delegateLatLng.get(0), delegateLatLng.get(1));
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

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

    public interface OrderChatView {

        void showLoading();

        void hideLoading();

        void showPickItemPopupLoading();

        void hidePickItemPopupLoading(boolean isFromSucess);

        void showDeliverPopupLoading();

        void hideDeliverPopupLoading();

        void showInvalideTokenError();

        void showToastNetworkError();

        void showServerError();

        void showSuspededUserError(String errorMessage);

        void showPerformActionError(String errorMessage);

        void showSuccessStartRide();

        void showSuccessPickItem(Order order);

        void showSuccessDeliverOrder();

        void showMessagesList(ArrayList<Message> messages);

        void showSuccessSend();

        void showPopUpLoading();

        void hidePopUpLoading(boolean isFromSucess);

        void clearMessageText();

        void showSuccessUploadImage(UploadDelegateImagesResponse delegateImageData);

        void showOrderDirections(Route route, boolean isDelegateDirection);

        void showDefaultDistance(boolean isDelegateDirection);

        void showDelegateCurrentLocation(double delLat, double delLng);

    }
}
