package jaegerdemo;


import com.google.common.collect.ImmutableMap;

import io.opentracing.Span;
import io.opentracing.Tracer;
import lib.DemoLib;

public class JaegerLogs {
	
	private final Tracer tracer;
	
    private JaegerLogs(Tracer tracer) {
   	 this.tracer = tracer;
   }

	public static void main(String[] args) {
		if (args.length != 1) {
			throw new IllegalArgumentException("One Argument is Mandatory!!!!");
		}
		String helloServiceArg = args[0];
		
        Tracer tracer = DemoLib.initTracer("log-test-service");
		new JaegerLogs(tracer).sayHello(helloServiceArg);
	}

	private void sayHello(String helloServiceArg) {
		Span span = tracer.buildSpan("hello-span").start();
		span.log("Span started");
		span.setTag("function param", helloServiceArg);
		span.setTag("My Another Tag", "Another Tag Value");
		
		String frmtHello = String.format("Hello, %s!", helloServiceArg);
		span.log(ImmutableMap.of("event", "string-format", "value", frmtHello));
		
		System.out.println(frmtHello);
		span.log("Span completed");
		span.finish();

	}
	

}
