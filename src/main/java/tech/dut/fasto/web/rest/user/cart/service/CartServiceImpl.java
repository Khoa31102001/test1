package tech.dut.fasto.web.rest.user.cart.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.dut.fasto.common.domain.User;
import tech.dut.fasto.common.domain.enumeration.UserStatus;
import tech.dut.fasto.common.dto.CartDto;
import tech.dut.fasto.common.repository.UserRepository;

import tech.dut.fasto.common.service.RedisService;
import tech.dut.fasto.common.service.impl.MessageService;
import tech.dut.fasto.config.security.SecurityUtils;
import tech.dut.fasto.errors.FastoAlertException;
import tech.dut.fasto.web.rest.user.cart.dto.UserCartDto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {
    private final RedisService redisService;

    private final UserRepository userRepository;

    private final MessageService messageService;

    
    @Override
    @Transactional
    public Map<String,List<CartDto>> getAllCarts(String deviceToken) {
        return redisService.getCarts(deviceToken);
    }


    @Override
    @Transactional
    public void updateCart(UserCartDto cartDto) {
        List<CartDto> listCarts = cartDto.getCartDtos();
         if(!listCarts.isEmpty()){
             Map<String, List<CartDto>> myMapCart = getAllCarts(cartDto.getDeviceToken());
             if (myMapCart != null  ) {
                 if(myMapCart.get(cartDto.getShopId().toString())!=null){
                     Map<Long,CartDto> myTmpCart = myMapCart.get(cartDto.getShopId().toString()).stream().collect(Collectors.toMap(CartDto::getId,
                             Function.identity()));
                     List<CartDto> tmp = myMapCart.get(cartDto.getShopId().toString());


                     for(CartDto item : listCarts){
                         Long key = item.getId();
                         if(myTmpCart.get(key) != null){
                             myTmpCart.get(key).setAmount(item.getAmount());

                         }else {

                                 tmp.add(item);


                         }
                     }
                 }else{
                     myMapCart.put(cartDto.getShopId().toString(), cartDto.getCartDtos());
                 }

             }
             else{
                 myMapCart = new HashMap<>();
                 myMapCart.put(cartDto.getShopId().toString(), cartDto.getCartDtos());
             }

             for(List<CartDto> item : myMapCart.values()){
                 item.removeIf((e->e.getAmount()<1));
             }
             List<String> strings = new ArrayList<>();
             for(String key : myMapCart.keySet()){
                 if(myMapCart.get(key)==null || myMapCart.get(key).isEmpty()){
                     strings.add(key);
                 }
             }

             for(String item : strings){
                 myMapCart.remove(item);
             }
            redisService.setCarts(cartDto.getDeviceToken(), myMapCart);
        }
    }

    @Override
    public void removeProductCart(List<Long> productIds, Long shopId, String deviceToken) {
        String email = SecurityUtils.getCurrentUserLogin().orElseThrow(() -> new FastoAlertException(messageService.getMessage("error.code.cart.remove.failed"), messageService.getMessage("error.authenticate.unauthorized.user")));
        User user = userRepository.findByEmailAndStatus(email, UserStatus.ACTIVATED).orElseThrow(() -> new FastoAlertException(messageService.getMessage("error.code.cart.remove.failed"), messageService.getMessage("error.authenticate.user.not.activated")));
        Map<String, List<CartDto>> myMapCart = getAllCarts(deviceToken);
        if(null == myMapCart){
            return;
        }
        List<CartDto> carts = myMapCart.get(shopId.toString());
        if (null == carts){
            return;
        }
        Map<Long, CartDto> myCart = myMapCart.get(shopId.toString()).stream().collect(Collectors.toMap(CartDto::getId,Function.identity()));
        for(Long item : productIds){
            carts.remove(myCart.get(item));
        }
        List<String> strings = new ArrayList<>();
        for(String key : myMapCart.keySet()){
            if(myMapCart.get(key)==null || myMapCart.get(key).isEmpty()){
                strings.add(key);
            }
        }
        for(List<CartDto> item : myMapCart.values()){
            item.removeIf((e->e.getAmount()<1));
        }
        for(String item : strings){
            myMapCart.remove(item);
        }
        redisService.setCarts(deviceToken, myMapCart);
    }

}
