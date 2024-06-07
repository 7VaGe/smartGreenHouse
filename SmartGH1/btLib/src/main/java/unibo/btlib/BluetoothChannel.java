package unibo.btlib;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import java.util.ArrayList;
import java.util.List;

import unibo.btlib.utils.C;

public abstract class BluetoothChannel implements CommChannel {

    private final List<Listener> listeners = new ArrayList<>();
    BluetoothChannelHandler btChannelHandler = new BluetoothChannelHandler(Looper.getMainLooper());
    ExtendedRunnable worker;

    // Variabile per tracciare lo stato della connessione
    private boolean isOpen;

    public BluetoothChannel() {
        this.isOpen = false;
    }

    @Override
    public void close() {
        worker.cancel();
        this.isOpen = false; // Aggiorna lo stato
    }

    @Override
    public void registerListener(final Listener listener) {
        listeners.add(listener);
    }

    @Override
    public void removeListener(final Listener listener) {
        listeners.remove(listener);
    }

    @Override
    public void sendMessage(final String message) {
        worker.write(message.getBytes());
    }

    public boolean isOpen() {
        return isOpen;
    }

    public void setOpen(boolean open) {
        this.isOpen = open;
    }

    class BluetoothChannelHandler extends Handler {

        BluetoothChannelHandler(final Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(final Message message) {
            if (message.what == C.channel.MESSAGE_RECEIVED) {
                for (Listener l : listeners) {
                    l.onMessageReceived(new String((byte[]) message.obj));
                }
            }

            if (message.what == C.channel.MESSAGE_SENT) {
                for (Listener l : listeners) {
                    l.onMessageSent(new String((byte[]) message.obj));
                }
            }
        }
    }
}
