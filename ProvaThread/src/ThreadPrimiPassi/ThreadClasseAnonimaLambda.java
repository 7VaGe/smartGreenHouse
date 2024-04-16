package ThreadPrimiPassi;

public class ThreadClasseAnonimaLambda {

    /**
     * Lo stesso codice puoi scriverlo utilizzando le lambda è questo risulta essere un codice più professionale.*/
    public static void main(String[] args) {
        Runnable thRunnable = () -> System.out.println("Prova di stampa con Lambda");
        Thread th = new Thread(thRunnable, "Thread Classe Anonima con Lambda");
        th.start();
    }
}
