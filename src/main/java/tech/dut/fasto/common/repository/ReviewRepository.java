package tech.dut.fasto.common.repository;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tech.dut.fasto.common.domain.Review;
import tech.dut.fasto.web.rest.user.community.dto.response.ReviewResponseDto;

import java.util.Optional;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    Optional<Review> findByIdAndDeleteFlagIsFalse(Long id);

    @Query(value = "select new tech.dut.fasto.web.rest.user.community.dto.response.ReviewResponseDto(" +
            "rv.id," +
            "rv.title," +
            "rv.content," +
            "s.id, u.id,COUNT(ufr.review.id), rv.modifiedAt) " +
            "FROM Review as rv " +
            "left JOIN UserFavoriteReview as ufr " +
            "ON rv.id = ufr.review.id " +
            "left JOIN Reply as rl " +
            "ON rv.id = rl.review.id " +
            "INNER JOIN Shop as s " +
            "ON rv.shop.id = s.id " +
            "INNER JOIN User as u " +
            "ON rv.user.id= u.id " +
            "WHERE (:shopId IS NULL OR rv.shop.id = :shopId) " +
            "AND rv.deleteFlag = FALSE AND ufr.deleteFlag = FALSE " +
            "GROUP BY rv.id " +
            "ORDER BY COUNT(ufr.review.id) DESC, " +
            "COUNT(rl.review.id) DESC")
    Page<ReviewResponseDto> getAllTopReviewByShopId(@Param("shopId") Long shopId, Pageable pageable);

    @Query(value = "select new tech.dut.fasto.web.rest.user.community.dto.response.ReviewResponseDto(" +
            "rv.id," +
            "rv.title," +
            "rv.content," +
            "s.id,u.id,COUNT(ufr.review.id), rv.modifiedAt ) " +
            "FROM Review as rv " +
            "left JOIN UserFavoriteReview as ufr " +
            "ON rv.id = ufr.review.id " +
            "INNER JOIN Shop as s " +
            "ON rv.shop.id = s.id " +
            "INNER JOIN User as u " +
            "ON rv.user.id= u.id " +
            "WHERE (:shopId IS NULL OR rv.shop.id = :shopId) " +
            "AND rv.deleteFlag = FALSE " +
            "group by  rv.id"
    )
    Page<ReviewResponseDto> getAllReview(@Param("shopId") Long shopId, Pageable pageable);

}