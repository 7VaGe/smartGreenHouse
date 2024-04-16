package ThreadPrimiPassi;
/**
* In questo caso si implementa un runnable al posto di estendere un thread,
* la interfaccia Runnable, si utilizza per creare delle classi che verranno eseguite da un thread */
public class ThreadRunnable {
    public static void main(String[] args) {
        //Diamo in pasto un runnable, e gli facciamo eseguire qualcosa all'interno come la stampa
        Thread th = new Thread(new PolloRunnable());
        th.start();
    }
}

class PolloRunnable implements Runnable {
    @Override
    public void run() {
        System.out.println("Sono un bel pollo runnable.");
    }
}