package com.eeos.common.web.dto;

import com.eeos.common.application.AbstractApplicationDto;

// T 타입으로 변환할 수 있는 Web 계층의 DTO
public interface AbstractWebDto<T extends AbstractApplicationDto> {
    T toApplicationRequest();
}
