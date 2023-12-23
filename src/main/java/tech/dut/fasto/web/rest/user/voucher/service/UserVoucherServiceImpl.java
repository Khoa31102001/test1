package tech.dut.fasto.web.rest.user.voucher.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.dut.fasto.common.domain.Product;
import tech.dut.fasto.common.domain.Voucher;
import tech.dut.fasto.common.domain.enumeration.VoucherProvider;
import tech.dut.fasto.common.domain.enumeration.VoucherStatus;
import tech.dut.fasto.common.repository.ProductRepository;
import tech.dut.fasto.common.repository.VoucherRepository;

import tech.dut.fasto.common.service.impl.MessageService;
import tech.dut.fasto.errors.FastoAlertException;
import tech.dut.fasto.web.rest.user.voucher.dto.request.UserVoucherProductDetailRequestDto;
import tech.dut.fasto.web.rest.user.voucher.dto.request.UserVoucherProductRequestDto;
import tech.dut.fasto.web.rest.user.voucher.dto.response.VoucherUserResponseDto;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class UserVoucherServiceImpl implements UserVoucherService {

    private final VoucherRepository voucherRepository;

    private final ProductRepository productRepository;

    private final MessageService messageService;


    @Override
    @Transactional(readOnly = true)
    public List<VoucherUserResponseDto> getAllVoucherForBill(UserVoucherProductRequestDto userVoucherProductRequestDto) {
        Map<Long, UserVoucherProductDetailRequestDto> maps = userVoucherProductRequestDto.getOrderDetails().stream().collect(Collectors.toMap(UserVoucherProductDetailRequestDto::getProductId, Function.identity()));
        List<Long> ids = userVoucherProductRequestDto.getOrderDetails().stream().map(UserVoucherProductDetailRequestDto::getProductId).collect(Collectors.toList());
        List<Product> products = productRepository.getProductInIds(ids);
        BigDecimal totalOrigin = BigDecimal.valueOf(0L);
        for (Product product : products) {
            if (!product.getShop().getId().equals(userVoucherProductRequestDto.getShopId())) {
                throw new FastoAlertException(messageService.getMessage("error.code.voucher.get.voucher.for.bill.failed"), messageService.getMessage("error.product.not.found"));
            }
            UserVoucherProductDetailRequestDto productDetailRequestDto = maps.get(product.getId());
            BigDecimal tmp = product.getPrice().multiply(BigDecimal.valueOf(productDetailRequestDto.getAmount()));
            totalOrigin = totalOrigin.add(tmp);
        }
        return voucherRepository.getAllVoucherUser(userVoucherProductRequestDto.getShopId(), totalOrigin, VoucherProvider.ADMIN, Arrays.asList(VoucherStatus.DELETED, VoucherStatus.INACTIVE));
    }

    @Override
    @Transactional(readOnly = true)
    public VoucherUserResponseDto getDetailVoucher(Long voucherId) {
        VoucherUserResponseDto voucherUserResponseDto = new VoucherUserResponseDto();

        Optional<Voucher> voucherOptional = voucherRepository.findById(voucherId);
        if (voucherOptional.isPresent()) {
            BeanUtils.copyProperties(voucherOptional.get(), voucherUserResponseDto);
            voucherUserResponseDto.setShopId(voucherOptional.get().getShop().getId());
            voucherUserResponseDto.setShopName(voucherOptional.get().getName());
        } else {
            throw new FastoAlertException(messageService.getMessage("error.code.voucher.get.failed"), messageService.getMessage("error.voucher.not.existed"));
        }
        return voucherUserResponseDto;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<VoucherUserResponseDto> getAllVoucher(VoucherProvider voucherProvider, Pageable pageable, String query) {
        return voucherRepository.getAllVoucherForUser(pageable,voucherProvider, VoucherStatus.DELETED, query);
    }
}
