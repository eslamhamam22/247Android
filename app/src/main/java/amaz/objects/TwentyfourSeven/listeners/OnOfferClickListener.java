package amaz.objects.TwentyfourSeven.listeners;

import amaz.objects.TwentyfourSeven.data.models.Offer;

public interface OnOfferClickListener {

    void onAcceptOfferClick(Offer offer);

    void onRejectOfferClick(Offer offer);

    void onDelegateClick(int delegateId);

}
