package com.bestow.hackmhs.bestow;

import android.Manifest;
import android.app.Fragment;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class PlusFragment extends Fragment implements LocationListener{

    LocationManager locationManager;
    static final int REQUEST_LOCATION = 1;
    double latitude = 0;
    double longitude = 0;
    TextView latText;
    TextView longText;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_plus,null);
        latText = view.findViewById(R.id.PlusFragment_TextView_latText);
        longText = view.findViewById(R.id.PlusFragment_TextView_longText);

        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        getLocation();

        return view;
    }

    private void getLocation() {

        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(getActivity(), new String[] {Manifest.permission.ACCESS_FINE_LOCATION},REQUEST_LOCATION);
        }
        else {
            Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000, 0, this);
            latitude = location.getLatitude();
            longitude = location.getLatitude();
            latText.setText(latitude+"");
            longText.setText(longitude+"");
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode){
            case REQUEST_LOCATION:
                getLocation();
                break;
        }
    }


    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }
}

