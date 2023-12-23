package tech.dut.fasto.common.mapper;

import org.mapstruct.*;

import java.util.List;

public interface EntityMapper<D, E> {


    E toEntity(D dto);

    List<E> toListEntity(List<D> dtoList);

    @Named("partialUpdate")
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    void partialUpdate(@MappingTarget E entity, D dto);
}
