package com.eeos.common.discovery.autoconfig;

import com.eeos.common.discovery.client.LoadBalancerServiceDiscoveryClient;
import com.eeos.common.discovery.client.ServiceDiscoveryClient;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.context.annotation.Bean;

// ServiceDiscoveryClient 를 자동 설정하는 AutoConfiguration 클래스
@AutoConfiguration
@ConditionalOnClass(LoadBalancerClient.class)
public class ServiceDiscoveryAutoConfiguration {

    @Bean
    @ConditionalOnBean(LoadBalancerClient.class)
    @ConditionalOnMissingBean(ServiceDiscoveryClient.class)
    public ServiceDiscoveryClient serviceDiscoveryClient(LoadBalancerClient loadBalancerClient) {
        return new LoadBalancerServiceDiscoveryClient(loadBalancerClient);
    }
}
