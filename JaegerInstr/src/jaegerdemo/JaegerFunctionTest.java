package jaegerdemo;


import com.google.common.collect.ImmutableMap;

import io.opentracing.Span;
import io.opentracing.Tracer;
import lib.DemoLib;

public class JaegerFunctionTest {
	
	private final Tracer tracer;
	
    private JaegerFunctionTest(Tracer tracer) {
   	 this.tracer = tracer;
   }

	public static void main(String[] args) {
		if (args.length != 1) {
			throw new IllegalArgumentException("One Argument is Mandatory!!!!");
		}
		String helloServiceArg = args[0];
		
        Tracer tracer = DemoLib.initTracer("function-service");
		new JaegerFunctionTest(tracer).sayHello(helloServiceArg);
	}

	private void sayHello(String helloServiceArg) {
		Span span = tracer.buildSpan("sayhello-span").start();
		span.log("Span started");
		span.setTag("function param", helloServiceArg);
		span.setTag("My Another Tag", "Another Tag Value");
		
		String valueAdd1 = valueAdd1Func(span,helloServiceArg);
		
		String valueAdd2 = valueAdd2Func(span,valueAdd1);
		
		//String frmtHello = String.format("Hello, %s!", helloServiceArg);
		span.log(ImmutableMap.of("event", "sayhello-func", "value", valueAdd2));
		
		System.out.println(valueAdd2);
		span.log("Span completed");
		span.finish();

	}

	private String valueAdd2Func(Span span, String valueAdd1) {
    	span.setTag("function name", "valueAdd2Func");
		span.setTag("function param", valueAdd1);
		
    	String valueAdd2Result = String.format("Value Added 2, %s!", valueAdd1);
        span.log(ImmutableMap.of("event", "Value Added 2", "value", valueAdd2Result));		
		
		return valueAdd2Result;
	}

	private String valueAdd1Func(Span span, String helloServiceArg) {
		span.setTag("function name", "valueAdd1Func");
		span.setTag("function param", helloServiceArg);
		
		String valueAdd1Result = String.format("Value Added 1, %s!", helloServiceArg);
		span.log(ImmutableMap.of("event", "Value Added 1", "value", valueAdd1Result));
		
		return valueAdd1Result;
	}
	

}
