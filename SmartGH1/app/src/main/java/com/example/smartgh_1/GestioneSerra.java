package com.example.smartgh_1;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
//import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

public class GestioneSerra extends Fragment {

    private SeekBar sBar;
    private TextView tView;
    private int pval = 0;
    private HomeFragment.OnInteractionListener listener;

    @Override
    public void onAttach(@NonNull Context context) {

        if(context instanceof HomeFragment.OnInteractionListener){
            listener = (HomeFragment.OnInteractionListener) context;
        } else {
            throw new ClassCastException(context  +  HomeFragment.OnInteractionListener.class.getSimpleName());
        }
        super.onAttach(context);
    }
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_gestione_serra, container, false);
        sBar = view.findViewById(R.id.seekBar);
        tView = view.findViewById(R.id.textview);
        Button backHome = view.findViewById(R.id.backHome);
        backHome.setOnClickListener(view1 -> {
            String sval = (String) tView.getText(); //metto il valore della textview in una variabile, pronto da inviare al canale Bluetooth
            //torna indietro
            listener.backHome();
        });

        sBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {


            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                pval = progress;
                updateTextView();
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                //showToast("Ricordati di non esagerare con l'acqua!");
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                //showToast("hai finito?");
            }
        });

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        GestioneSerraViewModel mViewModel = new ViewModelProvider(this).get(GestioneSerraViewModel.class);// TODO: Usa il ViewModel come necessario

    }

    private void updateTextView() {
        String progressText = getString(R.string.seekbar_progress, pval, sBar.getMax());
        tView.setText(progressText);
    }

    /*private void showToast(String message) {
        // Metodo di utilità per mostrare un messaggio Toast
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
    }*/
}
