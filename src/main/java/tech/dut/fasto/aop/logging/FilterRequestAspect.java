package tech.dut.fasto.aop.logging;

import lombok.RequiredArgsConstructor;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import tech.dut.fasto.common.domain.Shop;
import tech.dut.fasto.common.domain.enumeration.UserStatus;
import tech.dut.fasto.common.repository.ShopRepository;
import tech.dut.fasto.common.service.impl.MessageService;
import tech.dut.fasto.config.security.SecurityUtils;
import tech.dut.fasto.errors.FastoAlertException;

@Aspect
@RequiredArgsConstructor
public class FilterRequestAspect {


    private final ShopRepository shopRepository;

    private final MessageService messageService;

    @Pointcut(
            "within(tech.dut.fasto.web.rest.shop..*)" +
                    " && !within(tech.dut.fasto.web.rest.shop.authentication..*)" +
                    " && !within(tech.dut.fasto.web.rest.shop.information..*)" +
                    " && !within(tech.dut.fasto.web.rest.shop.image..*)" +
                    " && !within(tech.dut.fasto.web.rest.shop.vnpay..*)"
    )
    public void applicationPackageShopPointcut() {
    }

    @Before("applicationPackageShopPointcut()")
    public void filterSecurityRequest() {
        if (SecurityUtils.getCurrentUserLogin().isPresent()) {
            String email = SecurityUtils.getCurrentUserLogin().orElseThrow(() -> new FastoAlertException(messageService.getMessage("error.code.product.create.failed"), messageService.getMessage("error.authenticate.unauthorized.user")));
            Shop shop = shopRepository.findByUserEmailAndUserStatus(email, UserStatus.ACTIVATED).orElseThrow(() -> new FastoAlertException(messageService.getMessage("error.code.product.create.failed"), messageService.getMessage("error.shop.is.inactive")));
            if (Boolean.FALSE.equals(shop.getIsDeposit())) {
                throw new FastoAlertException(messageService.getMessage("error.authenticate.forbidden"),messageService.getMessage("error.deposit.shop"));
            }
        }
    }


}
