package tech.dut.fasto.web.rest.shop.product.mapper;

import org.mapstruct.Mapper;
import tech.dut.fasto.common.domain.Product;
import tech.dut.fasto.common.mapper.CategoryMapper;
import tech.dut.fasto.common.mapper.DtoMapper;
import tech.dut.fasto.web.rest.shop.product.dto.response.ShopProductResponseDto;

@Mapper(componentModel = "spring", uses = {CategoryMapper.class})
public interface ShopProductDtoMapper extends DtoMapper<ShopProductResponseDto, Product> {
}
