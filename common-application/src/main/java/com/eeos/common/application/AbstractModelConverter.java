package com.eeos.common.application;

import com.eeos.common.domain.AbstractModel;

// ApplicationDto (=Command) -> AbstractModel 로 변환 가능한 converter
public interface AbstractModelConverter<T extends AbstractApplicationDto, R extends AbstractModel> {

    R toModel(T applicationDto);

}
