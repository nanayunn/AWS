package com.example.p502;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class ThirdActivity extends AppCompatActivity {
    Item item;
    GoogleMap mMap;
    SupportMapFragment supportMapFragment;
    Intent intent;
    Marker coupon;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third);
        supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);

        intent = getIntent();
        item = (Item) intent.getSerializableExtra("loc");

        supportMapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                mMap = googleMap;

                LatLng mc = new LatLng(item.getLeti(), item.getLongti());
                coupon = mMap.addMarker(new MarkerOptions().position(mc).title("coupon"));
                mMap.addMarker(new MarkerOptions().position(mc).title(item.getName()));
                Log.d("----test","mc Marker & coupon");


                mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                    @Override
                    public boolean onMarkerClick(Marker marker) {
                        Thread t = new Thread(new Runnable() {

                            public void run() {
                                int i = 20;
                                while (i > 0) {
                                    final int ran = (int)(Math.random()*10) + 1;
                                    runOnUiThread(new Runnable() {
                                        public void run() {
                                            LatLng latLng = new LatLng(item.getLeti(), item.getLongti()+ran); //최초 시작했을 때 지도의 위치
                                            mMap.addMarker(new MarkerOptions().position(latLng).title("coupon"));
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


        });


    }


}
