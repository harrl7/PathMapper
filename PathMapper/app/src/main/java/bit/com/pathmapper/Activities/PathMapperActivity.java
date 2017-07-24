package bit.com.pathmapper.Activities;


import android.location.Location;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

import bit.com.pathmapper.Interfaces.IMarkers;
import bit.com.pathmapper.Interfaces.IPaths;
import bit.com.pathmapper.Models.ClusterMapMarker;
import bit.com.pathmapper.Models.Collection;
import bit.com.pathmapper.Models.PointOfInterest;
import bit.com.pathmapper.Utilities.DB_Handler;
import bit.com.pathmapper.Utilities.KmlParser;
import bit.com.pathmapper.Utilities.LocationChecker;


/**
 * Created by tsgar on 27/09/2016.
 */

public class PathMapperActivity extends BaseMapActivity implements IMarkers, IPaths{



    private GoogleMap gMap;
    private ArrayList<Collection> collectionArray;


    //Extends BaseMapActivity
    //Main logic for markers and paths / will create and use the utilities classes as necessary.
    //Subject to changes based on size and how paths and markers interact.

    //TODO create and add interface for paths
    //TODO create and add interface for markers
    //TODO map scan button


    @Override
    protected void start() {
        //Should start the map over the gardens information center.
        gMap = getMap();

        getMap().moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(-45.856637, 170.518787), 15));

//        KmlParser kmlParser = new KmlParser(gMap, this); //Initialize the KmlParser Class and pass it the map and the app context.
//        kmlParser.RenderKmlPaths(); //Call the wrapper render function.

        //Creation of collectionsArray-------------
        DB_Handler db = new DB_Handler(getApplicationContext());
        collectionArray = new ArrayList<>();

        List <Collection> cl = new DB_Handler(this).getAllCollections();
        for (Collection col : cl)
        {
            List<PointOfInterest> points = db.getAllCollectionPOI(col.getId());
            Collection collection = new Collection(col.getId(),col.getCollectionName(), points);
            collectionArray.add(collection);
        }
        //End of creation---------------------------

    }

    @Override
    protected void showClusters()
    {

        getManager().clearItems();
        List<ClusterMapMarker> items = new ArrayList<ClusterMapMarker>();

        for (Collection col : collectionArray)
        {
            List<PointOfInterest> points = col.getPoints();

            for (PointOfInterest poi : points)
            {
                items.add(new ClusterMapMarker(poi.getId(), poi.getLat(), poi.getLng()));
            }
        }

        getManager().addItems(items);
    }

    @Override
    protected void showClustersByCollection(int collectionID)
    {
        getManager().clearItems();
        List<ClusterMapMarker> items = new ArrayList<ClusterMapMarker>();

        for (Collection col : collectionArray)
        {
            if(col.getId() == collectionID)
            {
                List<PointOfInterest> points = col.getPoints();

                for (PointOfInterest poi : points)
                {
                    items.add(new ClusterMapMarker(poi.getId(), poi.getLat(), poi.getLng()));
                }
            }

        }

        getManager().addItems(items);
    }

    @Override
    protected void showNearClusters(Location location)
    {
        LocationChecker lChecker = new LocationChecker();
        List<ClusterMapMarker> items = lChecker.checkNearby(location, getMap(), getApplicationContext(), collectionArray);
        //getManager().clearItems();
        getManager().addItems(items);
    }




}
