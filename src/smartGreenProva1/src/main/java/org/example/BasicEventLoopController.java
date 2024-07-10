package org.example;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public abstract class BasicEventLoopController extends Thread implements Observer{
    public static final int defaultEventQueueSize = 50;
    protected BlockingQueue<Event> eventQueue;

    protected BasicEventLoopController(int size){
        eventQueue = new ArrayBlockingQueue<Event>(size);
    }

    protected BasicEventLoopController(){
        this(defaultEventQueueSize);
    }

    /**
     * The architecture behind the system manages an event loop based controller.
     *  It makes use of a thread to process events in a queue, taking an event that is in the head position queue.
     * */
    public void run(){
        while (true){
            try {
                Event ev = this.waitForNextEvent();
                this.processEvent(ev);
            } catch (Exception ex){
                ex.printStackTrace();
            }
        }
    }
    /**
     * Function used to pick the nextEvent in waiting on the queue, taking this event that is in the head position queue.
     * */
    protected Event waitForNextEvent() throws InterruptedException {
        return eventQueue.take();
    }
    /**
     * Function used to offer the Event to the queue,insert element in queue.
     * */
    @Override
    public boolean notifyEvent(Event ev){
        return eventQueue.offer(ev);
    }

    /**
     * Function used to process event ev
     *
     * @param ev event to be proccessed
     * */
    protected abstract void processEvent(org.example.Event ev);
}
