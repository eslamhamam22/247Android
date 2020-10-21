package amaz.objects.TwentyfourSeven.api;

/*
 * Steps for generating dev or test or stage version:
 * 1- change APIURLS file and uncomment the dev or test or stage block
 * 2- change build.gradle (Module: app) file and uncomment the test block for one signal
 * 3- change values/strings and values/strings(ar) files in res and uncomment the GOOGLE_DEV_KEYS block
 * 4- change build variants to be debug and build apk after build is finished
 * */

/*
 * Steps for generating production version:
 * 1- change APIURLS file and uncomment the prod block
 * 2- change build.gradle (Module: app) file and uncomment the Live block for one signal
 * 3- change values/strings and values/strings(ar) files in res and uncomment the GOOGLE_PROD_KEYS block
 * 4- change build variants to be release and generate signed apk after build is finished
 * */

public class APIURLs {

   /* public static final String BASE_URL = "https://247dev.objectsdev.com/"; //dev
    public static final String DELEGATES_LOCATIONS = "dev/delegates_locations";
    public static final String DELEGATES = "dev/delegates";
    public static final String CHAT_TABLE = "dev/orders_chat";
    public static final String CLIENTID ="387615629444-hhqts1ei8oslrpb97ast6c14qpdafgf7.apps.googleusercontent.com";
    public static final String SECRETID ="2QXhAz7vDaeTK-eJnkQALKxN";
    public static final String google_android_key ="AIzaSyCW7uAajspdHmOm9Rz5CrxPYAN8R5UXnMQ";
    public static final String google_server_key ="AIzaSyCBZqsbT6XjJU_c4fczxqKVlmVT886085c";*/

//    public static final String BASE_URL = "https://247test.objectsdev.com/";  //test
//    public static final String DELEGATES_LOCATIONS = "test/delegates_locations";
//    public static final String DELEGATES = "test/delegates";
//    public static final String CHAT_TABLE = "test/orders_chat";
//    public static final String CLIENTID ="387615629444-hhqts1ei8oslrpb97ast6c14qpdafgf7.apps.googleusercontent.com";
//    public static final String SECRETID ="2QXhAz7vDaeTK-eJnkQALKxN";
//    public static final String google_android_key ="AIzaSyCW7uAajspdHmOm9Rz5CrxPYAN8R5UXnMQ";
//    public static final String google_server_key ="AIzaSyCBZqsbT6XjJU_c4fczxqKVlmVT886085c";

    /*public static final String BASE_URL = "https://247automation.objectsdev.com/";  //automation
    public static final String DELEGATES_LOCATIONS = "247_automati+on/delegates_locations";
    public static final String DELEGATES = "247_automation/delegates";
    public static final String CHAT_TABLE = "247_automation/orders_chat";*/

//    public static final String BASE_URL = "https://247.objectsdev.com/";  //stage
//    public static final String DELEGATES_LOCATIONS = "stage/delegates_locations";
//    public static final String DELEGATES = "stage/delegates";
//    public static final String CHAT_TABLE = "stage/orders_chat";
//    public static final String CLIENTID ="387615629444-hhqts1ei8oslrpb97ast6c14qpdafgf7.apps.googleusercontent.com";
//    public static final String SECRETID ="2QXhAz7vDaeTK-eJnkQALKxN";
//    public static final String google_android_key ="AIzaSyCW7uAajspdHmOm9Rz5CrxPYAN8R5UXnMQ";
//    public static final String google_server_key ="AIzaSyCBZqsbT6XjJU_c4fczxqKVlmVT886085c";

    public static final String BASE_URL = "https://app.24-7-delivery.com/";  //prod
    public static final String DELEGATES_LOCATIONS = "prod/delegates_locations";
    public static final String DELEGATES = "prod/delegates";
    public static final String CHAT_TABLE = "prod/orders_chat";
    public static final String CLIENTID ="658431378721-23pau7q0bo9addbr3pqdbtl6ctfdrkul.apps.googleusercontent.com";
    public static final String SECRETID ="Dy-uviiC6etQVfgF9N6beyPp";
    public static final String google_android_key ="AIzaSyBi3qVakaNh185b2cmgFsuhBntGtA2SLLA";
    public static final String google_server_key ="AIzaSyCCm_dEtdfR12jlhi6REsenrgKwjLKS_iQ";

    public static final String GOOGLE_BASE_URL = "https://www.googleapis.com/";
    public static final String GOOGLE_AUTH = "oauth2/v4/token";
    public static final String SOCIAL_LOGIN = "api/v1/login/social";
    public static final String REQUEST_VERIFICATION_CODE = "api/v1/login/request-code";
    public static final String VERIFY_PHONE = "api/v1/login/verify";
    public static final String UPDATE_USER_PROFILE = "api/v1/profile";
    public static final String CHANGE_LANGUAGE = "api/v1/lang";
    public static final String GET_PAGES = "api/v1/pages";
    public static final String CHANGE_PHONE_REQUEST_CODE = "api/v1/profile/mobile/request-code";
    public static final String CHANGE_PHONE_VERIFY_CODE = "api/v1/profile/mobile/verify";
    public static final String REFRESH_TOKEN = "api/v1/token/refresh";
    public static final String CONTACT_US = "api/v1/page/contact-us";
    public static final String MY_ADDRESSES = "api/v1/addresses";
    public static final String HOW_TO_USE = "api/v1/page/how-to-use";
    public static final String HOW_TO_BECOME_A_DELEGATE = "api/v1/page/how-to-become-a-delegate";
    public static final String UPLOAD_DELEGATE_IMAGES = "api/v1/delegates/images";
    public static final String SUBMIT_DELEGATE_REQUEST = "api/v1/delegates/requests";
    public static final String GET_CAR_DETAILS = "api/v1/profile/delegate-details";
    public static final String REGISTERDEVICE = "api/v1/profile/devices";
    public static final String GET_NOTIFICATION = "api/v1/profile/notifications";
    public static final String MARKREAD = "api/v1/profile/notifications/mark-as-read";
    public static final String GET_CATEGORIES = "api/v1/categories";
    public static final String GOOGLE_PLACES_BASE_URL = "https://maps.googleapis.com/";
    public static final String GET_NEAR_PLACES = "maps/api/place/nearbysearch/json";
    public static final String GET_STORE_WORKING_HOURES = "maps/api/place/details/json";
    public static final String ORDER_IMAGES = "api/v1/orders/images";
    public static final String CREATE_ORDER = "api/v1/orders";
    public static final String ORDER_VOICE = "api/v1/orders/voicenotes";
    public static final String CHANGE_DELEGATE_STATUS = "api/v1/profile/activity-status";
    public static final String GET_CURRENT_USER_ORDER = "api/v1/profile/orders/current";
    public static final String GET_History_USER_ORDER = "api/v1/profile/orders/history";
    public static final String GET_CURRENT_DELEGTE_ORDER = "api/v1/profile/delegate-orders/current";
    public static final String GET_History_DELEGTE_ORDER = "api/v1/profile/delegate-orders/history";
    public static final String GET_CUSTOMER_ORDER_DETAILS = "api/v1/profile/orders";
    public static final String GET_DELEGATE_ORDER_DETAILS = "api/v1/profile/delegate-orders";
    public static final String CANCELOFFER = "/cancel";
    public static final String ACCEPT_OFFER = "/accept";
    public static final String REJECT_OFFER = "/reject";
    public static final String DELEGATES_RESEARCH = "/research-delegates";
    public static final String GET_DIRECTIONS = "maps/api/directions/json";
    public static final String ADD_OFFER = "/offers";
    public static final String START_RIDE = "/start";
    public static final String PICK_ITEM = "/pick-item";
    public static final String DELIVER_ORDER = "/complete";
    public static final String RATE_ORDER = "/ratings";
    public static final String CHAT_IMAGES = "/chat/images";
    public static final String GET_AREAS = "api/v1/settings";
    public static final String USERS = "api/v1/users";
    public static final String GET_CUSTOMER_REVIEWS = "/reviews";
    public static final String GET_DELEGATE_REVIEWS = "/delegate-details/reviews";
    public static final String GET_COMPLAINTS = "api/v1/profile/complaints";
    public static final String SUBMIT_COMPLAINT = "/complaints";
    public static final String GET_HISTORY_TRANSACTION = "api/v1/profile/wallet/transactions";
    public static final String GET_WALLET_DETAILS = "api/v1/profile/wallet/details";
    public static final String GET_BANK_ACCOUNTS = "api/v1/bank-accounts";
    public static final String SEND_BANK_TRANSFER = "api/v1/profile/bank-transfer-requests";
    public static final String DEREGISTER_DEVICE = "api/v1/devices";
    public static final String GET_ORDER_CANCELATION_REASONS = "api/v1/settings/order-cancel-reasons";
    public static final String DELEGATE_ORDERS = "api/v1/delegate-orders";
    public static final String IGNORE_ORDER = "/ignore";
    public static final String GET_PRAYER_TIMES = "api/v1/settings/prayer-times";
    public static final String VALIDATE_COUPON = "api/v1/orders/validate-coupon";
    public static final String STORE_IMAGES = "images/stores/branches/";
    public static final String REGISTER_CARD_PAYMENT = "api/v1/payment-cards/pay/request";

    public static final String STC_BASE_URL = "https://b2btest.stcpay.com.sa/B2B.DirectPayment.WebApi/DirectPayment/V4/";
    public static final String STC_DirectPaymentAuthorize = "DirectPaymentAuthorize";
    public static final String STC_DirectPaymentConfirm = "DirectPaymentConfirm";
    public static final String STC_DirectPayment = "DirectPayment";
    public static final String STC_PaymentInquiry = "PaymentInquiry";

    public static final String STC_MERCHANT_BASE_URL = "https://b2btest.stcpay.com.sa/B2B.MerchantTransactionsWebApi/MerchantTransactions/v3/";
    public static final String STC_MERCHANT_PaymentInquiry = "PaymentInquiry";
    public static final String STC_MERCHANT_PaymentAuthorize = "PaymentAuthorize";
    public static final String STC_MERCHANT_MobilePaymentAuthorize = "MobilePaymentAuthorize";
    public static final String STC_MERCHANT_OnlinePaymentAuthorize = "OnlinePaymentAuthorize";
    public static final String STC_MERCHANT_CancelPaymentAuthorize = "CancelPaymentAuthorize";
    public static final String STC_MERCHANT_RefundPayment = "RefundPayment";
    public static final String STC_MERCHANT_ReversePayment = "ReversePayment";
    public static final String STC_MERCHANT_PaymentOrderInquiry = "PaymentOrderInquiry";
    public static final String STC_MERCHANT_PaymentOrderPay = "PaymentOrderPay";
    public static final String STC_MERCHANT_PaymentOrderCancel = "PaymentOrderCancel";

}
