package tech.dut.fasto.web.rest.shop.voucher.mapper;

import org.mapstruct.Mapper;
import tech.dut.fasto.common.domain.Voucher;
import tech.dut.fasto.common.mapper.EntityMapper;
import tech.dut.fasto.web.rest.shop.voucher.dto.request.ShopVoucherRequestDto;

import java.time.Instant;

@Mapper(componentModel = "spring")
public interface ShopVoucherEntityMapper extends EntityMapper<ShopVoucherRequestDto, Voucher> {
    default Instant map(Long unixTime) {
        return unixTime == null ? null : Instant.ofEpochSecond(unixTime);
    }
}
