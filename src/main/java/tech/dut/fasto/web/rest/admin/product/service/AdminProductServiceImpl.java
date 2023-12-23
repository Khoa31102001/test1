package tech.dut.fasto.web.rest.admin.product.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.dut.fasto.common.domain.enumeration.ProductStatus;
import tech.dut.fasto.common.repository.ProductRepository;

import tech.dut.fasto.common.service.impl.MessageService;
import tech.dut.fasto.errors.FastoAlertException;
import tech.dut.fasto.web.rest.admin.product.dto.response.AdminProductResponseDto;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AdminProductServiceImpl implements AdminProductService {
    private final ProductRepository productRepository;

    private final MessageService messageService;


    @Transactional(readOnly = true)
    @Override
    public Page<AdminProductResponseDto> getAllProducts(Pageable pageable, ProductStatus status, String query) {

        Page<AdminProductResponseDto> dtoPage;

        if (status == null) {
            dtoPage = productRepository.getAllProductsByAdmin(pageable, query);
        } else {
            dtoPage = productRepository.getAllProductsAndStatusByAdmin(pageable, status, query);
        }
        return dtoPage;
    }

    @Transactional(readOnly = true)
    @Override
    public AdminProductResponseDto getDetail(Long id) throws FastoAlertException {
        Optional<AdminProductResponseDto> optionalProduct = productRepository.getDetailsByAdmin(id);

        if (optionalProduct.isPresent()) {
            return optionalProduct.get();
        } else {
            throw new FastoAlertException(this.messageService.getMessage("error.code.product.get.detail.product.failed"), this.messageService.getMessage("error.product.not.found"));
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AdminProductResponseDto> getAllProductShop(Pageable pageable, ProductStatus status, String query, Long shopId) {
        Page<AdminProductResponseDto> dtoPage;

        if (status == null) {
            dtoPage = productRepository.getAllProductsShopByAdmin(pageable, query, shopId);
        } else {
            dtoPage = productRepository.getAllProductsShopAndStatusByAdmin(pageable, status, query, shopId);
        }
        return dtoPage;
    }
}
