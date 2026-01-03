package com.eeos.common.discovery.autoconfig;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.eeos.common.discovery.client.ServiceDiscoveryClient;
import com.eeos.common.discovery.exception.ServiceDiscoveryException;
import java.io.IOException;
import java.net.URI;
import java.util.Collections;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.cloud.client.loadbalancer.LoadBalancerRequest;
import org.springframework.cloud.client.loadbalancer.Request;

class ServiceDiscoveryAutoConfigurationTest {

    private final ApplicationContextRunner contextRunner =
            new ApplicationContextRunner()
                    .withConfiguration(AutoConfigurations.of(ServiceDiscoveryAutoConfiguration.class))
                    .withBean(LoadBalancerClient.class, TestLoadBalancerClient::new);

    @Test
    void registersServiceDiscoveryClientWhenLoadBalancerPresent() {
        contextRunner.run(context -> assertThat(context).hasSingleBean(ServiceDiscoveryClient.class));
    }

    @Test
    void serviceDiscoveryClientResolvesUris() {
        contextRunner.run(
                context -> {
                    ServiceDiscoveryClient client = context.getBean(ServiceDiscoveryClient.class);
                    assertThat(client.getServiceUri("user")).isEqualTo(URI.create("http://localhost:8080"));
                    assertThat(client.getServiceUri("user", "/health"))
                            .isEqualTo(URI.create("http://localhost:8080/health"));
                    assertThat(client.getServiceUri("user", "api"))
                            .isEqualTo(URI.create("http://localhost:8080/api"));
                });
    }

    @Test
    void throwsExceptionWhenNoInstanceAvailable() {
        new ApplicationContextRunner()
                .withConfiguration(AutoConfigurations.of(ServiceDiscoveryAutoConfiguration.class))
                .withBean(LoadBalancerClient.class, EmptyLoadBalancerClient::new)
                .run(context -> {
                    ServiceDiscoveryClient client = context.getBean(ServiceDiscoveryClient.class);
                    assertThatThrownBy(() -> client.getServiceUri("user"))
                            .isInstanceOf(ServiceDiscoveryException.class);
                });
    }

    static class TestLoadBalancerClient implements LoadBalancerClient {

        @Override
        public ServiceInstance choose(String serviceId) {
            return new StaticServiceInstance();
        }

        @Override
        public <T> ServiceInstance choose(String serviceId, Request<T> request) {
            return choose(serviceId);
        }

        @Override
        public <T> T execute(String serviceId, LoadBalancerRequest<T> request) throws IOException {
            try {
                return request.apply(choose(serviceId));
            } catch (Exception ex) {
                throw new IOException(ex);
            }
        }

        @Override
        public <T> T execute(String serviceId, ServiceInstance serviceInstance, LoadBalancerRequest<T> request)
                throws IOException {
            try {
                return request.apply(serviceInstance);
            } catch (Exception ex) {
                throw new IOException(ex);
            }
        }

        @Override
        public URI reconstructURI(ServiceInstance instance, URI original) {
            return original;
        }
    }

    static class EmptyLoadBalancerClient extends TestLoadBalancerClient {
        @Override
        public ServiceInstance choose(String serviceId) {
            return null;
        }
    }

    static class StaticServiceInstance implements ServiceInstance {

        @Override
        public String getServiceId() {
            return "user-service";
        }

        @Override
        public String getHost() {
            return "localhost";
        }

        @Override
        public int getPort() {
            return 8080;
        }

        @Override
        public boolean isSecure() {
            return false;
        }

        @Override
        public URI getUri() {
            return URI.create("http://localhost:8080");
        }

        @Override
        public Map<String, String> getMetadata() {
            return Collections.emptyMap();
        }
    }
}
