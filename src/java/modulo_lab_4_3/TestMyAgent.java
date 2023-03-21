package seiot.modulo_lab_4_3;

/**
 * In questo esempio un agente basato su architettura di controllo event-loop
 * (pattern reactor) invia "ping" e riceve dei "pong" via seriale (UART o Bluetooth)
 * aspettando 500 ms ad ogni ricezione.
 * 
 * Highlights:
 * - come usare un arch di controllo event-loop con message service, usando
 *   in un approccio ad eventi, senza primitive bloccanti.
 *   
 * @author aricci
 *
 */
public class TestMyAgent {
	private static String PORT = "COM4";
	private static int BAUD = 9600;
	
	public static void main(String[] args) {
		 /* replace with the name of the serial port */
		System.out.println("Sono in ascolto sulla porta: " + PORT + "\nAlla frequenza di: "+ BAUD + "Baud.");
		MyAgent agent = new MyAgent(PORT,BAUD);
		agent.start();
	}

}
