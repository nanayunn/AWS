package com.example.p502;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.Random;

import static android.app.PendingIntent.getActivity;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    Intent intent;
    Item item;
    LocationManager locationManager;
    Thread t;
    ArrayList<Marker> array_marker = new ArrayList<>();
    int i = 10;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        intent = getIntent();
        item = (Item) intent.getSerializableExtra("loc");
        String[] permitions =
                {
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                };
        ActivityCompat.requestPermissions(this, permitions, 101);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        MyLocation myLocation = new MyLocation();
        long minTime = 1000;
        float minDistance = 0;
        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        locationManager.requestLocationUpdates(
                LocationManager.NETWORK_PROVIDER,
                minTime,
                minDistance,
                myLocation
        );





    }

    private com.example.p502.Location startLocationService() {
        Log.d("---", "startLocationService");
        try {
            Location location = null;
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                //GPS는 avd 용 network는 핸드폰 용
                Log.d("---", "checkSelfPermission");
                double lat = location.getLatitude();
                double lon = location.getLongitude();
                Log.d("---", lat + "," + lon);
                com.example.p502.Location myloc = new com.example.p502.Location(lat, lon);
                return myloc;

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    class MyLocation implements LocationListener {


        @Override
        public void onLocationChanged(Location location) {
            double lat = location.getLatitude();
            double lon = location.getLongitude();

        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */



    @Override
    public void onMapReady(final GoogleMap googleMap) {

        mMap = googleMap;

        LatLng mc = new LatLng(item.getLeti(), item.getLongti());
//        coupon = mMap.addMarker(new MarkerOptions().position(
//                new LatLng(item.getLeti()+new Random().nextDouble(),item.getLongti()+new Random().nextDouble())).title("coupon"));
        mMap.addMarker(new MarkerOptions().position(mc).title(item.getName()));
        Log.d("----test","mc Marker & coupon");

        final Handler handler = new Handler();
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {

                Thread t = new Thread(new Runnable() {
                    public void run() {
                        while (i > 0) {
                            final Random random = new Random();
                            final float ran = ((float) (random.nextInt()*0.000000000003));
                            final float rsn = ((float) (random.nextInt()*0.000000000002));
                            runOnUiThread(new Runnable() {
                                public void run() {
                                    LatLng latLng = new LatLng(item.getLeti()+rsn, item.getLongti()+ran);
                                    array_marker.add(mMap.addMarker(new MarkerOptions().position(latLng).title("coupon")));
                                    for(int i = 0; i < array_marker.size()-1; i++)
                                    {
                                        Marker tmp =  array_marker.get(i);
                                        tmp.setVisible(false);
                                    }
                                    mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                                        @Override
                                        public boolean onMarkerClick(Marker marker) {
                                            Toast.makeText(getApplicationContext(), "쿠폰 획득!!!",Toast.LENGTH_SHORT).show();
                                            for(int i = 0; i < array_marker.size(); i++)
                                            {
                                                Marker tmp =  array_marker.get(i);
                                                tmp.remove();
                                            }
                                            array_marker.clear();
                                            i = 0;
                                            return false;
                                        }
                                    });
                                }
                            });
                            try {
                                Thread.sleep(500);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            i--;
                        }
                    }
                });
                t.start();


                return false;
            }
        });
        Log.d("----test",mc.toString()+" mc value");
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mc, 14));

        mMap.setMyLocationEnabled(true);
    }
}
