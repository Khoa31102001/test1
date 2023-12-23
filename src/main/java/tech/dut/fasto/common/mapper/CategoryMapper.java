package tech.dut.fasto.common.mapper;

import org.mapstruct.Mapper;
import tech.dut.fasto.common.domain.Category;
import tech.dut.fasto.common.dto.CategoryDto;

@Mapper(componentModel = "spring")
public interface CategoryMapper extends DtoMapper<CategoryDto, Category> {
}
