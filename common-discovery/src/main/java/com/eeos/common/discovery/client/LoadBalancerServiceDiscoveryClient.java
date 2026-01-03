package com.eeos.common.discovery.client;

import com.eeos.common.discovery.exception.ServiceDiscoveryException;
import java.net.URI;
import java.util.Objects;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.util.StringUtils;
import org.springframework.web.util.UriComponentsBuilder;

public class LoadBalancerServiceDiscoveryClient implements ServiceDiscoveryClient {

    private final LoadBalancerClient loadBalancerClient;

    public LoadBalancerServiceDiscoveryClient(LoadBalancerClient loadBalancerClient) {
        this.loadBalancerClient = loadBalancerClient;
    }

    @Override
    public URI getServiceUri(String serviceId) {
        return getServiceUri(serviceId, null);
    }

    @Override
    public URI getServiceUri(String serviceId, String path) {
        ServiceInstance instance = chooseInstance(serviceId);
        URI baseUri = instance.getUri();

        if (!StringUtils.hasText(path)) {
            return baseUri;
        }

        String sanitizedPath = path.startsWith("/") ? path : "/" + path;
        return UriComponentsBuilder
                .fromUri(baseUri)
                .path(sanitizedPath)
                .build(true)
                .toUri();
    }

    private ServiceInstance chooseInstance(String serviceId) {
        Objects.requireNonNull(serviceId, "serviceId 는 null 이 될 수 없습니다.");
        ServiceInstance instance = loadBalancerClient.choose(serviceId);
        if (instance == null) {
            throw new ServiceDiscoveryException(serviceId);
        }
        return instance;
    }
}
