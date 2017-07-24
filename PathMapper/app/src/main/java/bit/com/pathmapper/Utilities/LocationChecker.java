package bit.com.pathmapper.Utilities;

import android.content.Context;
import android.location.Location;

import com.google.android.gms.maps.GoogleMap;

import java.util.ArrayList;
import java.util.List;

import bit.com.pathmapper.Models.ClusterMapMarker;
import bit.com.pathmapper.Models.Collection;
import bit.com.pathmapper.Models.PointOfInterest;


/**
 * Created by tsgar on 28/09/2016.
 */

public class LocationChecker
{

    public List<ClusterMapMarker> checkNearby(Location location, GoogleMap map, Context context,ArrayList<Collection> collectionArray)
    {

        List<ClusterMapMarker> items = new ArrayList<ClusterMapMarker>();

        for (Collection col : collectionArray)
        {
            List<PointOfInterest> points = col.getPoints();

            for (PointOfInterest poi : points)
            {
                double lat = poi.getLat();
                double lng = poi.getLng();
                Location too = new Location("Location B");
                too.setLatitude(lat);
                too.setLongitude(lng);

                double distance=location.distanceTo(too);


                if (distance < 50)
                {
                    items.add(new ClusterMapMarker(poi.getId(), lat, lng));
                }
            }
        }

        return items;
    }

    //Location checker logic for getting the users location and showing the markers
    //Called by the PathMapperActivity





}
