package tech.dut.fasto.common.repository;



import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tech.dut.fasto.common.domain.ShopFavourite;
import tech.dut.fasto.web.rest.user.favorite.dto.response.ShopFavoriteResponseDto;


import java.util.Optional;

@SuppressWarnings("unused")
@Repository
public interface ShopFavouriteRepository extends JpaRepository<ShopFavourite, Long> {
    Optional<ShopFavourite> findByShopIdAndUserId(Long shopId, Long userId);

    @Query(value = "SELECT new tech.dut.fasto.web.rest.user.favorite.dto.response.ShopFavoriteResponseDto(s.id, s.name, s.banner, s.description, s.scheduleActive, s.phone) FROM ShopFavourite as f inner join User as u on f.user.id = u.id inner join Shop  as s on f.shop.id = s.id where u.id = :userId order by s.id ")
    Page<ShopFavoriteResponseDto> getAllShopFavorite(@Param("userId") Long userId, Pageable pageable);

}