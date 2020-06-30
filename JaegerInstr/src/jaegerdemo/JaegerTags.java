package jaegerdemo;


import io.opentracing.Span;
import io.opentracing.Tracer;
import lib.DemoLib;

public class JaegerTags {
	
	private final Tracer tracer;
	
    private JaegerTags(Tracer tracer) {
   	 this.tracer = tracer;
   }

	public static void main(String[] args) {
		if (args.length != 1) {
			throw new IllegalArgumentException("One Argument is Mandatory!!!!");
		}
		String helloServiceArg = args[0];
		
        Tracer tracer = DemoLib.initTracer("tag-test-service");
		new JaegerTags(tracer).sayHello(helloServiceArg);
	}

	private void sayHello(String helloServiceArg) {
		Span span = tracer.buildSpan("hello-span").start();
		span.setTag("function param", helloServiceArg);
		span.setTag("My Another Tag", "Another Tag Value");
		
		String frmtHello = String.format("Hello, %s!", helloServiceArg);
		System.out.println(frmtHello);
		span.finish();

	}
	

}
