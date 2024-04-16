package ThreadPrimiPassi;

public class Thread1 {

    public static void main(String[] args) {
       /**  primo modo di invocare un thread, esistono altri modi.
             Thread thread = new Thread();
                thread.start();
        */

        GattoThread gattoThread = new GattoThread();
        gattoThread.start();

    }
}
/**
*  Secondo metodo per fare creare e lanciare un thread.
* Non e un modo molto bello per richiamare un thread, si preferisce utilizzare una estensione RUNNABLE.
* */
class GattoThread  extends Thread{
    @Override
    public void run() {
        System.out.println("Sono un gatto Thread e sono stato creato da un Gatto");
    }
}