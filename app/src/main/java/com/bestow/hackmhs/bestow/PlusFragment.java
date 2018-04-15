package com.bestow.hackmhs.bestow;

import android.Manifest;
import android.app.Fragment;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.content.Context;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import clarifai2.api.ClarifaiBuilder;
import clarifai2.api.ClarifaiClient;
import clarifai2.dto.input.ClarifaiInput;
import clarifai2.dto.model.output.ClarifaiOutput;
import clarifai2.dto.prediction.Concept;

public class PlusFragment extends Fragment implements LocationListener{

    LocationManager locationManager;
    static final int REQUEST_LOCATION = 1;
    Bitmap bitmap;
    double latitude = 0;
    double longitude = 0;
    String town="";
    JSONObject GPSjsonObject;

    EditText editDescription;
    ImageView pictureTaken;
    Button takePic, post;
    private static final int CAMERA_REQUEST_CODE = 10;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_plus,null);

        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        getLocation();

        pictureTaken = view.findViewById(R.id.PlusFragment_ImageView_PictureTaken);
        takePic = view.findViewById(R.id.PlusFragment_Button_TakePic);
        post = view.findViewById(R.id.PlusFragment_Button_PostButton);
        editDescription = view.findViewById(R.id.PlusFragment_EditText_EditDesc);
            post.setVisibility(View.INVISIBLE);
            editDescription.setVisibility(View.INVISIBLE);

        takePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                captureImg();
                post.setVisibility(View.VISIBLE);
                editDescription.setVisibility(View.VISIBLE);
            }
        });
        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(editDescription.getText().toString().length()==0){
                    Toast.makeText(getActivity(), "Please Enter a Description", Toast.LENGTH_SHORT).show();
                }else{
                    FirebaseDatabase firebaseDatabase =  FirebaseDatabase.getInstance();
                    DatabaseReference drItems = firebaseDatabase.getReference("items");
                    drItems.child(drItems.push().getKey()).setValue("Shivens sister has a fat ass");
                    Toast.makeText(getActivity(), "Item Added", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;
    }

    public void storeItemFirebase(){
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

        String name = firebaseUser.getDisplayName();
        String description = editDescription.getText().toString();

        Item item = new Item( name, description, town, bitmap);
        Item itemUpload = new Item (item.getUsername(), item.getDescription(), item.getCity(), "");

        FirebaseDatabase firebaseDatabase =  FirebaseDatabase.getInstance();
        DatabaseReference drItems = firebaseDatabase.getReference("items");
        drItems.child(drItems.push().getKey()).setValue("Hello-world");
    }

    public void captureImg(){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, CAMERA_REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        bitmap = (Bitmap)data.getExtras().get("data");
        pictureTaken.setImageBitmap(bitmap);
    }

    private void getLocation() {

        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[] {Manifest.permission.ACCESS_FINE_LOCATION},REQUEST_LOCATION);
        }
        else {
            Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000, 0, this);
            if (location != null) {
                latitude = location.getLatitude();
                longitude = location.getLongitude();
            }else{
                Toast.makeText(getActivity(),"Unable to aquire location. Try again in a minute",Toast.LENGTH_LONG).show();
            }
            GPSAsyncThread GPSThread = new GPSAsyncThread();
            GPSThread.execute();
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode){
            case REQUEST_LOCATION:
                getLocation();
                break;
            case CAMERA_REQUEST_CODE:
                captureImg();
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

    public class GPSAsyncThread extends AsyncTask<Void, Void, Void>{

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                URL url = new URL("https://maps.googleapis.com/maps/api/geocode/json?latlng=" + latitude + "," + longitude + "&key=AIzaSyDxkprNaAGCP6GZ-HIJIIYTyZzVNWeBm2w");
                URLConnection urlConnection = url.openConnection();
                InputStream inputStream = urlConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                String input = bufferedReader.readLine();
                String line = "";

                while (line != null){
                    line = bufferedReader.readLine();
                    input += line;
                }

                GPSjsonObject = new JSONObject(input);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            try {
                town = GPSjsonObject.getJSONArray("results").getJSONObject(1).getString("formatted_address");

                // town is the address that you have to send to firebase
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

    public class ClarifaiThread extends AsyncTask<Void, Void, Void>{
        @Override
        protected Void doInBackground(Void... voids) {
            ClarifaiClient client = new ClarifaiBuilder("e1689dff436e4335b7f3e799ba4c2fc2").buildSync();
            final List<ClarifaiOutput<Concept>> predictionResults =
                    client.getDefaultModels().generalModel().predict()
                            .withInputs(ClarifaiInput.forImage("https://img.michaels.com/L6/3/IOGLO/873402639/212485006/10186027_r.jpg?fit=inside|1024:1024"))
                            .executeSync().get();

            /*final List<ClarifaiOutput<Concept>> predictionResults =
                    client.getDefaultModels().generalModel().predict()
                    .withInputs(ClarifaiInput.forImage(new File("/home/user/image.jpeg")))
                    .executeSync();*/

            Log.d("debugging",predictionResults.get(0).toString());
            String result = predictionResults.toString();
            result = result.substring(result.indexOf("data=["),result.indexOf("}],"));
            Log.d("debugging",result);
            ArrayList<String> options = new ArrayList<>();
            for(int i=0; i<5; i++){
                result=result.substring(result.indexOf("name=")+5);
                options.add(result.substring(0,result.indexOf(",")));
            }
            Log.d("debugging",options.toString());
            return null;
        }
    }


}

