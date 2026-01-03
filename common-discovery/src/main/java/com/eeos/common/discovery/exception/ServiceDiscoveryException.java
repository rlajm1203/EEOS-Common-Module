package com.eeos.common.discovery.exception;

public class ServiceDiscoveryException extends RuntimeException {

    public ServiceDiscoveryException(String serviceId) {
        super("serviceId에 해당하는 사용 가능한 서비스 인스턴스가 존재하지 않습니다: " + serviceId);
    }
}
