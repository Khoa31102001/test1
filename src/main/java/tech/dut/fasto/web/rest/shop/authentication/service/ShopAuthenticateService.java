package tech.dut.fasto.web.rest.shop.authentication.service;

import tech.dut.fasto.web.rest.shop.authentication.dto.DepositShopDto;
import tech.dut.fasto.web.rest.shop.authentication.dto.SignUpShopDto;

public interface ShopAuthenticateService {
    SignUpShopDto register(SignUpShopDto signUpShopDto);

    DepositShopDto validateDeposit();
}
