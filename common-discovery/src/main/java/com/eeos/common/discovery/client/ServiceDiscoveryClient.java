package com.eeos.common.discovery.client;

import java.net.URI;

/**
 * 서비스 ID를 실제 접근 가능한 URI로 변환해 주는 클라이언트
 */
public interface ServiceDiscoveryClient {

    /**
     * 주어진 서비스의 기본 URI 조회 메소드
     *
     * @param serviceId 디스커버리 서버에 등록된 서비스 식별자
     * @return 선택된 서비스 URI
     */
    URI getServiceUri(String serviceId);

    /**
     * 주어진 서비스의 URI를 조회하고 제공받은 경로를 이어 붙입니다.
     *
     * @param serviceId 디스커버리 서버에 등록된 서비스 식별자
     * @param path 서비스 기본 URI에 추가할 경로
     * @return 제공된 경로가 포함된 선택된 서비스 URI
     */
    URI getServiceUri(String serviceId, String path);
}
