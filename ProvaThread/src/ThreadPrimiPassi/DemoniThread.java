package ThreadPrimiPassi;

public class DemoniThread {
    public static void main(String[] args) throws InterruptedException {
        /*
        *
        * */
        Thread.getAllStackTraces();

        //Prima scrivi cosa deve fare il runnable e lo completi con il lavoro che deve svolgere
        Runnable thRunnable = () -> {
            while(true){
                try{
                    Thread.sleep(1000);
                }catch(InterruptedException e){
                    throw new RuntimeException(e);
                }
                Thread.getAllStackTraces();
                System.out.println("Thead che sta correndo");
            }
        };
        // poi lo richiami lanciando un thread che eseguira il runnable, precisamente il metodo run del runnable, quello
        // che hai inserito dentro la lambda.
        Thread thread = new Thread(thRunnable);

        //cosa accadrebbe se noi facessimo diventare questo thread un DEMONE??
        //Cos'è un DEMONE ? e come funziona ?

        //LA JVM non considera i demoni, e nel caso un thread diventi un demone, la JVM non aspetta che questo finisce il suo lavoro
        //prima di poter far altro, anzi, lei non lo considera proprio e continua a lavorare su altri thread.
        //LA JVM fuiche ha dei thread non demoni, aspetta che i thread finiscano il loro lavoro.

        thread.setDaemon(true); //Questo thread viene indicato come demone, e non viene nemmeno eseguito.
        /*
        * Ora che ho impostato il mio thread come demone, la mia JVM non lo eseguirà mai, essendo per appunto un demone,
        * e quindi il mio runnable non verrà mai eseguito in quanto la JVM non considera il demone che
        * lancia per appunto questo runnable.
        * */
        thread.start();

        // Se qui aggiungessi ora una sleep sul thread principale, darei del tempo al mio demone di rimanere in vita, per i millis della sleep
        // questa cosa andrebbe approfondita ulteriormente.
        // Thread.sleep(3000);

        //Tramite la funzione join, possiamo aspettare che il Thread muoia, e finchè questo thread non muore, il mio demone
        //può continuare ad eseguire le sue istruzioni senza che la JVM dia precedenza ad altri thread non demoni.
        thread.join();
    }
}
