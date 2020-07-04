package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import io.jaegertracing.Configuration;
import io.jaegertracing.Configuration.ReporterConfiguration;
import io.jaegertracing.Configuration.SamplerConfiguration;

@RestController
public class HelloController {
	
    @Autowired
    private RestTemplate restTemplate;
	
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
	
    @RequestMapping("/hello")
    public String hello() {
        return "Hello from Spring Boot!";
    }
    
    @RequestMapping("/chaining")
    public String chaining() {
        ResponseEntity<String> response = restTemplate.getForEntity("http://localhost:8080/hello", String.class);
        return "Chaining Hello Response + " + response.getBody();
    }
    
    @Bean
    public io.opentracing.Tracer initTracer() {
      SamplerConfiguration samplerConfig = new SamplerConfiguration().withType("const").withParam(1);
      ReporterConfiguration reporterConfig = ReporterConfiguration.fromEnv().withLogSpans(true);
      return Configuration.fromEnv("boot-service-a").withSampler(samplerConfig).withReporter(reporterConfig).getTracer();
    }
}
