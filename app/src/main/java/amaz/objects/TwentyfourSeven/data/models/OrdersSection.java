package amaz.objects.TwentyfourSeven.data.models;

import java.util.ArrayList;


public class OrdersSection {

    private ArrayList<Order> orders;
    private String sectionTitle;

    public OrdersSection() {

    }

    public OrdersSection(String sectionTitle, ArrayList<Order> orders) {
        this.sectionTitle = sectionTitle;
        this.orders = orders;
    }

    public ArrayList<Order> getChildItems() {
        return orders;
    }

    public String getSectionTitle() {
        return sectionTitle;
    }

    public void setSectionProducts(ArrayList<Order> orders) {
        this.orders = orders;
    }

    public void setSectionTitle(String sectionTitle) {
        this.sectionTitle = sectionTitle;
    }
}
