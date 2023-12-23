package tech.dut.fasto.web.rest.user.information.service;

import tech.dut.fasto.web.rest.user.information.dto.request.UserAvatarDtoRequest;
import tech.dut.fasto.web.rest.user.information.dto.request.UserProfileDtoRequest;
import tech.dut.fasto.web.rest.user.information.dto.response.UserProfileDtoResponse;

public interface UserInformationService {
    UserProfileDtoResponse getProfileUser();
    void updateProfileUser(UserProfileDtoRequest userProfileDtoRequest);

    void updateAvatar(UserAvatarDtoRequest userAvatarDtoRequest);

    UserProfileDtoResponse getProfileUserOther(Long id);
}
