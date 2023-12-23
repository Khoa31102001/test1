package tech.dut.fasto.web.rest.user.shop.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.dut.fasto.common.domain.RecentShop;
import tech.dut.fasto.common.domain.Shop;
import tech.dut.fasto.common.domain.User;
import tech.dut.fasto.common.domain.enumeration.ProductStatus;
import tech.dut.fasto.common.domain.enumeration.UserStatus;
import tech.dut.fasto.common.domain.enumeration.VoucherStatus;
import tech.dut.fasto.common.repository.*;
import tech.dut.fasto.common.service.impl.MessageService;
import tech.dut.fasto.config.security.AuthoritiesConstants;
import tech.dut.fasto.config.security.SecurityUtils;

import tech.dut.fasto.errors.FastoAlertException;
import tech.dut.fasto.util.AppUtil;
import tech.dut.fasto.web.rest.shop.voucher.dto.response.ShopVoucherResponseDto;
import tech.dut.fasto.web.rest.user.shop.dto.response.UserShopLocationResponseDto;
import tech.dut.fasto.web.rest.user.shop.dto.response.UserShopProductResponseDto;
import tech.dut.fasto.web.rest.user.shop.dto.response.UserShopProfileResponseDto;
import tech.dut.fasto.web.rest.user.shop.dto.response.UserShopResponseDto;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserShopServiceImpl implements UserShopService {

    private final ShopRepository shopRepository;

    private final ProductRepository productRepository;

    private final VoucherRepository voucherRepository;

    private final BillRepository billRepository;

    private final UserRepository userRepository;

    private final RecentShopRepository recentShopRepository;

    private final ShopFavouriteRepository shopFavouriteRepository;

    private final MessageService messageService;

    @Override
    @Transactional(readOnly = true)
    public UserShopResponseDto getDetailShop(Long shopId, Double x, Double y) {

        Shop shop = shopRepository.findById(shopId).orElseThrow(() -> new FastoAlertException(messageService.getMessage("error.code.shop.get.detail.failed"), messageService.getMessage("error.shop.not.found")));
        UserShopProfileResponseDto shopProfileResponseDto = new UserShopProfileResponseDto();
        shopProfileResponseDto.setId(shop.getId());
        shopProfileResponseDto.setName(shop.getName());
        shopProfileResponseDto.setBanner(shop.getBanner());
        shopProfileResponseDto.setDescription(shop.getDescription());
        shopProfileResponseDto.setPhone(shop.getPhone());

        String authorize = SecurityUtils.getCurrentUserLogin().get();
        if (!AuthoritiesConstants.ANOYMOUS.equals(authorize) ) {
            User user = userRepository.findByEmailAndStatus(authorize, UserStatus.ACTIVATED).orElseThrow(() -> new FastoAlertException(messageService.getMessage("error.code.shop.get.detail.failed"), messageService.getMessage("error.authenticate.user.already.activated")));
            shopProfileResponseDto.setIsFavorite(shopFavouriteRepository.findByShopIdAndUserId(shopId, user.getId()).isPresent());
        }
        shopProfileResponseDto.setRanting(shop.getRatings());
        shopProfileResponseDto.setStartRatings(shop.getStartRatings());
        shopProfileResponseDto.setCity(shop.getAddress().getCity());
        shopProfileResponseDto.setCountry(shop.getAddress().getCountry());
        shopProfileResponseDto.setStateProvince(shop.getAddress().getStateProvince());
        shopProfileResponseDto.setStreetAddress(shop.getAddress().getStreetAddress());
        shopProfileResponseDto.setX(shop.getAddress().getX());
        shopProfileResponseDto.setY(shop.getAddress().getY());
        shopProfileResponseDto.setDistance(AppUtil.getDistanceFromLatLonInKm(x,y,shop.getAddress().getX(), shop.getAddress().getY() ));

        List<UserShopProductResponseDto> productResponseDtos = productRepository.getListAllProductsAndStatus(shopId, ProductStatus.HOT);

        List<ShopVoucherResponseDto> voucherResponseDtos = voucherRepository.getListAllVouchers(shopId, VoucherStatus.ACTIVATED);

        UserShopResponseDto userShopResponseDto = new UserShopResponseDto();
        userShopResponseDto.setShopProfile(shopProfileResponseDto);
        userShopResponseDto.setListProduct(productResponseDtos);
        userShopResponseDto.setListVoucher(voucherResponseDtos);

        return userShopResponseDto;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<UserShopLocationResponseDto> getAllLocationByCurrentLocation(Double x, Double y, Double radius, Pageable pageable) {

        Page<UserShopLocationResponseDto> userLocations = shopRepository.findAllShopLocationByName(radius, x, y,pageable);
        for (UserShopLocationResponseDto userLocation : userLocations) {
            userLocation.setDistance(AppUtil.getDistanceFromLatLonInKm(userLocation.getX(), userLocation.getY(), x, y));
        }
        return userLocations;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<UserShopLocationResponseDto> getAllShopLocationByCurrentLocation(Long shopId, Double radius, Pageable pageable) {
        Shop shop = shopRepository.findById(shopId).orElseThrow(() -> new FastoAlertException(messageService.getMessage("error.code.shop.get.by.location.failed"), messageService.getMessage("error.shop.not.found")));

        Page<UserShopLocationResponseDto> userLocations = shopRepository.findAllShopLocationByName(radius, shopId,pageable);

        for (UserShopLocationResponseDto userLocation : userLocations) {
            userLocation.setDistance(AppUtil.getDistanceFromLatLonInKm(userLocation.getX(), userLocation.getY(), shop.getAddress().getX(), shop.getAddress().getY()));
        }
        return userLocations;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<UserShopProfileResponseDto> getAllShops(String query, Pageable pageable) {
        return shopRepository.getAlLUserShop(query, pageable);
    }

    @Override
    @Transactional(rollbackFor = FastoAlertException.class)
    public void createRecentShop(Long shopId) {
        Shop shop = shopRepository.findById(shopId).orElseThrow(() -> new FastoAlertException(messageService.getMessage("error.code.recent.shop.create.failed"), messageService.getMessage("error.shop.not.found")));

        String email = SecurityUtils.getCurrentUserLogin().orElseThrow(() -> new FastoAlertException(messageService.getMessage("error.code.recent.shop.create.failed"), messageService.getMessage("error.authenticate.unauthorized.user")));
        User user = userRepository.findByEmailAndStatus(email, UserStatus.ACTIVATED).orElseThrow(() -> new FastoAlertException(messageService.getMessage("error.code.recent.shop.create.failed"), messageService.getMessage("error.authenticate.user.already.activated")));

        Optional<RecentShop> optional = recentShopRepository.findByUserIdAndShopId(user.getId(), shop.getId());
        if (optional.isPresent()) {
            RecentShop recentShop = optional.get();
            recentShopRepository.delete(recentShop);
        }
        RecentShop recentShop = new RecentShop();
        recentShop.setShop(shop);
        recentShop.setUser(user);
        recentShopRepository.save(recentShop);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<UserShopProfileResponseDto> getRecentShop(Pageable pageable) {
        String email = SecurityUtils.getCurrentUserLogin().orElseThrow(() -> new FastoAlertException(messageService.getMessage("error.code.recent.shop.get.failed"), messageService.getMessage("error.authenticate.unauthorized.user")));
        User user = userRepository.findByEmailAndStatus(email, UserStatus.ACTIVATED).orElseThrow(() -> new FastoAlertException(messageService.getMessage("error.code.recent.shop.get.failed"), messageService.getMessage("error.authenticate.user.already.activated")));

        return recentShopRepository.getAllRecentShop(user.getId(), pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<UserShopProfileResponseDto> getTopShops(Pageable pageable) {
        return shopRepository.getTopShop(pageable);
    }


}
