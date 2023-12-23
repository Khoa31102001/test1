package tech.dut.fasto.web.rest.user.information.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.dut.fasto.common.domain.User;
import tech.dut.fasto.common.domain.enumeration.UserStatus;
import tech.dut.fasto.common.repository.UserInformationRepository;
import tech.dut.fasto.common.repository.UserRepository;

import tech.dut.fasto.common.service.impl.MessageService;
import tech.dut.fasto.config.security.SecurityUtils;
import tech.dut.fasto.errors.FastoAlertException;
import tech.dut.fasto.errors.FastoAlertException;
import tech.dut.fasto.web.rest.user.information.dto.request.UserAvatarDtoRequest;
import tech.dut.fasto.web.rest.user.information.dto.request.UserProfileDtoRequest;
import tech.dut.fasto.web.rest.user.information.dto.response.UserProfileDtoResponse;

import java.time.Instant;

@RequiredArgsConstructor
@Service
public class UserInformationServiceImpl implements  UserInformationService {

    private final UserRepository userRepository;

    private final UserInformationRepository userInformationRepository;

    private final MessageService messageService;

    @Override
    @Transactional(readOnly = true)
    public UserProfileDtoResponse getProfileUser() {

        String email = SecurityUtils.getCurrentUserLogin().orElseThrow(() -> new FastoAlertException(messageService.getMessage("error.code.user.get.information.failed"), messageService.getMessage("error.authenticate.unauthorized.user")));
        User user = userRepository.findByEmailAndStatus(email, UserStatus.ACTIVATED).orElseThrow(() -> new FastoAlertException(messageService.getMessage("error.code.user.get.information.failed"), messageService.getMessage("error.authenticate.unauthorized.user")));

        return userInformationRepository.getProfileUser(user.getId());
    }

    @Override
    @Transactional(readOnly = true)
    public UserProfileDtoResponse getProfileUserOther(Long id) {

        String email = SecurityUtils.getCurrentUserLogin().orElseThrow(() -> new FastoAlertException(messageService.getMessage("error.code.user.get.information.failed"), messageService.getMessage("error.authenticate.unauthorized.user")));
        User user = userRepository.findByEmailAndStatus(email, UserStatus.ACTIVATED).orElseThrow(() -> new FastoAlertException(messageService.getMessage("error.code.user.get.information.failed"), messageService.getMessage("error.authenticate.unauthorized.user")));

        return userInformationRepository.getProfileUser(id);
    }

    @Override
    @Transactional(rollbackFor = FastoAlertException.class)
    public void updateProfileUser(UserProfileDtoRequest userProfileDtoRequest) {
        String email = SecurityUtils.getCurrentUserLogin().orElseThrow(() -> new FastoAlertException(messageService.getMessage("error.code.user.update.information.failed"), messageService.getMessage("error.authenticate.unauthorized.user")));
        User user = userRepository.findByEmailAndStatus(email, UserStatus.ACTIVATED).orElseThrow(() -> new FastoAlertException(messageService.getMessage("error.code.user.update.information.failed"), messageService.getMessage("error.authenticate.unauthorized.user")));
        user.getUserInformation().setUserImage(userProfileDtoRequest.getUserImage());
        user.getUserInformation().setFirstName(userProfileDtoRequest.getFirstName());
        user.getUserInformation().setLastName(userProfileDtoRequest.getLastName());
        user.getUserInformation().setGender(userProfileDtoRequest.getGender());
        user.getUserInformation().setBirthday(Instant.ofEpochSecond(userProfileDtoRequest.getBirthday()));
        userRepository.save(user);
    }

    @Override
    @Transactional(rollbackFor = FastoAlertException.class)
    public void updateAvatar(UserAvatarDtoRequest userAvatarDtoRequest) {
        String email = SecurityUtils.getCurrentUserLogin().orElseThrow(() -> new FastoAlertException(messageService.getMessage("error.code.user.update.avatar.failed"), messageService.getMessage("error.authenticate.unauthorized.user")));
        User user = userRepository.findByEmailAndStatus(email, UserStatus.ACTIVATED).orElseThrow(() -> new FastoAlertException(messageService.getMessage("error.code.user.update.avatar.failed"), messageService.getMessage("error.authenticate.unauthorized.user")));
        user.getUserInformation().setUserImage(userAvatarDtoRequest.getImageUser());
        userRepository.save(user);

    }
}
