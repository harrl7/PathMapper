package bit.com.pathmapper.Activities;

import android.app.Dialog;
import android.app.FragmentManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.GroundOverlayOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.maps.android.clustering.ClusterManager;

import java.util.List;

import bit.com.pathmapper.AlertDialogs.Easy;
import bit.com.pathmapper.AlertDialogs.Hard;
import bit.com.pathmapper.AlertDialogs.Hours;
import bit.com.pathmapper.AlertDialogs.Medium;
import bit.com.pathmapper.AlertDialogs.POI_Dialog;
import bit.com.pathmapper.AlertDialogs.Prohibited;
import bit.com.pathmapper.AlertDialogs.Season;
import bit.com.pathmapper.AlertDialogs.Statistics;
import bit.com.pathmapper.Models.ClusterMapMarker;
import bit.com.pathmapper.Models.Collection;
import bit.com.pathmapper.R;
import bit.com.pathmapper.Utilities.DB_Handler;
import bit.com.pathmapper.Utilities.KmlParser;

/**
 * Created by tsgar on 27/09/2016.
 */

public abstract class BaseMapActivity extends AppCompatActivity implements
        OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        com.google.android.gms.location.LocationListener
{

    private static final int locationPermissionsRequestCode = 42;
    private static final String TAG = BaseMapActivity.class.getSimpleName();

    private GoogleMap map;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private ClusterManager<ClusterMapMarker> mClusterManager;
    private KmlParser kmlParser;
    private Location lastLocation;

    private Button btnScan;


    Hours hoursAlert;
    Season seasonAlert;
    Statistics statisticAlert;
    Prohibited prohibitedAlert;
    Easy easyAlert;
    Medium mediumAlert;
    Hard hardAlert;

    protected int getLayoutID() { return  R.layout.map; }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        //Check if google services is currently installed
        if(googleServicesAvailable())
        {
            setContentView(getLayoutID());
            setUpMap();
        }
        else
        {
            Toast.makeText(this, "Please install Google Play Services", Toast.LENGTH_LONG).show();
        }

        // Scanner button
        btnScan = (Button) findViewById(R.id.btnScan);
        btnScan.setOnClickListener(new ButtonOpenScannerHandler());

    }

    // Scan button handler
    public class ButtonOpenScannerHandler implements View.OnClickListener
    {

        @Override
        public void onClick(View view)
        {
            Intent scanIntent = new Intent(BaseMapActivity.this, BarCodeReaderActivity.class);
            startActivity(scanIntent);
        }
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        setUpMap();
    }

    @Override
    public void onMapReady(GoogleMap gMap)
    {
        if (map != null)
        {
            return;
        }
        map = gMap;
        map.setMyLocationEnabled(true);
        map.getUiSettings().setTiltGesturesEnabled(false);
        map.setMinZoomPreference(16);
        map.setMaxZoomPreference(17);

        setClusterManager();

        LatLngBounds polyBounds = new LatLngBounds(
                new LatLng(-45.858595,170.518425),       // South west corner
                new LatLng(-45.857140,170.524462));      // North east corner
        map.setLatLngBoundsForCameraTarget(polyBounds);
        map.setOnMarkerClickListener(getManager());

        start();
        setOverlay();
        googleAPIConnection();
        //showClusters();

        if (checkPermission())
        {
            map.setMyLocationEnabled(true);
        }

        kmlParser = new KmlParser(map, this);
    }

    private void setUpMap()
    {
        ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMapAsync(this);
    }

    protected abstract void start();
    protected abstract void showClusters();
    protected abstract void showClustersByCollection(int collectionID);
    protected abstract void showNearClusters(Location location);

    protected GoogleMap getMap() {
        return map;
    }
    protected ClusterManager getManager() { return mClusterManager; }


    //Start of Menu functions
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.colour_menu_list, menu);
        for (int i = 0; i < menu.size(); i++)
        {
            menu.getItem(i).setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
        }

        MenuItem menuItem = menu.findItem(R.id.menu_item_collections);
        SubMenu subMenu = menuItem.getSubMenu();

        List <Collection> cl = new DB_Handler(this).getAllCollections();
        for (Collection col : cl)
        {
            String colName = col.getCollectionName();
            subMenu.add(1, col.getId(), 1, colName);
        }

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        String itemTitle = (String) item.getTitle();

        switch(itemTitle)
        {
            case "Hours":
                hoursAlert = new Hours();
                FragmentManager fm = getFragmentManager();
                hoursAlert.show(fm, "confirm");
                break;

            case "Seasonal Attractions":
                seasonAlert = new Season();
                FragmentManager fm2 = getFragmentManager();
                seasonAlert.show(fm2, "confirm");
                break;

            case "Garden Statistics":
                statisticAlert = new Statistics();
                FragmentManager fm3 = getFragmentManager();
                statisticAlert.show(fm3, "confirm");
                break;

            case "Prohibited Items":
                prohibitedAlert = new Prohibited();
                FragmentManager fm4 = getFragmentManager();
                prohibitedAlert.show(fm4, "confirm");
                break;

            case "Easy":
                kmlParser.RenderKmlPaths(R.raw.easy_path);
//                easyAlert = new Easy();
//                FragmentManager fm5 = getFragmentManager();
//                easyAlert.show(fm5, "confirm");
                break;

            case "Medium":
//                mediumAlert = new Medium();
//                FragmentManager fm6 = getFragmentManager();
//                mediumAlert.show(fm6, "confirm");
                break;

            case "Hard":
                kmlParser.RenderKmlPaths(R.raw.hard_path);
//                hardAlert = new Hard();
//                FragmentManager fm7 = getFragmentManager();
//                hardAlert.show(fm7, "confirm");
                break;

            case "Show All":
                showClusters();
                Toast.makeText(this, "Loading....", Toast.LENGTH_LONG).show();

                break;

            case "Road Map":
                map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                Toast.makeText(this, "Loading....", Toast.LENGTH_SHORT).show();

                break;

            case "Hybrid":
                map.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                Toast.makeText(this, "Loading....", Toast.LENGTH_SHORT).show();

                break;

            case "Satellite":
                map.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                Toast.makeText(this, "Loading....", Toast.LENGTH_SHORT).show();

                break;

            case "Terrain":
                map.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
                Toast.makeText(this, "Loading....", Toast.LENGTH_SHORT).show();

                break;

            default:
                int colID = item.getItemId();
                showClustersByCollection(colID);
                Toast.makeText(this, "Loading....", Toast.LENGTH_SHORT).show();
                break;
        }

        return true;
    }
    //End of Menu functions

    public void setOverlay()
    {
        //Set the bounds of where the overlay will be
        LatLngBounds polyBounds = new LatLngBounds(
                new LatLng(-45.865092,170.511513),       // South west corner
                new LatLng(-45.851950,170.531448));      // North east corner

        //Create the ground the groundoverlay options
        GroundOverlayOptions groundMap = new GroundOverlayOptions()
                .image(BitmapDescriptorFactory.fromResource(R.drawable.test))
                .positionFromBounds(polyBounds);

        //Set the overly to the map
        //map.addGroundOverlay(groundMap);
    }

    public void googleAPIConnection()
    {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        mGoogleApiClient.connect();
    }

    //Check the availability of Google Play Services on phone
    //May move to seperate class handling connections
    public boolean googleServicesAvailable()
    {
        GoogleApiAvailability api = GoogleApiAvailability.getInstance();
        int isAvailable = api.isGooglePlayServicesAvailable(this);
        if (isAvailable == ConnectionResult.SUCCESS)
        {
            return true;
        }
        else if (api.isUserResolvableError(isAvailable))
        {
            Dialog dialog = api.getErrorDialog(this, isAvailable, 0);
            dialog.show();
        }
        else
        {
            Toast.makeText(this, "Can't get Map", Toast.LENGTH_LONG).show();
        }
        return false;


    }
    @Override
    public void onConnected( Bundle bundle)
    {
        Log.i(TAG, "onConnected()");
        getLastKnownLocation();
    }

    @Override
    public void onConnectionSuspended(int i)
    {
        Log.w(TAG, "onConnectionSuspended()");
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult)
    {
        Log.w(TAG, "onConnectionFailed()");
    }

    // Get last known location
    private void getLastKnownLocation() {
        Log.d(TAG, "getLastKnownLocation()");
        if ( checkPermission() )
        {
            lastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            if ( lastLocation != null )
            {
                Log.i(TAG, "LasKnown location. " + "Long: " + lastLocation.getLongitude() + " | Lat: " + lastLocation.getLatitude());
                //writeLastLocation();
                startLocationUpdates();
            }
            else
            {
                Log.w(TAG, "No location retrieved yet");
                startLocationUpdates();
            }
        }
        else askPermission();
    }

    // Start location Updates
    private void startLocationUpdates()
    {
        Log.i(TAG, "startLocationUpdates()");
        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setSmallestDisplacement(10)
                .setInterval(5000);
        //.setFastestInterval(FASTEST_INTERVAL);

        if (checkPermission())
        {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }

    }

    //When location is changed
    @Override
    public void onLocationChanged(Location location)
    {
        Log.d(TAG, "onLocationChanged ["+location+"]");

        lastLocation = location;
        showNearClusters(location);
    }

    // Check for permission to access Location
    private boolean checkPermission()
    {
        Log.d(TAG, "checkPermission()");
        // Ask for permission if it wasn't granted yet
        return (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED );
    }

    // Asks for permission
    private void askPermission()
    {
        Log.d(TAG, "askPermission()");
        String[] permissionsIWant = new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION};

        ActivityCompat.requestPermissions(this, permissionsIWant, locationPermissionsRequestCode);
    }

    // Verify user's response of the permission requested
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        Log.d(TAG, "onRequestPermissionsResult()");
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch ( requestCode ) {
            case 42: {
                if ( grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED ){
                    // Permission granted
                    getLastKnownLocation();

                } else {
                    // Permission denied
                    permissionsDenied();
                }
                break;
            }
        }
    }

    // App cannot work without the permissions
    private void permissionsDenied()
    {
        Log.w(TAG, "permissionsDenied()");
    }

    public void setClusterManager()
    {
        mClusterManager = new ClusterManager<>(this, getMap());
        getMap().setOnCameraIdleListener(mClusterManager);
        mClusterManager.setOnClusterItemClickListener(new clusterListener());

    }

    private class clusterListener implements ClusterManager.OnClusterItemClickListener<ClusterMapMarker> {
        @Override
        public boolean onClusterItemClick(ClusterMapMarker clusterMapMarker) {
            POI_Dialog pD= new POI_Dialog();
            Bundle bundle = new Bundle();
            bundle.putInt("key", clusterMapMarker.getID());
            pD.setArguments(bundle);
            FragmentManager fm8 = getFragmentManager();
            pD.show(fm8, "confirm");
            return false;
        }
    }




}
