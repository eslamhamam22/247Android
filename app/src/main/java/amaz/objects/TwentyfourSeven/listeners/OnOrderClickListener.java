package amaz.objects.TwentyfourSeven.listeners;

import amaz.objects.TwentyfourSeven.data.models.Address;
import amaz.objects.TwentyfourSeven.data.models.Order;

public interface OnOrderClickListener {

    void onOrderClick(Order order);
    void onShowMore();
}
