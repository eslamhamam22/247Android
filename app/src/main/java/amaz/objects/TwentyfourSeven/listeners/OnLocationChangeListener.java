package amaz.objects.TwentyfourSeven.listeners;

public interface OnLocationChangeListener {

    void setAddressData(String countryNameCode, String city, double latitude, double longitude);

    void openLocationSettings();

    void showActivateGPS();
}
