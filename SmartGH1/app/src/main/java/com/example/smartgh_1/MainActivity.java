package com.example.smartgh_1;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
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
import android.os.AsyncTask;
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

    private static final int REQUEST_CODE = 1;
    private BluetoothChannel btChannel;
    private final BluetoothAdapter btAdapter = BluetoothAdapter.getDefaultAdapter();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (btAdapter == null) {
            Log.e("MyApp", "BT is not available on this device");
            finish();
            return;
        }

        ActivityResultLauncher<Intent> firstActivityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        checkPermissionsAndStartDiscovery();
                    } else if (result.getResultCode() == Activity.RESULT_CANCELED) {
                        Log.e("MyApp", "Bluetooth enabling cancelled by user");
                    }
                }
        );

        if (!btAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            firstActivityResultLauncher.launch(enableBtIntent);
        } else {
            checkPermissionsAndStartDiscovery();
        }
    }

    private void checkPermissionsAndStartDiscovery() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_SCAN) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) == PackageManager.PERMISSION_GRANTED) {
            startDiscovery();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{
                    //Dato che ho un dispositivo vecchio devo controllare i permessi di bluetoot normali, e non quelli avvenuti dopo Android 12 (SDK 31) come SCAN E CONNECT
                    Manifest.permission.BLUETOOTH,
                    Manifest.permission.BLUETOOTH_ADMIN
            }, REQUEST_CODE);
        }
    }

    private void startDiscovery() {
        try {
            if (btAdapter.isDiscovering()) {
                btAdapter.cancelDiscovery();
                Log.e("MyApp", "Stai già ricercando i dispositivi");
            }
            if (btAdapter.startDiscovery()) {
                Log.e("MyApp", "Scansione, e scegli il dispositivo con cui collegarti.");
            }
        } catch (SecurityException e) {
            Log.e("MyApp", "Permission not granted for discovery", e);
        }
    }

    public void connectToBTServer() throws BluetoothDeviceNotFound {
        final BluetoothDevice serverDevice = BluetoothUtils.getPairedDeviceByName(C.Bluetooth.BT_DEVICE_ACTING_AS_SERVER_NAME);

        if (serverDevice == null) {
            Log.e("MyApp", "Server device not found!");
            return;
        }

        final UUID uuid = BluetoothUtils.getEmbeddedDeviceDefaultUuid();
        Log.e("ConnectTO", "sei dentro a connectToBTServer");

        new ConnectToBluetoothServerTask(serverDevice, uuid, new ConnectionTask.EventListener() {
            @Override
            public void onConnectionActive(final BluetoothChannel channel) {
                Log.e("Connect", "Connesso al BT!");

                btChannel = channel;
                btChannel.registerListener(new RealBluetoothChannel.Listener() {
                    @Override
                    public void onMessageReceived(String receivedMessage) {
                        // Gestisci i messaggi ricevuti
                    }

                    @Override
                    public void onMessageSent(String sentMessage) {
                        // Gestisci i messaggi inviati
                    }
                });

                runOnUiThread(() -> {
                    findViewById(R.id.btBtn).setEnabled(false);
                    goToSmartGreenHouse();
                });
            }

            @Override
            public void onConnectionCanceled() {
                Log.e("Connect", "Unable to connect, device not found!");
            }
        }).execute();
    }

    @Override
    public void goToSmartGreenHouse() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragmentContainerView, GestioneSerra.class, null)
                .setReorderingAllowed(true)
                .commit();
    }

    public void backHome() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragmentContainerView, HomeFragment.class, null)
                .setReorderingAllowed(true)
                .commit();
    }

    public void connectToBt() throws BluetoothDeviceNotFound {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) == PackageManager.PERMISSION_GRANTED) {
            Log.e("Connect", "sei dentro a connectToBt, prima di passare a ConnectToBTSever");
            connectToBTServer();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.BLUETOOTH_ADMIN,
                    Manifest.permission.BLUETOOTH
            }, REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startDiscovery();
            } else {
                Log.e("MyApp", "Permission denied");
            }
        }
    }
}
