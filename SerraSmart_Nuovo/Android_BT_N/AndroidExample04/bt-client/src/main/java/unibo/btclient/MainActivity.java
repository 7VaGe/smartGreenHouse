package unibo.btclient;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.ToggleButton;

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
    private final static String BTCONNECTIONCLOSE = "A";
    private final static String BTCONN = "B";
    private final static String BTOUTOFDISTANCE = "z";
    private final static String BTOPEN = "o";

    private final static String CLOSEPUMP = "0";
    private Button closeBtn;
    private ToggleButton connectBtn;
    private Button manualMode;
    private SeekBar sBar;
    private Button autoMode;
    private TextView seekBarText;
    private TextView infoText;
    private TextView umidityText;
    private TextView umidityValue;

    private TextView statusLabel;
    private Button sendBtn;
    private BluetoothAdapter btAdapter;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        closeBtn = findViewById(R.id.closeBtn);
        connectBtn = findViewById(R.id.connectBtn);
        manualMode = findViewById(R.id.manualMode);
        sBar = findViewById(R.id.seekBar);
        seekBarText = findViewById(R.id.seekBarText);
        autoMode = findViewById(R.id.autoMode);
        sendBtn = findViewById(R.id.sendBtn);
        infoText = findViewById(R.id.infoText);
        umidityText = findViewById(R.id.umidityText);
        umidityValue = findViewById(R.id.umidityValue);
        statusLabel = findViewById(R.id.statusLabel);
        connectBtn.setTextOff("CONNECT TO BLUETOOTH DEVICE");
        connectBtn.setTextOn("DISCONNECT FROM BLUETOOTH DEVICE");
        updateUIForDisconnection();

        btAdapter = BluetoothAdapter.getDefaultAdapter();

        if (btAdapter != null && !btAdapter.isEnabled()) {
            startActivityForResult(
                    new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE),
                    C.bluetooth.ENABLE_BT_REQUEST
            );
        }

        initUI();
    }

    private void initUI() {
        connectBtn.setOnCheckedChangeListener((compoundButton, isChecked) -> {
            if (isChecked) {
                try {
                    connectToBTServer();
                } catch (BluetoothDeviceNotFound bluetoothDeviceNotFound) {
                    bluetoothDeviceNotFound.printStackTrace();
                    connectBtn.setChecked(false);
                }
            } else {
                if (btChannel != null && !btChannel.isClosed()) {
                    btChannel.sendMessage(BTCONNECTIONCLOSE);
                    btChannel.close();
                    updateUIForDisconnection();
                    Log.d(C.APP_LOG_TAG, "Bluetooth channel closed");
                } else {
                    Log.d(C.APP_LOG_TAG, "Bluetooth channel already closed or null");
                }
            }
        });

        closeBtn.setOnClickListener(l -> {
            if (btChannel != null && !btChannel.isClosed()) {
                btChannel.sendMessage(CLOSEPUMP);
                btChannel.close();
                updateUIForDisconnection();
                Log.d(C.APP_LOG_TAG, "Bluetooth channel closed");
            } else {
                Log.d(C.APP_LOG_TAG, "Bluetooth channel already closed or null");
            }
        });
    }

    private void updateUIForDisconnection() {
        statusLabel.setText(R.string.status_not_connected);
        sBar.setVisibility(View.INVISIBLE);
        sBar.setEnabled(false);
        sendBtn.setVisibility(View.INVISIBLE);
        sendBtn.setEnabled(false);
        closeBtn.setVisibility(View.INVISIBLE);
        closeBtn.setEnabled(false);
        autoMode.setVisibility(View.INVISIBLE);
        autoMode.setEnabled(false);
        manualMode.setVisibility(View.INVISIBLE);
        manualMode.setEnabled(false);
        seekBarText.setVisibility(View.INVISIBLE);
        umidityText.setVisibility(View.INVISIBLE);
        infoText.setVisibility(View.INVISIBLE);
        umidityValue.setVisibility(View.INVISIBLE);
        connectBtn.setEnabled(true);
        seekBarText.setVisibility(View.INVISIBLE);
        sBar.setProgress(0);
        connectBtn.setChecked(false);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (btChannel != null) {
            btChannel.close();
            Log.d(C.APP_LOG_TAG, "Bluetooth channel closed onStop");
        }
    }

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, @Nullable final Intent data) {
        if (requestCode == C.bluetooth.ENABLE_BT_REQUEST && resultCode == RESULT_OK) {
            Log.d(C.APP_LOG_TAG, "Bluetooth enabled!");
        } else if (requestCode == C.bluetooth.ENABLE_BT_REQUEST && resultCode == RESULT_CANCELED) {
            Log.d(C.APP_LOG_TAG, "Bluetooth not enabled!");
            connectBtn.setChecked(false);
        }
    }

    private void connectToBTServer() throws BluetoothDeviceNotFound {
        final BluetoothDevice serverDevice = BluetoothUtils
                .getPairedDeviceByName(C.bluetooth.BT_DEVICE_ACTING_AS_SERVER_NAME);

        final UUID uuid = BluetoothUtils.getEmbeddedDeviceDefaultUuid();
        // final UUID uuid = BluetoothUtils.generateUuidFromString(C.bluetooth.BT_SERVER_UUID);

        new ConnectToBluetoothServerTask(serverDevice, uuid, new ConnectionTask.EventListener() {
            @Override
            public void onConnectionActive(final BluetoothChannel channel) {
                btChannel = channel;
                setupConnectedUI(serverDevice);
                btChannel.registerListener(new RealBluetoothChannel.Listener() {
                    @Override
                    public void onMessageReceived(String receivedMessage) {
                        handleReceivedMessage(receivedMessage);
                    }

                    @Override
                    public void onMessageSent(String sentMessage) {
                        Log.d(C.APP_LOG_TAG, "Message sent: " + sentMessage);
                    }
                });
            }

            @Override
            public void onConnectionCanceled() {
                Log.d(C.APP_LOG_TAG, "Connection canceled");
                statusLabel.setText(String.format(
                        "Status: unable to connect, device %s not found!",
                        C.bluetooth.BT_DEVICE_ACTING_AS_SERVER_NAME
                ));
                updateUIForDisconnection();
                connectBtn.setChecked(false);
            }
        }).execute();
    }

    private void setupConnectedUI(BluetoothDevice serverDevice) {
        statusLabel.setText(String.format(
                "Status: connected \nDevice: %s", serverDevice.getName()
        ));
        connectBtn.setChecked(true);
        manualMode.setVisibility(View.VISIBLE);
        manualMode.setEnabled(true);
        closeBtn.setVisibility(View.VISIBLE);
        closeBtn.setEnabled(true);
        autoMode.setVisibility(View.INVISIBLE);
        infoText.setVisibility(View.INVISIBLE);
        umidityValue.setVisibility(View.VISIBLE);
        seekBarText.setVisibility(View.INVISIBLE);
        umidityText.setVisibility(View.VISIBLE);

        manualMode.setOnClickListener(k -> {
            btChannel.sendMessage(BTCONN);
        });

        sendBtn.setOnClickListener(j -> {
            btChannel.sendMessage(String.valueOf(seekbarValue));
            String infoTextString = (getString(R.string.sentValue) + " " + seekbarValue);
            infoText.setText(infoTextString);
        });

        autoMode.setOnClickListener(h -> {
            btChannel.sendMessage("A"); // pass to auto mode
            autoMode.setVisibility(View.INVISIBLE);
            autoMode.setEnabled(false);
            sendBtn.setVisibility(View.INVISIBLE);
            sendBtn.setEnabled(false);
            manualMode.setVisibility(View.VISIBLE);
            manualMode.setEnabled(true);
            sBar.setVisibility(View.INVISIBLE);
            sBar.setEnabled(false);
            infoText.setText(R.string.autoMode);
            seekBarText.setVisibility(View.INVISIBLE);
        });

        sBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                seekbarValue = progress;
                updateSeekBarTextView();
            }

            private void updateSeekBarTextView() {
                String progressText = String.valueOf(seekbarValue);
                seekBarText.setText(progressText);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                seekBarText.setEnabled(true);
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                seekBarText.setEnabled(false);
            }
        });
    }
    private String valueUIRender(String message) {
        try {
            if (message == null || message.isEmpty()) {
                throw new NumberFormatException("empty String");
            }
            double messageToDouble = Double.parseDouble(message);
            messageToDouble *= 100;
            int messageToInt = (int) messageToDouble;
            return messageToInt + "%";
        } catch (NumberFormatException e) {
            Log.e(C.APP_LOG_TAG, "Invalid message format: " + message, e);
            return "Invalid value";
        }
    }


    private void handleReceivedMessage(String message) {
        if(!message.isEmpty()){
            Log.d(C.APP_LOG_TAG, "Received message: " + message);
            String header = message.substring(0, 1);
            switch (header) {
                case BTOUTOFDISTANCE:
                    autoMode.setVisibility(View.INVISIBLE);
                    autoMode.setEnabled(false);
                    sendBtn.setVisibility(View.INVISIBLE);
                    sendBtn.setEnabled(false);
                    manualMode.setVisibility(View.VISIBLE);
                    manualMode.setEnabled(true);
                    infoText.setText(R.string.disconnected);
                    sBar.setVisibility(View.INVISIBLE);
                    break;
                case BTOPEN:
                    autoMode.setVisibility(View.VISIBLE);
                    autoMode.setEnabled(true);
                    sBar.setVisibility(View.VISIBLE);
                    sBar.setEnabled(true);
                    manualMode.setVisibility(View.INVISIBLE);
                    manualMode.setEnabled(false);
                    sendBtn.setVisibility(View.VISIBLE);
                    sendBtn.setEnabled(true);
                    infoText.setVisibility(View.VISIBLE);
                    infoText.setText(R.string.manual_state);
                    seekBarText.setVisibility(View.VISIBLE);
                    break;
                default:
                    umidityValue.setText(valueUIRender(message));
                    break;
            }
        }
    }
}
