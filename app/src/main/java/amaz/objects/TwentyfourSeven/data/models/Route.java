package amaz.objects.TwentyfourSeven.data.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Route {

    @SerializedName("overview_polyline")
    @Expose
    private RoutePolyLine routePolyLine;

    @SerializedName("legs")
    @Expose
    private ArrayList<DirectionLeg> legs;

    public RoutePolyLine getRoutePolyLine() {
        return routePolyLine;
    }

    public void setRoutePolyLine(RoutePolyLine routePolyLine) {
        this.routePolyLine = routePolyLine;
    }

    public ArrayList<DirectionLeg> getLegs() {
        return legs;
    }

    public void setLegs(ArrayList<DirectionLeg> legs) {
        this.legs = legs;
    }
}
