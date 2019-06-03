package io.github.microservices.demo.service;

import com.codahale.metrics.MetricRegistry;

import org.apache.camel.CamelContext;
import org.apache.camel.component.metrics.routepolicy.MetricsRoutePolicyFactory;
import org.apache.camel.opentracing.starter.CamelOpenTracing;
import org.apache.camel.spring.boot.CamelContextConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.util.StringUtils;

import io.jaegertracing.internal.JaegerTracer;
import io.jaegertracing.internal.reporters.RemoteReporter;
import io.jaegertracing.internal.samplers.ConstSampler;
import io.jaegertracing.thrift.internal.senders.HttpSender;

@CamelOpenTracing
@SpringBootApplication
public class Application {

    @Value("${jaeger.reporter.endpoint}")
    private String jagerReporterEndpoint;
    private static final Logger LOG = LoggerFactory.getLogger(Application.class);
    
    @Bean
    public io.opentracing.Tracer tracer() {
        final JaegerTracer.Builder builder = new JaegerTracer.Builder("aggregator-service");
        builder.withSampler(new ConstSampler(true));

        if(!StringUtils.isEmpty(jagerReporterEndpoint)){
            RemoteReporter.Builder rBuilder = new RemoteReporter.Builder();
            rBuilder.withSender(new HttpSender.Builder(jagerReporterEndpoint).build());
            builder.withReporter(rBuilder.build());
        }

        return builder.build();
    }
    
    @Bean
    MetricRegistry metricRegistry() {
        return new MetricRegistry();
    }

    @Bean
    CamelContextConfiguration contextConfiguration() {
        return new CamelContextConfiguration(){
            @Override
            public void beforeApplicationStart(CamelContext context) {
                LOG.info("Configuring Camel metrics on all routes");
                MetricsRoutePolicyFactory fac = new MetricsRoutePolicyFactory();
                fac.setMetricsRegistry(metricRegistry());
                context.addRoutePolicyFactory(fac);
            }

            @Override
            public void afterApplicationStart(CamelContext camelContext) {
                // noop
            }
        };
    }

    /**
     * Main method to start the application.
     */
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}