package amaz.objects.TwentyfourSeven.utilities;

import android.content.Context;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.PolyUtil;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import amaz.objects.TwentyfourSeven.data.models.BlockedArea;

public class AreasUtility {


    public static boolean checkLocationInBlockedArea(Context context,LatLng point) {
      LocalSettings localSettings = new LocalSettings(context);

        if (localSettings.getBlockedAreas() != null) {
            ArrayList<BlockedArea> blockedAreas = new ArrayList<BlockedArea>(Arrays.asList(localSettings.getBlockedAreas()));
            ArrayList<String> blockedPaths = new ArrayList<>();
            for (int i = 0; i < blockedAreas.size(); i++) {
                blockedPaths.add(blockedAreas.get(i).getEncoded_path());
            }
            for (String polyline : blockedPaths) {
                List<LatLng> decoded;
                try {
                    decoded = PolyUtil.decode(polyline);
                    if (PolyUtil.containsLocation(point, decoded, false)) {
                        return true;
                    }
                }
                catch (StringIndexOutOfBoundsException e){
                    Log.e("catch","StringIndexOutOfBoundsException");
                    return false;
                }
            }
            return false;
        }
        return false;

    }
}
