package jaegerdemo;

import io.jaegertracing.Configuration;
import io.jaegertracing.Configuration.ReporterConfiguration;
import io.jaegertracing.Configuration.SamplerConfiguration;
import io.jaegertracing.internal.JaegerTracer;
import io.opentracing.Span;
import io.opentracing.Tracer;

public class HelloSpan {
	
	private final Tracer tracer;
	
    private HelloSpan(Tracer tracer) {
   	 this.tracer = tracer;
   }

	public static void main(String[] args) {
		if (args.length != 1) {
			throw new IllegalArgumentException("One Argument is Mandatory!!!!");
		}
		String helloServiceArg = args[0];
		
        Tracer tracer = initTracer("hello-world-service");
		new HelloSpan(tracer).sayHello(helloServiceArg);
	}

	private void sayHello(String helloServiceArg) {
		Span span = tracer.buildSpan("hello-span").start();
		String frmtHello = String.format("Hello, %s!", helloServiceArg);
		System.out.println(frmtHello);
		span.finish();

	}
	
    public static JaegerTracer initTracer(String service) {
    	//Constant (sampler.type=const) sampler always makes the same decision for all traces. 
    	//It either samples all traces (sampler.param=1) or none of them (sampler.param=0)
        SamplerConfiguration mySamplerConfig = SamplerConfiguration.fromEnv().withType("const").withParam(1);

        //Whether the reporter should also log the spans
        ReporterConfiguration myReporterConfig = ReporterConfiguration.fromEnv().withLogSpans(true);
        
        //Creates configuration with the above created sampler and reporter configuration
        Configuration config = new Configuration(service).withSampler(mySamplerConfig).withReporter(myReporterConfig);
        
        //return the tracker
        return config.getTracer();

    }
}
