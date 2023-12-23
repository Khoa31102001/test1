package tech.dut.fasto.web.rest.shop.product.mapper;

import org.mapstruct.Mapper;
import tech.dut.fasto.common.domain.Product;
import tech.dut.fasto.common.mapper.EntityMapper;
import tech.dut.fasto.web.rest.shop.product.dto.request.ShopProductRequestDto;

@Mapper(componentModel = "spring")
public interface ShopProductEntityMapper extends EntityMapper<ShopProductRequestDto, Product> {
}
