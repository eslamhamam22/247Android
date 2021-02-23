package amaz.objects.TwentyfourSeven.injection;

import amaz.objects.TwentyfourSeven.data.repositories.NetworkGeneralRepository;
import amaz.objects.TwentyfourSeven.data.repositories.NetworkGoogleRepository;
import amaz.objects.TwentyfourSeven.data.repositories.NetworkOrderRepository;
import amaz.objects.TwentyfourSeven.data.repositories.NetworkPaymentRepository;
import amaz.objects.TwentyfourSeven.data.repositories.NetworkUserRepository;
import amaz.objects.TwentyfourSeven.data.repositories.OrderRepository;
import amaz.objects.TwentyfourSeven.data.repositories.PaymentRepository;
import amaz.objects.TwentyfourSeven.data.repositories.UserRepository;

public class Injection {

    public static UserRepository provideUserRepository(){
        return new NetworkUserRepository();
    }

    public static PaymentRepository providePaymentRepository(){
        return new NetworkPaymentRepository();
    }

    public static OrderRepository provideOrderRepository(){
        return new NetworkOrderRepository();
    }

    public static NetworkGoogleRepository provideGoogleRepository(){
        return new NetworkGoogleRepository();
    }

    public static NetworkGeneralRepository provideGeneralRepository(){
        return new NetworkGeneralRepository();
    }
}
