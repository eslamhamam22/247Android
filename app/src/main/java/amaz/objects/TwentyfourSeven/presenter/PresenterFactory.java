package amaz.objects.TwentyfourSeven.presenter;

import amaz.objects.TwentyfourSeven.injection.Injection;
import amaz.objects.TwentyfourSeven.ui.AccountDetails.AccountDetailsPresenter;
import amaz.objects.TwentyfourSeven.ui.AddAddress.AddAddressPresenter;
import amaz.objects.TwentyfourSeven.ui.AddMoney.AddMoneyPresenter;
import amaz.objects.TwentyfourSeven.ui.AddMoney.BankTransferPresenter;
import amaz.objects.TwentyfourSeven.ui.AddMoney.StcPayPresenter;
import amaz.objects.TwentyfourSeven.ui.AddMoney.SubmitTransactionPresenter;
import amaz.objects.TwentyfourSeven.ui.CarDetails.CarDetailsPresenter;
import amaz.objects.TwentyfourSeven.ui.Categories.CategoriesPresenter;
import amaz.objects.TwentyfourSeven.ui.ContactUs.ContactUsPresenter;
import amaz.objects.TwentyfourSeven.ui.DelegateRequest.DelegateRequestPresenter;
import amaz.objects.TwentyfourSeven.ui.HowToUse.HowToUsePresenter;
import amaz.objects.TwentyfourSeven.ui.MobileRegisteration.MobileRegisterationPresenter;
import amaz.objects.TwentyfourSeven.ui.MobileVerification.MobileVerificationPresenter;
import amaz.objects.TwentyfourSeven.ui.MyAccount.MainPresenter;
import amaz.objects.TwentyfourSeven.ui.MyAddresses.MyAddressesPresenter;
import amaz.objects.TwentyfourSeven.ui.CategoryStores.CategoryStoresPresenter;
import amaz.objects.TwentyfourSeven.ui.MyBalance.MyBalancePresenter;
import amaz.objects.TwentyfourSeven.ui.MyComplaints.MyComplaintsPresenter;
import amaz.objects.TwentyfourSeven.ui.MyOrders.MyCurrentOrdersPresenter;
import amaz.objects.TwentyfourSeven.ui.MyOrders.MyOrdersPresenter;
import amaz.objects.TwentyfourSeven.ui.MyReviews.MyReviewsPresenter;
import amaz.objects.TwentyfourSeven.ui.Notification.NotificationPresenter;
import amaz.objects.TwentyfourSeven.ui.OrderChat.OrderChatPresenter;
import amaz.objects.TwentyfourSeven.ui.OrderDetails.OrderDetailsPresenter;
import amaz.objects.TwentyfourSeven.ui.RegisterOrLogin.RegisterOrLoginPresenter;
import amaz.objects.TwentyfourSeven.ui.RequestFromStore.RequestFromStorePresenter;
import amaz.objects.TwentyfourSeven.ui.Settings.SettingsPresenter;
import amaz.objects.TwentyfourSeven.ui.Pages.PagesPresenter;
import amaz.objects.TwentyfourSeven.ui.StoreDetails.StoreDetailsPresenter;
import amaz.objects.TwentyfourSeven.ui.SubmitComplaint.SubmitComplaintPresenter;

public class PresenterFactory {

    public enum PresenterType{
        RegiterOrLogin,
        MobileRegisteration,
        MobileVerification,
        AccountDetails,
        Settings,
        Page,
        MyAddresses,
        ContactUs,
        AddAddress,
        HowToUse,
        UploadDelegateImages,
        CarDetails,
        Notification,
        Categories,
        NearestStores,
        StoreDetails,
        RequestFromStore,
        MyOrder,
        MyCurrentOrder,
        OrderDetails,
        OrderChat,
        MYBALANCE,
        ADDMONEY,
        StcPay,
        MYBANKTRANSFER,
        SUBMITTRANSACTION,

        MyReviews,
        MyComplaints,
        SubmitComplaint,
        Main
    }

    public static BasePresenter create(PresenterType type){
        BasePresenter presenter = null;
        switch (type){
            case RegiterOrLogin:
                presenter = new RegisterOrLoginPresenter(Injection.provideUserRepository(), Injection.provideGoogleRepository());
                break;

            case MobileRegisteration:
                presenter = new MobileRegisterationPresenter(Injection.provideUserRepository());
                break;

            case MobileVerification:
                presenter = new MobileVerificationPresenter(Injection.provideUserRepository());
                break;

            case AccountDetails:
                presenter = new AccountDetailsPresenter(Injection.provideUserRepository());
                break;

            case Settings:
                presenter = new SettingsPresenter(Injection.provideUserRepository());
                break;

            case Page:
                presenter = new PagesPresenter(Injection.provideGeneralRepository());
                break;

            case MyAddresses:
                presenter = new MyAddressesPresenter(Injection.provideUserRepository());
                break;

            case ContactUs:
                presenter = new ContactUsPresenter(Injection.provideUserRepository());
                break;

            case AddAddress:
                presenter = new AddAddressPresenter(Injection.provideUserRepository());
                break;

            case HowToUse:
                presenter = new HowToUsePresenter(Injection.provideGeneralRepository());
                break;

            case UploadDelegateImages:
                presenter = new DelegateRequestPresenter(Injection.provideUserRepository());
                break;

            case CarDetails:
                presenter = new CarDetailsPresenter(Injection.provideUserRepository());
                break;

            case Notification:
                presenter = new NotificationPresenter(Injection.provideUserRepository());
                break;

            case Categories:
                presenter = new CategoriesPresenter(Injection.provideGeneralRepository());
                break;

            case NearestStores:
                presenter = new CategoryStoresPresenter(Injection.provideGoogleRepository());
                break;

            case StoreDetails:
                presenter = new StoreDetailsPresenter(Injection.provideGoogleRepository());
                break;

            case RequestFromStore:
                presenter = new RequestFromStorePresenter(Injection.provideOrderRepository());
                break;
            case MyOrder:
                presenter = new MyOrdersPresenter(Injection.provideUserRepository());
                break;
            case MyCurrentOrder:
                presenter = new MyCurrentOrdersPresenter(Injection.provideUserRepository());
                break;

            case OrderDetails:
                presenter = new OrderDetailsPresenter(Injection.provideOrderRepository(), Injection.provideGoogleRepository());
                break;

            case OrderChat:
                presenter = new OrderChatPresenter(Injection.provideOrderRepository(), Injection.provideGoogleRepository());
                break;

            case MyReviews:
                presenter = new MyReviewsPresenter(Injection.provideUserRepository());
                break;

            case MyComplaints:
                presenter = new MyComplaintsPresenter(Injection.provideUserRepository());
                break;

            case MYBALANCE:
                presenter = new MyBalancePresenter(Injection.provideUserRepository());
                break;

            case ADDMONEY:
                presenter = new AddMoneyPresenter(Injection.provideUserRepository());
                break;

            case StcPay:
                presenter = new StcPayPresenter(Injection.providePaymentRepository());
                break;

            case MYBANKTRANSFER:
                presenter = new BankTransferPresenter(Injection.provideUserRepository());
                break;
            case SUBMITTRANSACTION:
                presenter = new SubmitTransactionPresenter(Injection.provideUserRepository());
                break;

            case SubmitComplaint:
                presenter = new SubmitComplaintPresenter(Injection.provideOrderRepository());
                break;

            case Main:
                presenter = new MainPresenter(Injection.provideUserRepository());
                break;
        }
        return presenter;
    }
}
