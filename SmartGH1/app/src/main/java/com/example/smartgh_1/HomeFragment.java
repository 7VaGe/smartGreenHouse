package com.example.smartgh_1;



import android.Manifest;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.dynamic.SupportFragmentWrapper;

import unibo.btlib.exceptions.BluetoothDeviceNotFound;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    private ActivityResultLauncher<Intent> firstActivityResultLauncher;
    public HomeFragment() {
    }

    // TODO: Rename parameter arguments, choose names that match
    /* the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(/*String param1, String param2*/) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
       /* args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);*/
        return fragment;
    }

    public interface OnInteractionListener{
        void goToSmartGreenHouse();

        void backHome();

        void connectToBt();


        }

    private OnInteractionListener listener;

    @Override
    public void onAttach(@NonNull Context context) {

        if(context instanceof OnInteractionListener){
            listener = (OnInteractionListener) context;
        } else {
            throw new ClassCastException(context  +  OnInteractionListener.class.getSimpleName());
        }
        super.onAttach(context);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    /*private void showToast(String message) {
        // Metodo di utilità per mostrare un messaggio Toast
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
    }*/
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);


       Button btBtn = (Button) view.findViewById(R.id.btBtn);
        btBtn.setOnClickListener(view1 -> {
            //showToast("Hai premuto!");
            listener.connectToBt();
            listener.goToSmartGreenHouse();
        });
        return view;
    }
}