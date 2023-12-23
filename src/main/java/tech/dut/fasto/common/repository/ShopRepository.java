package tech.dut.fasto.common.repository;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tech.dut.fasto.common.domain.Shop;
import tech.dut.fasto.common.domain.enumeration.UserStatus;
import tech.dut.fasto.web.rest.admin.shop.dto.AdminShopResponseDto;
import tech.dut.fasto.web.rest.user.shop.dto.response.UserShopLocationResponseDto;
import tech.dut.fasto.web.rest.user.shop.dto.response.UserShopProfileResponseDto;

import java.util.List;
import java.util.Optional;


@SuppressWarnings("unused")
@Repository
public interface ShopRepository extends JpaRepository<Shop, Long> {
    @Query(value = "select new tech.dut.fasto.web.rest.admin.shop.dto.AdminShopResponseDto(s.id, s.name,u.status,s.description,s.banner,s.phone, s.ratings, s.startRatings, s.isDeposit) " +
            "from User as u inner join Shop as s on u.id=s.user.id and (:query IS NULL OR s.name like concat('%',:query,'%')) ")
    Page<AdminShopResponseDto> getAllShop(Pageable pageable, @Param("query") String query);

    @Query(value = "select new tech.dut.fasto.web.rest.admin.shop.dto.AdminShopResponseDto(s.id, s.name,u.status,s.description,s.banner,s.phone, s.ratings, s.startRatings, s.isDeposit) " +
            "from User as u inner join Shop as s on u.id=s.user.id where u.status = :status and (:query IS NULL OR s.name like concat('%',:query,'%'))")
    Page<AdminShopResponseDto> getAllShop(Pageable pageable, @Param("status") UserStatus status, @Param("query") String query);

    Optional<Shop> findByUserEmailAndUserStatus(String email, UserStatus status);

    Optional<Shop> findByIdAndUserStatus(Long id, UserStatus status);

    @Query(value = "SELECT new tech.dut.fasto.web.rest.user.shop.dto.response.UserShopLocationResponseDto(" +
            "s.id," +
            "s.name," +
            "s.description," +
            "s.banner," +
            "l.x, l.y," +
            "l.streetAddress, s.ratings, ROUND(s.startRatings, 1)) FROM Shop as s inner join Location as l on s.address.id = l.id inner join User as u on u.id = s.user.id" +
            " WHERE u.status = 'ACTIVATED' and HaversineDistance(:x,:y,l.x,l.y) <=10000 and  (:radius IS NULL OR  distance_shop(l.id, :x, :y) <= :radius) and s.isDeposit = true")
    Page<UserShopLocationResponseDto> findAllShopLocationByName(@Param("radius") Double radius, @Param("x") Double x, @Param("y") Double y, Pageable pageable);

    @Query(value = "SELECT new tech.dut.fasto.web.rest.user.shop.dto.response.UserShopLocationResponseDto(" +
            "s.id," +
            "s.name," +
            "s.description," +
            "s.banner," +
            "l.x, l.y," +
            "l.streetAddress, s.ratings, ROUND(s.startRatings, 1)) FROM Shop as s inner join Location as l on s.address.id = l.id inner join User as u on s.user.id = u.id " +
            "WHERE u.status = 'ACTIVATED' and s.id <> :shopId and distance_shop(l.id,l.x, l.y) <= :radius and s.isDeposit = true ")
    Page<UserShopLocationResponseDto> findAllShopLocationByName(@Param("radius") Double radius,@Param("shopId") Long shopId,  Pageable pageable);

    @Query(value = "select new tech.dut.fasto.web.rest.user.shop.dto.response.UserShopProfileResponseDto(shop.id,shop.name,shop.banner, shop.description,shop.phone,shop.startRatings, shop.ratings, l.city,l.stateProvince,l.country,l.streetAddress,l.stateProvince, l.x, l.y) from Shop as shop inner join Location as l on shop.address.id = l.id inner join User  as u on shop.user.id = u.id where  u.status = 'ACTIVATED' and (:query IS NULL OR shop.name like concat('%',:query,'%')) order by shop.id desc")
    Page<UserShopProfileResponseDto> getAlLUserShop(@Param("query") String query, Pageable pageable);

    Optional<Shop> findByNameIgnoreCase(String name);

    @Query(value = "select new tech.dut.fasto.web.rest.user.shop.dto.response.UserShopProfileResponseDto(shop.id,shop.name,shop.banner, shop.description,shop.phone,shop.startRatings, shop.ratings, l.city,l.stateProvince,l.country,l.streetAddress,l.stateProvince, l.x, l.y) from Shop as shop inner join Location as l on shop.address.id = l.id inner join User  as u on shop.user.id = u.id where  u.status = 'ACTIVATED' order by shop.startRatings desc")
    Page<UserShopProfileResponseDto> getTopShop(Pageable pageable);

    List<Shop> findAllByUserStatus(@Param("status") UserStatus status);
}