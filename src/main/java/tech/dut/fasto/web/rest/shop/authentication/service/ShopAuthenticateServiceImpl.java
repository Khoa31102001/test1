package tech.dut.fasto.web.rest.shop.authentication.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.dut.fasto.common.domain.*;
import tech.dut.fasto.common.domain.enumeration.Provider;
import tech.dut.fasto.common.domain.enumeration.UserStatus;
import tech.dut.fasto.common.mapper.LocationMapper;
import tech.dut.fasto.common.repository.AddressRepository;
import tech.dut.fasto.common.repository.ShopRepository;
import tech.dut.fasto.common.repository.UserRepository;
import tech.dut.fasto.common.service.RoleService;
import tech.dut.fasto.common.service.impl.MessageService;
import tech.dut.fasto.config.security.AuthoritiesConstants;
import tech.dut.fasto.config.security.SecurityUtils;
import tech.dut.fasto.errors.FastoAlertException;
import tech.dut.fasto.util.constants.Constants;
import tech.dut.fasto.web.rest.shop.authentication.dto.DepositShopDto;
import tech.dut.fasto.web.rest.shop.authentication.dto.SignUpShopDto;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class ShopAuthenticateServiceImpl implements ShopAuthenticateService {
    private final UserRepository userRepository;

    private final RoleService roleService;

    private final PasswordEncoder passwordEncoder;

    private final LocationMapper locationMapper;

    private final AddressRepository addressRepository;

    private final ShopRepository shopRepository;

    private final MessageService messageService;


    @Override
    @Transactional(rollbackFor = FastoAlertException.class)
    public SignUpShopDto register(SignUpShopDto signUpShopDto) {
        if (userRepository.existsByEmailIgnoreCaseAndProvider(signUpShopDto.getEmail(), Provider.LOCAL)) {

            throw new FastoAlertException(messageService.getMessage("error.code.shop.create.failed"), messageService.getMessage("error.shop.mail.is.existed"));
        }
        if (null != signUpShopDto.getPhoneNumber() && userRepository.existsByPhoneNumberIgnoreCase(signUpShopDto.getPhoneNumber())) {
            throw new FastoAlertException(messageService.getMessage("error.code.shop.create.failed"), messageService.getMessage("error.shop.phone.number.is.existed"));
        }
        if (this.checkPasswordInValid(signUpShopDto.getPassword())) {
            throw new FastoAlertException(messageService.getMessage("error.code.shop.create.failed"), messageService.getMessage("error.authenticate.format.password.incorrect"));
        }
        User user = new User();
        user.setPassword(passwordEncoder.encode(signUpShopDto.getPassword()));
        user.setEmail(signUpShopDto.getEmail());
        user.setStatus(UserStatus.INACTIVE);
        user.setProvider(Provider.LOCAL);
        user.setExpiredTime(Instant.now().plus(1, ChronoUnit.DAYS));
        createUserRoles(user, AuthoritiesConstants.SHOP);

        if(addressRepository.findByXAndY(signUpShopDto.getLocationDto().getX(), signUpShopDto.getLocationDto().getY()).isPresent()){
            throw  new FastoAlertException(messageService.getMessage("error.code.shop.create.failed"), messageService.getMessage("error.shop.location.existed"));
        }

        if(shopRepository.findByNameIgnoreCase(signUpShopDto.getName()).isPresent()){
            throw  new FastoAlertException(messageService.getMessage("error.code.shop.create.failed"),messageService.getMessage("error.shop.name.existed"));
        }

        Location location = locationMapper.toEntity(signUpShopDto.getLocationDto());



        Shop shop = new Shop();
        shop.setName(signUpShopDto.getName());
        shop.setBanner(signUpShopDto.getBanner());
        shop.setDescription(signUpShopDto.getDescription());
        shop.setAddress(location);
        shop.setPhone(signUpShopDto.getPhoneNumber());
        shop.setUser(user);

        user.setShop(shop);
        userRepository.save(user);
        return signUpShopDto;
    }

    @Override
    @Transactional(readOnly = true)
    public DepositShopDto validateDeposit() {
        String email = SecurityUtils.getCurrentUserLogin().orElseThrow(() -> new FastoAlertException(messageService.getMessage("error.code.product.get.failed"), messageService.getMessage("error.authenticate.unauthorized.user")));
        Shop shop = shopRepository.findByUserEmailAndUserStatus(email, UserStatus.ACTIVATED).orElseThrow(() -> new FastoAlertException(messageService.getMessage("error.code.product.get.failed"), messageService.getMessage("error.shop.is.inactive")));
        DepositShopDto depositShopDto = new DepositShopDto();
        depositShopDto.setIsDeposit(shop.getIsDeposit());
        return depositShopDto;
    }

    private void createUserRoles(User user, String... roleNames) {
        Set<UserRole> userRoles = new HashSet<>();

        for (String roleName : roleNames) {
            Role role = roleService.findOneByName(roleName);
            UserRole userRole = new UserRole();
            userRole.setRole(role);
            userRole.setUser(user);
            userRoles.add(userRole);
        }

        user.setUserRoles(userRoles);
    }

    private boolean checkPasswordInValid(String password) {
        return !password.matches(Constants.PASSWORD_PATTERN);
    }
}
