package tech.dut.fasto.common.mapper;

import java.util.List;

public interface DtoMapper<D, E> {
    D toDto(E entity);

    List<D> toListDto(List<E> entityList);
}
