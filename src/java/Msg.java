package gui;

public enum Msg
{
	/**
	 * Set the sugar value at 0.
	 */
	FRIST_MESSAGE("0"),
	/**
	 * Set the sugar value at 1.
	 */
	SECOND_MESSAGE("1"),
	/**
	 * Set the sugar value at 2.
	 */
	THIRD_MESSAGE("2"),
	/**
	 * Set the sugar value at 3.
	 */
	FOURTH_MESSAGE("3"),
	/**
	 *  Warns java to write on text this message below
	 */
	FIFTH_MESSAGE("4", "WELCOME!"),
	/**
	 *  to be continued
	 */
	SIXTH_MESSAGE("7", "MAKING A COFFEE"),
	/**
	 * It warns that coffee has finished
	 */
	EMPTY_COFFE("6", "NO MORE COFFEE, WAITING FOR RECHARGE!"),
	/**
	 * The coffee is ready
	 */
	COFFEE_READY("8", "THE COFFEE IS READY"),
	/**
	 *  Warns Java that the coffee machine was refilled.
	 */
	EIGHT_MESSAGE("9", "COFFEE REFILLED, DONE");
	
	
	private String Numero;
	private String Messaggio;
	
	
	private Msg(String Numero) {
		this.Numero = Numero;
	}
	private Msg(String Numero, String Messaggio) {
		this.Numero=Numero;
		this.Messaggio=Messaggio;
	}
	
	public String getNum() {
		return this.Numero;
	}
	public String getMessage() {
		return this.Messaggio;
	}
}
