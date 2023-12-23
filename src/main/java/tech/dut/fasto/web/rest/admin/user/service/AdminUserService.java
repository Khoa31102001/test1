package tech.dut.fasto.web.rest.admin.user.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import tech.dut.fasto.common.domain.enumeration.UserStatus;
import tech.dut.fasto.web.rest.admin.user.dto.response.AdminUserResponseDto;

public interface AdminUserService {


    void blockUser(Long userId);
    Page<AdminUserResponseDto> getAllListUser(Pageable pageable, String query, UserStatus status);

    AdminUserResponseDto getUserDetail(Long user);
}
