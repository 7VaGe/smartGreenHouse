package ThreadConcorrenti;

import ThreadPrimiPassi.ThreadRunnable;

public class DueThreadConcorrenti {
    /*
    * */

    public static void main(String[] args) {

        // Inserisco i nomi per i nostri thread che utilizzerò
        String th1Name = "Runner 1";
        String th2Name = "Runner 2";

        // Creo degli oggetti runnable
        ThRunnable th1Run = new ThRunnable();
        ThRunnable th2Run = new ThRunnable();

        // Creo i nostri thread e gli assegno prima il nostro oggetto runnable, poi il nome dedicato per ognuno.
        Thread th1 =  new Thread(th1Run, th1Name);
        Thread th2 =  new Thread(th2Run, th2Name);

        // Lancio i thread
        th1.start();
        th2.start();
    }
}
