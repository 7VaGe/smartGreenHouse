package unibo.btlib;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import java.util.ArrayList;
import java.util.List;

import unibo.btlib.utils.C;

public abstract class BluetoothChannel implements CommChannel {
    private final List<Listener> listeners = new ArrayList<>();
    private final BluetoothChannelHandler btChannelHandler =
            new BluetoothChannelHandler(Looper.getMainLooper());

    ExtendedRunnable worker;
    private boolean closed = false;
    @Override
    public void close(){
        if (!closed) {
            closed = true;
            worker.cancel();
        }
    }

    @Override
    public void registerListener(final Listener listener){
        listeners.add(listener);
    }

    @Override
    public void removeListener(final Listener listener){
        listeners.remove(listener);
    }

    @Override
    public void sendMessage(final String message) {
        if (!closed) {
            worker.write(message.getBytes());
        } else {
            throw new IllegalStateException("Cannot send message: BluetoothChannel is closed.");
        }
    }
    public boolean isClosed() {
        return closed;
    }
    protected BluetoothChannelHandler getBTChannelHandler() {
        return btChannelHandler;
    }

    /**
     * Used for communication between the Main thread and one
     * or more Worker threads (BluetoothChannel's workers).
     */
    class BluetoothChannelHandler extends Handler {

        BluetoothChannelHandler(final Looper looper){
            super(looper);
        }

        @Override
        public void handleMessage(final Message message) {

            if(message.what == C.channel.MESSAGE_RECEIVED) {
                for(Listener l : listeners){
                    l.onMessageReceived(new String((byte[])message.obj));
                }
            }

            if(message.what == C.channel.MESSAGE_SENT) {
                for(Listener l : listeners){
                    l.onMessageSent(new String((byte[])message.obj));
                }
            }
        }
    }
}
