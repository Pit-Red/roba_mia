public class NumberTok extends Token {
	public static int valore;
	public String toString() { return "<" + tag + ", " + valore + ">"; }

	public NumberTok(int tag, String s){
		super(tag);
		valore = Integer.parseInt(s);
	}
}
