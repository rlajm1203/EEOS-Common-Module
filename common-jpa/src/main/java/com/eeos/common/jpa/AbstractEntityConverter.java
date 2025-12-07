package com.eeos.common.jpa;


import com.eeos.common.domain.AbstractModel;

// JpaEntity <-> DomainModel 간 변환을 위한 Converter
public interface AbstractEntityConverter<T extends BaseEntity, R extends AbstractModel> {
    R from(T t);

    T toEntity(R t);
}

