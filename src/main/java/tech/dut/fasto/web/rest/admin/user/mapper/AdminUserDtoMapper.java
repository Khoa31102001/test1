package tech.dut.fasto.web.rest.admin.user.mapper;

import org.mapstruct.Mapper;
import tech.dut.fasto.common.domain.User;
import tech.dut.fasto.common.mapper.DtoMapper;
import tech.dut.fasto.common.mapper.UserInformationMapper;
import tech.dut.fasto.web.rest.admin.user.dto.response.AdminUserResponseDto;

@Mapper(componentModel = "spring", uses = {UserInformationMapper.class})
public interface AdminUserDtoMapper extends DtoMapper<AdminUserResponseDto, User> {
}
