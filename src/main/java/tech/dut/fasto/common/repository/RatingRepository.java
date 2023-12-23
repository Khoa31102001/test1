package tech.dut.fasto.common.repository;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tech.dut.fasto.common.domain.Rating;
import tech.dut.fasto.web.rest.shop.rating.dto.response.ShopRatingResponseDto;


@SuppressWarnings("unused")
@Repository
public interface RatingRepository extends JpaRepository<Rating, Long> {
    @Query(value = "select new tech.dut.fasto.web.rest.shop.rating.dto.response.ShopRatingResponseDto(u.id,uf.firstName, uf.lastName, uf.userImage, r.content, r.ratings) from Rating as r inner join User as u on r.user.id = u.id inner join Shop as s on s.id = r.shop.id inner join UserInformation as uf on u.id = uf.user.id where s.id = :shopId order by r.id desc ")
    Page<ShopRatingResponseDto> getAllRatingShop(Pageable pageable, @Param("shopId") Long shopId);

}