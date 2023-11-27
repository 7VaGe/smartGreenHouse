package com.example.smartgh_1;


import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.app.Activity;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;


import com.example.smartgh_1.utils.C;

import java.util.UUID;

import unibo.btlib.BluetoothChannel;
import unibo.btlib.BluetoothUtils;
import unibo.btlib.ConnectToBluetoothServerTask;
import unibo.btlib.ConnectionTask;
import unibo.btlib.RealBluetoothChannel;
import unibo.btlib.exceptions.BluetoothDeviceNotFound;

public class MainActivity extends AppCompatActivity implements HomeFragment.OnInteractionListener {

    private BluetoothChannel btChannel;

private ActivityResultLauncher<Intent> firstActivityResultLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



      /*  final BluetoothAdapter btAdapter = BluetoothAdapter.getDefaultAdapter();

        if (btAdapter != null && !btAdapter.isEnabled()) {
             Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
           if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            startActivityForResult(enableIntent, C.Bluetooth.ENABLE_BT_REQUEST);
            //startActivityForResult(new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE), C.Bluetooth.ENABLE_BT_REQUEST);

            // Register the permissions callback, which handles the user's response to the
            // system permissions dialog. Save the return value, an instance of
            // ActivityResultLauncher, as an instance variable.

        }*/
        }


        private void initUI () {
            findViewById(R.id.btBtn).setOnClickListener(v -> {
                try {
                    goToSmartGreenHouse();
                    connectToBTServer();
                } catch (BluetoothDeviceNotFound bluetoothDeviceNotFound) {
                    bluetoothDeviceNotFound.printStackTrace();
                }
            });

        }


        public void connectToBTServer () throws BluetoothDeviceNotFound {
            final BluetoothDevice serverDevice = BluetoothUtils.getPairedDeviceByName(C.Bluetooth.BT_DEVICE_ACTING_AS_SERVER_NAME);

            // !!! UTILIZZARE IL CORRETTO VALORE DI UUID
            final UUID uuid = BluetoothUtils.getEmbeddedDeviceDefaultUuid();
            //final UUID uuid = BluetoothUtils.generateUuidFromString(C.bluetooth.BT_SERVER_UUID);

            new ConnectToBluetoothServerTask(serverDevice, uuid, new ConnectionTask.EventListener() {
                @Override
                public void onConnectionActive(final BluetoothChannel channel) {

                    /*((TextView) findViewById(R.id.statusLabel)).setText(String.format("Status : connected to server on device %s",
                            serverDevice.getName()));*/

                    findViewById(R.id.btBtn).setEnabled(false);

                    btChannel = channel;
                    btChannel.registerListener(new RealBluetoothChannel.Listener() {
                        @Override
                        public void onMessageReceived(String receivedMessage) {
                            /*
                                    QUANDO RICEVI UN MESSAGGIO
                            ((TextView) findViewById(R.id.chatLabel)).append(String.format("> [RECEIVED from %s] %s\n",
                                    btChannel.getRemoteDeviceName(),
                                    receivedMessage));*/
                        }

                        @Override
                        public void onMessageSent(String sentMessage) {
                            /*
                                    QUANDO INVII UN MESSAGGIO DA TELEFONO
                            ((TextView) findViewById(R.id.chatLabel)).append(String.format("> [SENT to %s] %s\n",
                                    btChannel.getRemoteDeviceName(),
                                    sentMessage));*/
                        }
                    });
                }

                @Override
                public void onConnectionCanceled() {
                   /*
                            DI CHE NON SEI CONNESSO, O RITORNA BACK.
                   ((TextView) findViewById(R.id.statusLabel)).setText(String.format("Status : unable to connect, device %s not found!",
                            C.bluetooth.BT_DEVICE_ACTING_AS_SERVER_NAME)); */
                }


            });
        }/*
         * Qui eseguiamo il cambio di frammento, ora vedo come eseguirlo, il metodo viene invocato dal frammento
         *  ho inserito la transazione dentro alla funzione di connessione al server.
         * */

        @Override
        public void goToSmartGreenHouse () {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragmentContainerView, GestioneSerra.class, null)
                    .setReorderingAllowed(true)
                    .commit();

        }
        /*
         * Qui eseguiamo il cambio di frammento, ora vedo come eseguirlo, il metodo viene invocato dal frammento
         *
         * */

        public void backHome () {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragmentContainerView, HomeFragment.class, null)
                    .setReorderingAllowed(true)
                    .commit();
        }

        public void connectToBt(){
            if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.BLUETOOTH_CONNECT) ==
                    PackageManager.PERMISSION_GRANTED) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.BLUETOOTH_CONNECT}, 2);

                    firstActivityResultLauncher = registerForActivityResult(
                            new ActivityResultContracts.StartActivityForResult(),
                            result -> {
                                if (result.getResultCode() == Activity.RESULT_OK) {
                                    Intent btEnable = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                                    firstActivityResultLauncher.launch(btEnable);
                                    initUI();
                                } else if (result.getResultCode() == Activity.RESULT_CANCELED) {
                                    Log.e("Cancelled", "Cancelled");
                                    new DialogFragment().show(getSupportFragmentManager(), "DIALOG");
                                }
                            });
                    // You can use the API that requires the permission.
                } else if (ActivityCompat.shouldShowRequestPermissionRationale(
                        MainActivity.this, Manifest.permission.BLUETOOTH_CONNECT)) {
                    Log.e("Cancelled", "Cancelled");
                }

        }

    }
}