package tech.dut.fasto.web.rest.admin.user;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import tech.dut.fasto.common.anotation.SwaggerPageable;
import tech.dut.fasto.common.domain.enumeration.UserStatus;
import tech.dut.fasto.config.security.AuthoritiesConstants;
import tech.dut.fasto.web.rest.admin.user.dto.response.AdminUserResponseDto;
import tech.dut.fasto.web.rest.admin.user.service.AdminUserService;

@RestController
@RequestMapping("/admin/management/users")
@RequiredArgsConstructor
@Validated
@PreAuthorize("hasAuthority('" + AuthoritiesConstants.ADMIN + "')")
public class AdminUserResource {

    private final AdminUserService adminUserService;

    @ApiOperation("Block User")
    @PatchMapping(path = "/block/{id}")
    public ResponseEntity<String> blockUser(@PathVariable("id") Long id) {
        adminUserService.blockUser(id);
        return ResponseEntity.ok().build();
    }

    @ApiOperation("API for get all users")
    @GetMapping
    @SwaggerPageable
    public ResponseEntity<Page<AdminUserResponseDto>> getAllUser(
             @PageableDefault(sort = "modifiedAt", direction = Sort.Direction.DESC)
            Pageable pageable,
            @ApiParam(value = "Search by name")
            @RequestParam(required = false) String query,
            @ApiParam(value = "Search by status")
            @RequestParam(required = false) UserStatus status) {
        return ResponseEntity.ok().body(adminUserService.getAllListUser(pageable, query, status));
    }

}
