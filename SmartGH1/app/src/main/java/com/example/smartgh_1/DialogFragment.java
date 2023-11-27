package com.example.smartgh_1;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import com.google.android.gms.dynamic.SupportFragmentWrapper;


public class DialogFragment extends androidx.fragment.app.DialogFragment {




    public static class StartGameDialogFragment extends DialogFragment {
        @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the Builder class for convenient dialog construction.
            AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
            builder.setMessage(R.string.dialog_bt_denied)
                    .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.dismiss();
                        }
                    });

            return builder.create();
        }
    }
}
