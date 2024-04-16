package ThreadPrimiPassi;

public class ThreadClasseAnonima {
    /**
     * Questa è la classe anonima, dove al suo interno richiede l'override del metodo principale, che è il RUN*/
    public static void main(String[] args) {

        Runnable thRunnable = new Runnable() {
            @Override
            public void run() {
                System.out.println("Prova di stampa");
            }
        };

        /**
         * nota la differenza tra TH e thRunnable, sono due cose diverse, uno è un runnable, uno è un thread.*/
        Thread th = new Thread(thRunnable, "sono un threadRunnable in classe anonima"); //ho dato un nome al mio thread, così posso capire quale sia il thread in elaborazione
        th.start();
    }
}
