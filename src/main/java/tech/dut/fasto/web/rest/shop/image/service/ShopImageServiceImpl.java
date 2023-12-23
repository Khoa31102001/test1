package tech.dut.fasto.web.rest.shop.image.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.dut.fasto.common.domain.*;
import tech.dut.fasto.common.domain.enumeration.UserStatus;
import tech.dut.fasto.common.repository.*;

import tech.dut.fasto.common.service.impl.MessageService;
import tech.dut.fasto.config.security.SecurityUtils;

import tech.dut.fasto.errors.FastoAlertException;
import tech.dut.fasto.web.rest.shop.image.dto.request.ShopImageRequestDto;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ShopImageServiceImpl implements ShopImageService {
    private final VoucherRepository voucherRepository;

    private final VoucherImageRepository voucherImageRepository;

    private final ShopImageRepository shopImageRepository;

    private final ShopRepository shopRepository;

    private final ProductRepository productRepository;

    private final ProductImageRepository productImageRepository;

    private final MessageService messageService;

    @Override
    @Transactional(rollbackFor = FastoAlertException.class)
    public void createImageForVoucher(ShopImageRequestDto shopImageRequestDto) {

        String email = SecurityUtils.getCurrentUserLogin().orElseThrow(() -> new FastoAlertException(messageService.getMessage("error.code.product.create.failed"), messageService.getMessage("error.authenticate.unauthorized.user")));

        Shop shop = shopRepository.findByUserEmailAndUserStatus(email, UserStatus.ACTIVATED).orElseThrow(() -> new FastoAlertException(messageService.getMessage("error.code.product.create.failed"), messageService.getMessage("error.shop.is.inactive")));


        Optional<Voucher> voucherOptional = voucherRepository.findByIdAndShopId(shopImageRequestDto.getId(), shop.getId());

        if (voucherOptional.isPresent() && !shopImageRequestDto.getImages().isEmpty()) {
            Voucher voucher = voucherOptional.get();
            List<VoucherImage> voucherImageList = new ArrayList<>();
            for (String item : shopImageRequestDto.getImages()) {
                VoucherImage voucherImage = new VoucherImage();
                voucherImage.setImage(item);
                voucherImage.setVoucher(voucher);
                voucherImageList.add(voucherImage);
            }
            voucherImageRepository.saveAll(voucherImageList);
        } else {
            throw new FastoAlertException(messageService.getMessage("error.code.voucher.create.image.failed"), messageService.getMessage("error.voucher.not.existed"));
        }
    }

    @Override
    @Transactional(rollbackFor = FastoAlertException.class)
    public void createImageForShop(ShopImageRequestDto shopImageRequestDto) {
        String email = SecurityUtils.getCurrentUserLogin().orElseThrow(() -> new FastoAlertException(messageService.getMessage("error.code.product.create.failed"), messageService.getMessage("error.authenticate.unauthorized.user")));

        Shop shop = shopRepository.findByUserEmailAndUserStatus(email, UserStatus.ACTIVATED).orElseThrow(() -> new FastoAlertException(messageService.getMessage("error.code.product.create.failed"), messageService.getMessage("error.shop.is.inactive")));
        if (!shopImageRequestDto.getImages().isEmpty()) {
            List<ShopImage> shopImageList = new ArrayList<>();
            for (String item : shopImageRequestDto.getImages()) {
                ShopImage shopImage = new ShopImage();
                shopImage.setImage(item);
                shopImage.setShop(shop);
                shopImageList.add(shopImage);
            }
            shopImageRepository.saveAll(shopImageList);
        }
        else{
            throw new FastoAlertException(messageService.getMessage("error.code.voucher.create.image.failed"), messageService.getMessage("error.shop.image.not.found"));

        }

    }

    @Override
    @Transactional(rollbackFor = FastoAlertException.class)
    public void createImageForProduct(ShopImageRequestDto shopImageRequestDto) {
        String email = SecurityUtils.getCurrentUserLogin().orElseThrow(() -> new FastoAlertException(messageService.getMessage("error.code.product.create.failed"), messageService.getMessage("error.authenticate.unauthorized.user")));

        Shop shop = shopRepository.findByUserEmailAndUserStatus(email, UserStatus.ACTIVATED).orElseThrow(() -> new FastoAlertException(messageService.getMessage("error.code.product.create.failed"), messageService.getMessage("error.shop.is.inactive")));

        Optional<Product> productOptional = productRepository.findByIdAndShopId(shopImageRequestDto.getId(), shop.getId());
        if(productOptional.isPresent() && !shopImageRequestDto.getImages().isEmpty())
        {
            Product product = productOptional.get();
            List<ProductImage> productImages = new ArrayList<>();
            for (String item : shopImageRequestDto.getImages()) {
                 ProductImage productImage = new ProductImage();
                productImage.setImage(item);
                productImage.setProduct(product);
                productImages.add(productImage);
            }
            productImageRepository.saveAll(productImages);
        }
        else{
            throw new FastoAlertException(messageService.getMessage("error.code.product.image.create.failed"),messageService.getMessage("error.product.image.not.found"));
        }

    }

    @Override
    @Transactional(readOnly = true)
    public List<String> getImageForVoucher(Long id) {
        return voucherImageRepository.findAllByVoucherId(id).stream().map(VoucherImage::getImage).toList();

    }

    @Override
    @Transactional(readOnly = true)
    public List<String> getImageForShop(Long id) {
        return shopImageRepository.findAllByShopId(id).stream().map(ShopImage::getImage).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<String> getImageForProduct(Long id) {
        return productImageRepository.findAllByProductId(id).stream().map(ProductImage::getImage).toList();
    }

}
