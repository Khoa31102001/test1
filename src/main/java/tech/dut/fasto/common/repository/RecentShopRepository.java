package tech.dut.fasto.common.repository;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tech.dut.fasto.common.domain.RecentShop;
import tech.dut.fasto.web.rest.user.shop.dto.response.UserShopProfileResponseDto;


import java.util.Optional;

@SuppressWarnings("unused")
@Repository
public interface RecentShopRepository extends JpaRepository<RecentShop, Long> {


    Optional<RecentShop> findByUserIdAndShopId(Long userId, Long shopId);

    @Query(value = "select new tech.dut.fasto.web.rest.user.shop.dto.response.UserShopProfileResponseDto(s.id,s.name,s.banner, s.description,s.phone,s.startRatings, s.ratings, l.city,l.stateProvince,l.country,l.streetAddress,l.stateProvince, l.x, l.y) from RecentShop as rs inner join User as u on u.id = rs.user.id  inner join Shop as s on  s.id = rs.shop.id  inner join Location as l on s.address.id = l.id where u.id = :userId order by rs.createdAt desc")
    Page<UserShopProfileResponseDto> getAllRecentShop(@Param("userId") Long userId, Pageable pageable);


}