package com.eeos.common.jpa;


import com.eeos.common.domain.AbstractModel;

public interface AbstractEntityConverter<T extends BaseEntity, R extends AbstractModel> {
    R from(T t);

    T toEntity(R t);
}

