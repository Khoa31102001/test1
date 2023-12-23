package tech.dut.fasto.web.rest.admin.shop.Mapper;

import org.mapstruct.Mapper;
import tech.dut.fasto.common.domain.Shop;
import tech.dut.fasto.common.mapper.DtoMapper;
import tech.dut.fasto.common.mapper.LocationMapper;
import tech.dut.fasto.web.rest.admin.shop.dto.AdminShopResponseDto;

@Mapper(componentModel = "spring", uses = {LocationMapper.class})
public interface AdminShopDtoMapper extends DtoMapper<AdminShopResponseDto, Shop> {
}
