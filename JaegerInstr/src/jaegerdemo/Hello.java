package jaegerdemo;

public class Hello {

	public static void main(String[] args) {
		if (args.length != 1) {
			throw new IllegalArgumentException("One Argument is Mandatory!!!!");
		}
		String helloServiceArg = args[0];
		new Hello().sayHello(helloServiceArg);
	}

	private void sayHello(String helloServiceArg) {
		String frmtHello = String.format("Hello, %s!", helloServiceArg);
		System.out.println(frmtHello);

	}
}
