package ThreadPrimiPassi;

public class PraticaNomiThread {
    /*
    *   Creo una istanza di runnable, e all'interno inserisco un get per avere il nome assegnato al thread in uso. */
    public static void main(String[] args) {
        Runnable thRunnable = () -> {
          String name = Thread.currentThread().getName();
          System.out.println("Sono un thread e il mio nome in codice è: "+ name);
        };
// Creo un ciclo for per gestire gli sleep e le stampe adeguatamente formattate, puoi inserire anche il printf senza println e formatted per evitare la ridondanza.
        for( int i = 3; i>0; i--){
            System.out.println("Dormirò fra %s ".formatted(i));
            try {
                Thread.sleep(1000); //metto in pausa il mio thread delle stampe di 1 secondo.
            }catch (InterruptedException e){
                e.printStackTrace();
                Thread.currentThread().interrupt();
            }
        }
        System.out.println("Sto dormendo");
        try {
            Thread.sleep(5000); //Lo metto a dormire per 5 secondi appena passano
        }catch(InterruptedException e){
            Thread.currentThread().interrupt();
        }
        System.out.println("SVEGLIA!"); //APPENA PASSATI sveglio il mio thread e stampo SVEGLIA a video.
        Thread th = new Thread(thRunnable, "Th1");
        th.start();
    }
}
