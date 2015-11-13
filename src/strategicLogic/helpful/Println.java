package strategicLogic.helpful;

public class Println {
	public static void println (Object obj) {
		System.out.println (obj);
	}
	public static void print (Object obj) {
		System.out.print (obj);
	}
	public static void printf (String fmt, Object... obj) {
		System.out.printf(fmt,  obj);
	}
}