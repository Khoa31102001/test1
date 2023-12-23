package tech.dut.fasto.web.rest.admin.voucher.mapper;

import org.mapstruct.Mapper;
import tech.dut.fasto.common.domain.Voucher;
import tech.dut.fasto.common.mapper.EntityMapper;
import tech.dut.fasto.web.rest.admin.voucher.dto.request.AdminVoucherRequestDto;

import java.time.Instant;

@Mapper(componentModel = "spring")
public interface AdminVoucherEntityMapper extends EntityMapper<AdminVoucherRequestDto, Voucher> {
    default Instant map(Long unixTime) {
        return unixTime == null ? null : Instant.ofEpochSecond(unixTime);
    }
}
