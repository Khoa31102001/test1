package tech.dut.fasto.web.rest.shop.product.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.dut.fasto.common.domain.Category;
import tech.dut.fasto.common.domain.Product;
import tech.dut.fasto.common.domain.Shop;
import tech.dut.fasto.common.domain.enumeration.ProductStatus;
import tech.dut.fasto.common.domain.enumeration.UserStatus;
import tech.dut.fasto.common.repository.CategoryRepository;
import tech.dut.fasto.common.repository.ProductRepository;
import tech.dut.fasto.common.repository.ShopRepository;
import tech.dut.fasto.common.service.impl.MessageService;
import tech.dut.fasto.config.security.SecurityUtils;
import tech.dut.fasto.errors.FastoAlertException;
import tech.dut.fasto.web.rest.shop.product.dto.request.ShopProductRequestDto;
import tech.dut.fasto.web.rest.shop.product.dto.response.ShopProductResponseDto;
import tech.dut.fasto.web.rest.shop.product.mapper.ShopProductDtoMapper;
import tech.dut.fasto.web.rest.shop.product.mapper.ShopProductEntityMapper;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ShopProductServiceImpl implements ShopProductService {
    private final ProductRepository productRepository;

    private final CategoryRepository categoryRepository;

    private final ShopRepository shopRepository;

    private final ShopProductEntityMapper productEntityMapper;

    private final ShopProductDtoMapper shopProductDtoMapper;

    private final MessageService messageService;


    @Transactional(rollbackFor = FastoAlertException.class)
    @Override
    public void createProduct(ShopProductRequestDto shopProductRequestDto) {

        String email = SecurityUtils.getCurrentUserLogin().orElseThrow(() -> new FastoAlertException(messageService.getMessage("error.code.product.create.failed"), messageService.getMessage("error.authenticate.unauthorized.user")));

        Shop shop = shopRepository.findByUserEmailAndUserStatus(email, UserStatus.ACTIVATED).orElseThrow(() -> new FastoAlertException(messageService.getMessage("error.code.product.create.failed"),messageService.getMessage("error.shop.is.inactive")));

        Category category = categoryRepository.findByIdAndDeleteFlagIsFalse(shopProductRequestDto.getCategoryId()).orElseThrow(() -> new FastoAlertException(messageService.getMessage("error.code.product.create.failed"), messageService.getMessage("error.categories.not.existed")));

        Product product = productEntityMapper.toEntity(shopProductRequestDto);
        product.setCountPay(0L);
        product.setDeleteFlag(false);
        product.setCategory(category);
        product.setShop(shop);

        productRepository.save(product);
    }

    @Transactional(readOnly = true)
    @Override
    public Page<ShopProductResponseDto> getAllProducts(Pageable pageable, ProductStatus status, String query) {
        String email = SecurityUtils.getCurrentUserLogin().orElseThrow(() -> new FastoAlertException(messageService.getMessage("error.code.product.get.failed"), messageService.getMessage("error.authenticate.unauthorized.user")));
        Shop shop = shopRepository.findByUserEmailAndUserStatus(email, UserStatus.ACTIVATED).orElseThrow(() -> new FastoAlertException(messageService.getMessage("error.code.product.get.failed"), messageService.getMessage("error.shop.is.inactive")));

        Page<ShopProductResponseDto> dtoPage;

        if (status == null) {
            dtoPage = productRepository.getAllProducts(pageable, shop.getId(), query);
        } else {
            dtoPage = productRepository.getAllProductsAndStatus(pageable, shop.getId(), status, query);
        }

        return dtoPage;
    }

    @Transactional(rollbackFor = FastoAlertException.class)
    @Override
    public void deleteProduct(Long productId) throws FastoAlertException {
        String email = SecurityUtils.getCurrentUserLogin().orElseThrow(() -> new FastoAlertException(messageService.getMessage("error.code.product.delete.failed"),messageService.getMessage("error.authenticate.unauthorized.user")));
        Shop shop = shopRepository.findByUserEmailAndUserStatus(email, UserStatus.ACTIVATED).orElseThrow(() -> new FastoAlertException(messageService.getMessage("error.code.product.delete.failed"),messageService.getMessage("error.shop.is.inactive")));

        Optional<Product> optionalProduct = productRepository.findByIdAndShopId(productId, shop.getId());

        if (optionalProduct.isPresent()) {
            Product product = optionalProduct.get();
            product.setDeleteFlag(true);
            product.setStatus(ProductStatus.DELETED);
        } else {
            throw new FastoAlertException(messageService.getMessage("error.code.product.delete.failed"), messageService.getMessage("error.product.not.found"));
        }
    }

    @Transactional(rollbackFor = FastoAlertException.class)
    @Override
    public void updateProduct(ShopProductRequestDto shopProductRequestDto) {
        String email = SecurityUtils.getCurrentUserLogin().orElseThrow(() -> new FastoAlertException(messageService.getMessage("error.code.product.update.failed"), messageService.getMessage("error.authenticate.unauthorized.user")));
        Shop shop = shopRepository.findByUserEmailAndUserStatus(email, UserStatus.ACTIVATED).orElseThrow(() -> new FastoAlertException(messageService.getMessage("error.code.product.update.failed"), messageService.getMessage("error.shop.is.inactive")));
        Optional<Product> optionalProduct = productRepository.findByIdAndShopIdAndDeleteFlagIsFalse(shopProductRequestDto.getId(), shop.getId());

        if (optionalProduct.isPresent()) {
            Product product = optionalProduct.get();
            Category category = categoryRepository.findById(shopProductRequestDto.getCategoryId()).orElseThrow(() -> new FastoAlertException(messageService.getMessage("error.code.product.update.failed"), messageService.getMessage("error.categories.not.existed")));
            productEntityMapper.partialUpdate(product, shopProductRequestDto);
            product.setCategory(category);
        } else {
            throw new FastoAlertException("Update product failed", "Shop doesn't have this product");
        }

    }

    @Transactional(readOnly = true)
    @Override
    public ShopProductResponseDto getDetail(Long id) throws FastoAlertException {
        String email = SecurityUtils.getCurrentUserLogin().orElseThrow(() -> new FastoAlertException(messageService.getMessage("error.code.product.get.failed"), messageService.getMessage("error.authenticate.unauthorized.user")));
        Shop shop = shopRepository.findByUserEmailAndUserStatus(email, UserStatus.ACTIVATED).orElseThrow(() -> new FastoAlertException(messageService.getMessage("error.code.product.get.failed"), messageService.getMessage("error.shop.is.inactive")));
        Optional<Product> optionalProduct = productRepository.findByIdAndShopIdAndDeleteFlagIsFalse(id, shop.getId());
        ShopProductResponseDto shopProductResponseDto;

        if (optionalProduct.isPresent()) {
            shopProductResponseDto = shopProductDtoMapper.toDto(optionalProduct.get());
        } else {
            throw new FastoAlertException(messageService.getMessage("error.code.product.get.failed"), messageService.getMessage("error.product.not.found"));
        }
        return shopProductResponseDto;
    }
}
