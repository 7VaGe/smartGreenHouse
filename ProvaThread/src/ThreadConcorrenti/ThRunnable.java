package ThreadConcorrenti;

public class ThRunnable implements Runnable{
    // Campo di istanza, che viene utilizzato globalmente, per alcune funzioni dedicate come l'incremento del proprio valore.
    private int counter;
    //Quando ho un campo di istanza questo viene condiviso dai notri due thread, non male.
    @Override
    public void run() {
        /*Zoccolo zoccolo = new Zoccolo("Beige");
        System.out.println(zoccolo);*/
            // Ciclo for che viene utilizzato per la gestione della corsa dei nostri due thread.
        /*
        *  Al suo interno abbiamo un parametro I che utilizzaimo come contatore interno per iterare il nostro ciclo,
        *  Questo parametro viene inserito all'interno della nostro thread Stack, sia di Th1 che Th2.
        * */
            for (int i = 0; i<5; i++){
                try{
                    Thread.sleep(1500);
                    System.out.printf("Thread %s è in running %d volte... \n", Thread.currentThread().getName(), getAndIncrementCounter());
                }catch(InterruptedException e){
                    Thread.currentThread().interrupt();
                    throw new RuntimeException(e);
                }
            }
    }

    public synchronized int getAndIncrementCounter(){
        counter++;
        return counter;
    }
}

//Con la parola chiave VOLATILE leggeremo sempre dalla memoria quel valore, senza che la JVM utilizzi delle ottimizzazioni non richieste.