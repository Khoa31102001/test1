package tech.dut.fasto.web.rest.admin.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.dut.fasto.common.domain.User;
import tech.dut.fasto.common.domain.enumeration.UserStatus;
import tech.dut.fasto.common.repository.UserRepository;

import tech.dut.fasto.common.service.impl.MessageService;
import tech.dut.fasto.errors.FastoAlertException;
import tech.dut.fasto.web.rest.admin.user.dto.response.AdminUserResponseDto;

@Service
@RequiredArgsConstructor
public class AdminUserServiceImpl implements AdminUserService {

    private final UserRepository userRepository;

    private final MessageService messageService;

    @Override
    @Transactional(rollbackFor = FastoAlertException.class)
    public void blockUser(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new FastoAlertException(this.messageService.getMessage("error.code.user.block.failed"),this.messageService.getMessage("error.user.not.found")));
        if (!user.getStatus().equals(UserStatus.ACTIVATED)) {
            throw new FastoAlertException(this.messageService.getMessage("error.code.user.block.failed"), this.messageService.getMessage("error.user.is.inactive"));
        }
        user.setStatus(UserStatus.BLOCKED);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AdminUserResponseDto> getAllListUser(Pageable pageable, String query, UserStatus status) {
        return userRepository.getAllUsers(pageable, query, status);
    }

    @Override
    public AdminUserResponseDto getUserDetail(Long user) {
        return null;
    }
}
