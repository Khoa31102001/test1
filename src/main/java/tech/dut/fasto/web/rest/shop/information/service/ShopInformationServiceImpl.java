package tech.dut.fasto.web.rest.shop.information.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.dut.fasto.common.domain.Shop;
import tech.dut.fasto.common.domain.ShopImage;
import tech.dut.fasto.common.domain.enumeration.UserStatus;
import tech.dut.fasto.common.repository.AddressRepository;
import tech.dut.fasto.common.repository.ShopRepository;
import tech.dut.fasto.common.service.impl.MessageService;
import tech.dut.fasto.config.security.SecurityUtils;
import tech.dut.fasto.errors.FastoAlertException;
import tech.dut.fasto.web.rest.shop.information.dto.request.ShopInformationRequestDto;
import tech.dut.fasto.web.rest.shop.information.dto.response.ShopProfileResponseDto;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RequiredArgsConstructor
@Service
public class ShopInformationServiceImpl implements ShopInformationService {
    private final ShopRepository shopRepository;

    private final AddressRepository addressRepository;

    private final MessageService messageService;


    @Override
    @Transactional(readOnly = true)
    public ShopProfileResponseDto getShopInfo() {

        String email = SecurityUtils.getCurrentUserLogin().orElseThrow(() -> new FastoAlertException(messageService.getMessage("error.code.shop.get.info.failed"), messageService.getMessage("error.authenticate.unauthorized.user")));

        Shop shop = shopRepository.findByUserEmailAndUserStatus(email, UserStatus.ACTIVATED).orElseThrow(() -> new FastoAlertException(messageService.getMessage("error.code.shop.get.info.failed"), messageService.getMessage("error.shop.is.inactive")));
        ShopProfileResponseDto shopProfileResponseDto = new ShopProfileResponseDto();
        shopProfileResponseDto.setId(shop.getId());
        shopProfileResponseDto.setName(shop.getName());
        shopProfileResponseDto.setLogo(shop.getBanner());
        shopProfileResponseDto.setDescription(shop.getDescription());
        shopProfileResponseDto.setPhone(shop.getPhone());
        List<String> imagesShop = shop.getShopImages().stream().map(ShopImage::getImage).toList();
        shopProfileResponseDto.setImageShops(imagesShop);
        shopProfileResponseDto.setRantings(shop.getRatings());
        shopProfileResponseDto.setCity(shop.getAddress().getCity());
        shopProfileResponseDto.setCountry(shop.getAddress().getCountry());
        shopProfileResponseDto.setProvince(shop.getAddress().getStateProvince());
        shopProfileResponseDto.setStreet(shop.getAddress().getStreetAddress());
        shopProfileResponseDto.setX(shop.getAddress().getX());
        shopProfileResponseDto.setY(shop.getAddress().getY());
        shopProfileResponseDto.setStartRatings(shop.getStartRatings());
        return shopProfileResponseDto;
    }

    @Override
    @Transactional(rollbackFor = FastoAlertException.class)
    public void updateShopInfo(ShopInformationRequestDto shopInformationRequestDto) {
        String email = SecurityUtils.getCurrentUserLogin().orElseThrow(() -> new FastoAlertException(messageService.getMessage("error.code.shop.update.info.failed"), messageService.getMessage("error.authenticate.unauthorized.user")));
        Shop shop = shopRepository.findByUserEmailAndUserStatus(email, UserStatus.ACTIVATED).orElseThrow(() -> new FastoAlertException(messageService.getMessage("error.code.shop.update.info.failed"), messageService.getMessage("error.shop.is.inactive")));
        if (null != shopInformationRequestDto.getName()) {
            shop.setName(shopInformationRequestDto.getName());
        }

        if (addressRepository.existsByXAndYAndShopIsNot(shopInformationRequestDto.getX(), shopInformationRequestDto.getY(), shop)) {
            throw new FastoAlertException(messageService.getMessage("error.code.shop.update.info.failed"), messageService.getMessage("error.shop.address.existed"));
        }

        if (null != shopInformationRequestDto.getImages() && !shopInformationRequestDto.getImages().isEmpty()) {
            Set<ShopImage> shopImages = new HashSet<>();
            for (String image : shopInformationRequestDto.getImages()) {
                ShopImage shopImage = new ShopImage();
                shopImage.setImage(image);
                shopImages.add(shopImage);
            }
            shop.getShopImages().addAll(shopImages);
        }

        if (null != shopInformationRequestDto.getCity()) {
            shop.getAddress().setCity(shopInformationRequestDto.getCity());
        }

        if (null != shopInformationRequestDto.getCountry()) {
            shop.getAddress().setCountry(shopInformationRequestDto.getCountry());
        }

        if (null != shopInformationRequestDto.getStreet()) {
            shop.getAddress().setStreetAddress(shopInformationRequestDto.getStreet());
        }

        if (null != shopInformationRequestDto.getProvince()) {
            shop.getAddress().setStateProvince(shopInformationRequestDto.getProvince());
        }
        if (null != shopInformationRequestDto.getX()) {
            shop.getAddress().setX(shopInformationRequestDto.getX());
        }
        if (null != shopInformationRequestDto.getY()) {
            shop.getAddress().setY(shopInformationRequestDto.getY());
        }
        if (null != shopInformationRequestDto.getBanner()) {
            shop.setBanner(shopInformationRequestDto.getBanner());
        }

    }
}
