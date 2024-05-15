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
	public static void main(String[] args) {
		String portName = "/dev/cu.isi00-DevB"; /* replace with the name of the serial port */
		MyAgent agent = new MyAgent(portName,9600);
		agent.start();
	}

}
