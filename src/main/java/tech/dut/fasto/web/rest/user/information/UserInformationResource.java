package tech.dut.fasto.web.rest.user.information;

import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import tech.dut.fasto.config.security.AuthoritiesConstants;
import tech.dut.fasto.web.rest.user.information.dto.request.UserAvatarDtoRequest;
import tech.dut.fasto.web.rest.user.information.dto.request.UserProfileDtoRequest;
import tech.dut.fasto.web.rest.user.information.dto.response.UserProfileDtoResponse;
import tech.dut.fasto.web.rest.user.information.service.UserInformationService;

@RestController
@RequestMapping("/user/management/information")
@RequiredArgsConstructor
@Validated
@PreAuthorize("hasAuthority('" + AuthoritiesConstants.USER + "')")
public class UserInformationResource {
    private final UserInformationService userInformationService;

    @ApiOperation("API for user get profile")
    @GetMapping
    public ResponseEntity<UserProfileDtoResponse> getProfile() {
        return ResponseEntity.ok(userInformationService.getProfileUser());
    }

    @ApiOperation("API for user get profile")
    @PostMapping
    public ResponseEntity<UserProfileDtoResponse> updateProfile(@RequestBody UserProfileDtoRequest userProfileDtoRequest) {
        userInformationService.updateProfileUser(userProfileDtoRequest);
        return ResponseEntity.ok().build();
    }

    @ApiOperation("User Update Avatart")
    @PatchMapping("/update-avatar")
    public ResponseEntity<?> updateAvatar( @RequestBody UserAvatarDtoRequest avatar) {

        userInformationService.updateAvatar(avatar);
        return ResponseEntity.ok().build();
    }
    @ApiOperation("API for user get profile")
    @GetMapping("/other/{id}")
    public ResponseEntity<UserProfileDtoResponse> getProfileother(@PathVariable("id") Long id) {
        return ResponseEntity.ok(userInformationService.getProfileUserOther(id));
    }

}
