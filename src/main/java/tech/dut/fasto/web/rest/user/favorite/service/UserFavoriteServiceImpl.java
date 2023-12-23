package tech.dut.fasto.web.rest.user.favorite.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.dut.fasto.common.domain.Shop;
import tech.dut.fasto.common.domain.ShopFavourite;
import tech.dut.fasto.common.domain.User;
import tech.dut.fasto.common.domain.enumeration.UserStatus;
import tech.dut.fasto.common.repository.ShopFavouriteRepository;
import tech.dut.fasto.common.repository.ShopRepository;
import tech.dut.fasto.common.repository.UserRepository;

import tech.dut.fasto.common.service.impl.MessageService;
import tech.dut.fasto.config.security.SecurityUtils;
import tech.dut.fasto.errors.FastoAlertException;
import tech.dut.fasto.web.rest.user.favorite.dto.response.ShopFavoriteResponseDto;

@Service
@RequiredArgsConstructor
public class UserFavoriteServiceImpl implements UserFavoriteService {

    private final UserRepository userRepository;

    private final ShopFavouriteRepository shopFavouriteRepository;

    private final ShopRepository shopRepository;

    private final MessageService messageService;

    @Override
    @Transactional(rollbackFor = FastoAlertException.class)
    public void createFavouriteShop(Long shopId) {

        String email = SecurityUtils.getCurrentUserLogin().orElseThrow(() -> new FastoAlertException(messageService.getMessage("error.code.favorite.shop.create.failed"), messageService.getMessage("error.authenticate.unauthorized.user")));
        User user = userRepository.findByEmailAndStatus(email, UserStatus.ACTIVATED).orElseThrow(() -> new FastoAlertException(messageService.getMessage("error.code.favorite.shop.create.failed"), messageService.getMessage("error.authenticate.unauthorized.user")));
        Shop shop = shopRepository.findByIdAndUserStatus(shopId, UserStatus.ACTIVATED).orElseThrow(() -> new FastoAlertException(messageService.getMessage("error.code.favorite.shop.create.failed"), messageService.getMessage("error.authenticate.user.not.activated")));
        ShopFavourite shopFavourite = new ShopFavourite();
        shopFavourite.setShop(shop);
        shopFavourite.setUser(user);
        shopFavouriteRepository.save(shopFavourite);
    }

    @Override
    @Transactional(rollbackFor = FastoAlertException.class)
    public void deleteFavouriteShop(Long id) {
        String email = SecurityUtils.getCurrentUserLogin().orElseThrow(() -> new FastoAlertException(messageService.getMessage("error.code.favorite.shop.delete.failed"), messageService.getMessage("error.authenticate.unauthorized.user")));
        User user = userRepository.findByEmailAndStatus(email, UserStatus.ACTIVATED).orElseThrow(() -> new FastoAlertException(messageService.getMessage("error.code.favorite.shop.delete.failed"), messageService.getMessage("error.authenticate.unauthorized.user")));

        ShopFavourite shopFavourite = shopFavouriteRepository.findByShopIdAndUserId(id, user.getId()).orElseThrow(() -> new FastoAlertException(messageService.getMessage("error.code.favorite.shop.delete.failed"), messageService.getMessage("error.favorite.shop.user.or.shop.not.found")));
        shopFavouriteRepository.delete(shopFavourite);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ShopFavoriteResponseDto> getAllFavouriteUsers(Pageable pageable) {
        String email = SecurityUtils.getCurrentUserLogin().orElseThrow(() -> new FastoAlertException(messageService.getMessage("error.code.favorite.shop.get.all.failed"), messageService.getMessage("error.authenticate.unauthorized.user")));
        User user = userRepository.findByEmailAndStatus(email, UserStatus.ACTIVATED).orElseThrow(() -> new FastoAlertException(messageService.getMessage("error.code.favorite.shop.get.all.failed"), messageService.getMessage("error.authenticate.unauthorized.user")));
        return shopFavouriteRepository.getAllShopFavorite(user.getId(),pageable);
    }
}
