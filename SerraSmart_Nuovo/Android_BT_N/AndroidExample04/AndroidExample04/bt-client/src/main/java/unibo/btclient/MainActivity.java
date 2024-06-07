package unibo.btclient;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.UUID;

import unibo.btlib.BluetoothChannel;
import unibo.btlib.BluetoothUtils;
import unibo.btlib.ConnectToBluetoothServerTask;
import unibo.btlib.ConnectionTask;
import unibo.btlib.RealBluetoothChannel;
import unibo.btlib.exceptions.BluetoothDeviceNotFound;
import unibo.btclient.utils.C;

public class MainActivity extends AppCompatActivity {
    private BluetoothChannel btChannel;
    private int seekbarValue;
    private final static String PCLOSE = "f";
    private final static String btCONN = "B";
    private Button closeBtn;
    private Button connectBtn;
    private Button startBtn;
    private SeekBar sBar;
    private TextView seekBarText;
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
         closeBtn = findViewById(R.id.closeBtn);
         connectBtn = findViewById(R.id.connectBtn);
         startBtn = findViewById(R.id.startBtn);
         sBar = findViewById(R.id.seekBar);
         seekBarText = findViewById(R.id.seekBarText);

        final BluetoothAdapter btAdapter = BluetoothAdapter.getDefaultAdapter();

        if(btAdapter != null && !btAdapter.isEnabled()) {
            startActivityForResult(
                new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE),
                C.bluetooth.ENABLE_BT_REQUEST
            );
        }
        initUI();
    }

    private void initUI() {

        connectBtn.setOnClickListener(l -> {
            l.setEnabled(false);
            try {
                connectToBTServer();
            } catch (BluetoothDeviceNotFound bluetoothDeviceNotFound) {
                Toast.makeText(this, "Bluetooth device not found !", Toast.LENGTH_LONG)
                        .show();
                bluetoothDeviceNotFound.printStackTrace();
            } finally {
                l.setEnabled(true);
            }
        });

        startBtn.setOnClickListener(l -> {
            btChannel.sendMessage(btCONN);
            btChannel.sendMessage(String.valueOf(seekbarValue));
        });
        closeBtn.setOnClickListener(l -> {
            btChannel.sendMessage(PCLOSE);
            btChannel.close();
            ((TextView) findViewById(R.id.statusLabel)).setText(
                    "Status : not connected");
            startBtn.setEnabled(false);
            closeBtn.setEnabled(false);
            connectBtn.setEnabled(true);
            sBar.setEnabled(false);
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        btChannel.close();
    }

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode,
                                    @Nullable final Intent data) {
        if(requestCode == C.bluetooth.ENABLE_BT_REQUEST && resultCode == RESULT_OK) {
            Log.d(C.APP_LOG_TAG, "Bluetooth enabled!");
        }

        if(requestCode == C.bluetooth.ENABLE_BT_REQUEST && resultCode == RESULT_CANCELED) {
            Log.d(C.APP_LOG_TAG, "Bluetooth not enabled!");
        }
    }

    private void connectToBTServer() throws BluetoothDeviceNotFound {
        final BluetoothDevice serverDevice = BluetoothUtils
                .getPairedDeviceByName(C.bluetooth.BT_DEVICE_ACTING_AS_SERVER_NAME);
        // !!! Choose the right UUID value
        final UUID uuid = BluetoothUtils.getEmbeddedDeviceDefaultUuid();
        //final UUID uuid = BluetoothUtils.generateUuidFromString(C.bluetooth.BT_SERVER_UUID);

        new ConnectToBluetoothServerTask(serverDevice, uuid, new ConnectionTask.EventListener() {
            @Override
            public void onConnectionActive(final BluetoothChannel channel) {
                ((TextView) findViewById(R.id.statusLabel)).setText(String.format(
                    "Status : connected to server on device %s",
                    serverDevice.getName()
                ));
                connectBtn.setEnabled(false);
                closeBtn.setEnabled(true);
                sBar.setEnabled(true);
                sBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                                                    @Override
                                                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                                                        seekbarValue = progress;
                                                    }

                                                    private void updateTextView() {
                                                        String progressText = String.valueOf(seekbarValue);
                                                        seekBarText.setText(progressText);
                                                    }

                                                    @Override
                                                    public void onStartTrackingTouch(SeekBar seekBar) {
                                                        seekBarText.setEnabled(true);
                                                        updateTextView();
                                                    }

                                                    @Override
                                                    public void onStopTrackingTouch(SeekBar seekBar) {
                                                        updateTextView();
                                                        findViewById(R.id.startBtn).setEnabled(true);
                                                    }
                                                });
                btChannel = channel;
                btChannel.registerListener(new RealBluetoothChannel.Listener() {
                    @Override
                    public void onMessageReceived(String receivedMessage) {
                        String formattedMessage = String.format(
                                /*"> [RECEIVED from %s] %s\n",  // Messaggio ricevuto
                                btChannel.getRemoteDeviceName(),*/
                                receivedMessage
                        );
                        ((TextView) findViewById(R.id.umidityValue)).setText(formattedMessage);
                    }

                    @Override
                    public void onMessageSent(String sentMessage) {
                        String formattedMessage = String.format(
                                /*"> [SENT to %s] %s\n",  // Messaggio inviato
                                btChannel.getRemoteDeviceName(),*/
                                sentMessage
                        );
                       // ((TextView) findViewById(R.id.textView2)).setText(formattedMessage);
                    }
                });
            }


            @Override
            public void onConnectionCanceled() {
                ((TextView) findViewById(R.id.statusLabel)).setText(String.format(
                    "Status : unable to connect, device %s not found!",
                    C.bluetooth.BT_DEVICE_ACTING_AS_SERVER_NAME
                ));
            }
        }).execute();
    }
}
