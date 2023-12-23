package tech.dut.fasto.web.rest.admin.image;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import tech.dut.fasto.config.security.AuthoritiesConstants;
import tech.dut.fasto.web.rest.admin.image.dto.request.AdminImageVoucherRequestDto;
import tech.dut.fasto.web.rest.admin.image.service.AdminImageService;

import java.util.List;

@RestController
@RequestMapping("/admin/management")
@RequiredArgsConstructor
@Validated
@PreAuthorize("hasAuthority('" + AuthoritiesConstants.ADMIN + "')")
public class AdminImageResource {
    private final AdminImageService adminImageService;

    @PostMapping("/voucher/{id}/images")
    public ResponseEntity<?> createVoucherImage(@PathVariable Long id, @RequestBody AdminImageVoucherRequestDto adminImageVoucherRequestDto){
        adminImageService.createImageForVoucher(adminImageVoucherRequestDto);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/voucher/{id}/images")
    public ResponseEntity<List<String>> getVoucherImage(@PathVariable Long id){

        return ResponseEntity.ok(adminImageService.getImageForVoucher(id));
    }
}
