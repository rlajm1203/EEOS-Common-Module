package com.eeos.common.web.dto;

import com.eeos.common.application.AbstractApplicationDto;

public interface AbstractWebDto<T extends AbstractApplicationDto> {
    T toApplicationRequest();
}
