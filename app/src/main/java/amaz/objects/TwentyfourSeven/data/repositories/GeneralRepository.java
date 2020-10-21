package amaz.objects.TwentyfourSeven.data.repositories;

import amaz.objects.TwentyfourSeven.listeners.OnResponseListener;

public interface GeneralRepository {

    void getPageData(String locale, String page, OnResponseListener onGetPageResponse);
    void getHowToUseData(String locale, OnResponseListener onGetHowToUseResponse);
    void getHowToBecomeADelegateData(String locale, OnResponseListener onGetHowToUseResponse);
    void getAllCategories(String locale, OnResponseListener onGetCategoriesResponse);
    void getAllBlockedArea(String locale, OnResponseListener onGetCategoriesResponse);

}
