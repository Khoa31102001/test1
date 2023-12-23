package tech.dut.fasto.web.rest.user.product.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.dut.fasto.common.domain.Product;
import tech.dut.fasto.common.repository.ProductRepository;

import tech.dut.fasto.common.service.impl.MessageService;
import tech.dut.fasto.errors.FastoAlertException;
import tech.dut.fasto.web.rest.shop.product.dto.response.ShopProductResponseDto;
import tech.dut.fasto.web.rest.user.product.dto.response.UserProductResponseDto;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserProductServiceImpl implements UserProductService {
    private final ProductRepository productRepository;

    private final MessageService messageService;
    @Override
    @Transactional(readOnly = true)
    public Page<UserProductResponseDto> getTopProductShop(Pageable pageable, Long shopId, String query) {
        return productRepository.getTopProductShop(pageable,shopId,query);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ShopProductResponseDto> getProductShop(Pageable pageable, Long shopId, String query) {
        return productRepository.getAllProducts(pageable,shopId,query);
    }

    @Override
    @Transactional(readOnly = true)
    public UserProductResponseDto getDetail(Long id) {
        UserProductResponseDto productResponseDto = new UserProductResponseDto();
        Optional<Product> optionalProduct = productRepository.findByIdAndDeleteFlagIsFalse(id);
        if (optionalProduct.isEmpty()) {

            throw new FastoAlertException( messageService.getMessage("error.code.product.get.detail.product.failed"),  messageService.getMessage("error.product.not.found"));
        }else{
            Product product = optionalProduct.get();
            productResponseDto.setId(product.getId());
            productResponseDto.setCategoryName(product.getCategory().getName());
            productResponseDto.setName(product.getName());
            productResponseDto.setDescription(product.getDescription());
            productResponseDto.setImage(product.getImage());
            productResponseDto.setStatus(product.getStatus());
            productResponseDto.setPrice(product.getPrice());
            productResponseDto.setCountPay(product.getCountPay());
        }
        return productResponseDto;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<UserProductResponseDto> getAllProduct(Pageable pageable, String query, Double x, Double y) {
        return productRepository.getAllProductShop(pageable,query, x,y);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<UserProductResponseDto> getTopProduct(Pageable pageable) {
        return productRepository.getTopProduct(pageable);
    }
}
