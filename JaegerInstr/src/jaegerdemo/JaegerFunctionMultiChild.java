package jaegerdemo;

import com.google.common.collect.ImmutableMap;

import io.opentracing.Span;
import io.opentracing.Tracer;
import lib.DemoLib;

public class JaegerFunctionMultiChild {

	private final Tracer tracer;

	private JaegerFunctionMultiChild(Tracer tracer) {
		this.tracer = tracer;
	}

	public static void main(String[] args) {
		if (args.length != 1) {
			throw new IllegalArgumentException("One Argument is Mandatory!!!!");
		}
		String helloServiceArg = args[0];

		Tracer tracer = DemoLib.initTracer("function-child-service");
		new JaegerFunctionMultiChild(tracer).sayHello(helloServiceArg);
	}

	private void sayHello(String helloServiceArg) {
		Span span = tracer.buildSpan("sayhello-span").start();
		span.log("Span started");
		span.setTag("function param", helloServiceArg);
		span.setTag("My Another Tag", "Another Tag Value");

		String valueAdd1 = valueAdd1Func(span, helloServiceArg);

		String valueAdd2 = valueAdd2Func(span, valueAdd1);

		// String frmtHello = String.format("Hello, %s!", helloServiceArg);
		span.log(ImmutableMap.of("event", "sayhello-func", "value", valueAdd2));

		System.out.println(valueAdd2);
		span.log("Span completed");
		span.finish();

	}

	private String valueAdd2Func(Span parentSpan, String valueAdd1) {
		Span span = tracer.buildSpan("valueAdd2Func").asChildOf(parentSpan).start();
		try {
			span.setTag("function name", "valueAdd2Func");
			span.setTag("function param", valueAdd1);

			String valueAdd2Result = String.format("Value Added 2, %s!", valueAdd1);
			span.log(ImmutableMap.of("event", "Value Added 2", "value", valueAdd2Result));

			return valueAdd2Result;
		} finally {
			span.finish();
		}
	}

	private String valueAdd1Func(Span parnetSpan, String helloServiceArg) {
		Span span = tracer.buildSpan("valueAdd1Func").asChildOf(parnetSpan).start();
		try {
			span.setTag("function name", "valueAdd1Func");
			span.setTag("function param", helloServiceArg);

			String valueAdd1Result = String.format("Value Added 1, %s!", helloServiceArg);
			span.log(ImmutableMap.of("event", "Value Added 1", "value", valueAdd1Result));

			return valueAdd1Result;
		} finally {
			span.finish();
		}
	}

}
