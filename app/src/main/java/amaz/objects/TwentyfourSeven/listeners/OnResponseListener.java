package amaz.objects.TwentyfourSeven.listeners;

import retrofit2.Response;

public interface OnResponseListener {

    void onSuccess(Response response);

    void onFailure();

    void onAuthError();

    void onInvalidTokenError();

    void onServerError();

    void onValidationError(String errorMessage);

    void onSuspendedUserError(String errorMessage);
}
