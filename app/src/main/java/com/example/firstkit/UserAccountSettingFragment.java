package com.example.firstkit;


import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.firstkit.Prevelant.Prevelant;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.RectangularBounds;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Arrays;
import java.util.HashMap;
import java.util.regex.Pattern;

import static android.support.constraint.Constraints.TAG;


/**
 * A simple {@link Fragment} subclass.
 */
public class UserAccountSettingFragment extends Fragment {

    private Button SubmitDetails;
    private String Address , LocationName , LatLngUser, FullAddress;
    private EditText Name  , Email , AlternateMobile , Landmark;
    private TextView address , userphone;
    String apiKey = "AIzaSyCS09VOWgpy0Mp7I1SGy7DpYnbvbjodS4k";
    PlacesClient placesClient;
    private AlertDialog dialog;
    DatabaseReference addUserDetails;

    public UserAccountSettingFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_user_account_setting, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        Name = (EditText) getView().findViewById(R.id.UserName);
        Email = (EditText) getView().findViewById(R.id.email_address);
        AlternateMobile = (EditText) getView().findViewById(R.id.alternate_mobile);
        address = (TextView) getView().findViewById(R.id.UserAddress1);
        Landmark = (EditText) getView().findViewById(R.id.Landmark);
        userphone = (TextView) getView().findViewById(R.id.UserPhone);


        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setCancelable(false); // if you want user to wait for some process to finish,
        builder.setView(R.layout.loading_dialog);
        dialog = builder.create();

        SubmitDetails = (Button) getView().findViewById(R.id.SubmitDetailsUser);
        addUserDetails = FirebaseDatabase.getInstance().getReference();

        extractDataFromUserModel();

        SubmitDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.show();
                InsertdetailsInDB();

            }
        });



        if(!Places.isInitialized()) {
            Places.initialize(getActivity() , apiKey);
        }
        placesClient = Places.createClient(getActivity());
        final AutocompleteSupportFragment autocompleteFragment = (AutocompleteSupportFragment) getChildFragmentManager().findFragmentById(R.id.place_autocomplete);

        // Specify the types of place data to return.
        autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.LAT_LNG, Place.Field.NAME, Place.Field.ADDRESS));

        autocompleteFragment.setHint("delivery address");


        ////Setting bounds for search
        ///18.6935602   73.8096359
        //
        //18.6035602   73.7196359

        RectangularBounds bounds = RectangularBounds.newInstance(new LatLng(18.6035602, 73.7196359), new LatLng(18.6935602, 73.8096359));


        autocompleteFragment.setLocationRestriction(bounds);
        // Set up a PlaceSelectionListener to handle the response.
        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                // TODO: Get info about the selected place.
                Log.i(TAG, "Place: " + place.getName() + ", " + place.getId() + ", " + place.getAddress() + ", " + place.getLatLng());
                Address = place.getAddress();
                LocationName = place.getName();
                LatLngUser = place.getLatLng().toString();
                FullAddress = LocationName + '\n' + Address + '\n' + LatLngUser ;
                address.setText(LocationName + '\n' + Address);

            }

            @Override
            public void onError(Status status) {
                // TODO: Handle the error.
                Log.i(TAG, "An error occurred: " + status);
            }
        });


    }



    private void extractDataFromUserModel()
    {

        Name.setText(Prevelant.currentOnlineUser.getName());
        userphone.setText(Prevelant.currentOnlineUser.getPhone());
        if(!Prevelant.currentOnlineUser.getEmail().isEmpty())
        { Email.setText(Prevelant.currentOnlineUser.getEmail()); }
        if(!Prevelant.currentOnlineUser.getAlternatemobile().isEmpty())
        { AlternateMobile.setText(Prevelant.currentOnlineUser.getAlternatemobile());}
        if(Prevelant.currentOnlineUser.getAddress()!=null){
            address.setText(Prevelant.currentOnlineUser.getAddress());
        }

    }



    private void InsertdetailsInDB() {

        if(Name.getText().toString().isEmpty()){
            dialog.dismiss();
            Name.setError("Required");
            Name.requestFocus();
            return;
        }



        else if(!isValidEmailId(Email.getText().toString().trim())&&!Email.getText().toString().isEmpty()){
            dialog.dismiss();
            Email.setError("Invalid email type");
            return;
        }
        else if(AlternateMobile.getText().toString().length()!=10&&!AlternateMobile.getText().toString().isEmpty()){
            dialog.dismiss();
            AlternateMobile.setError("Invalid mobile number");
            return;
        }

        HashMap<String , Object> Details = new HashMap<>();
        Details.put("name" , Name.getText().toString());
        Details.put("address" , FullAddress +'\n'+ Landmark.getText().toString());
        Details.put("alternatemobile" , AlternateMobile.getText().toString());
        Details.put("Email" , Email.getText().toString());

        addUserDetails.child("Users").child(Prevelant.currentOnlineUser.getPhone()).updateChildren(Details)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        dialog.dismiss();
                        Prevelant.currentOnlineUser.setAddress(address.getText().toString()+"\nRoom number/Landmark  "+Landmark.getText().toString());
                        Prevelant.currentOnlineUser.setName(Name.getText().toString());
                        Prevelant.currentOnlineUser.setAlternatemobile(AlternateMobile.getText().toString());
                        Prevelant.currentOnlineUser.setEmail(Email.getText().toString());
                        Toast.makeText(getActivity(), "lets check", Toast.LENGTH_SHORT).show();
                    }
                });
    }


    //////from internet to valid email text
    private boolean isValidEmailId(String email){

        return Pattern.compile("^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
                + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
                + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$").matcher(email).matches();
    }
}
