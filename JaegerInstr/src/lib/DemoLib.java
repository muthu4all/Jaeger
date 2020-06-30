package lib;

import io.jaegertracing.Configuration;
import io.jaegertracing.Configuration.ReporterConfiguration;
import io.jaegertracing.Configuration.SamplerConfiguration;
import io.jaegertracing.internal.JaegerTracer;

public final class DemoLib {
	

	private DemoLib() {
		
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
