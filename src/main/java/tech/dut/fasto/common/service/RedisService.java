package tech.dut.fasto.common.service;

import tech.dut.fasto.common.dto.CartDto;

import java.util.List;
import java.util.Map;

public interface RedisService {
    void setCarts(final String key,  Map<String,List<CartDto>> map);

    Map<String,List<CartDto>> getCarts(final String key);

}
