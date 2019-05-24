package nl.hari;

import io.jaegertracing.internal.JaegerTracer;
import io.opentracing.Tracer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ConfigTrace {


    @Bean
    public Tracer tracer(){
        Tracer tracer = new JaegerTracer.Builder("trace-server").build();
        return tracer;
    }



}
