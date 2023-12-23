package tech.dut.fasto.common.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tech.dut.fasto.common.domain.UserFavoriteReview;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserFavoriteReviewRepository extends JpaRepository<UserFavoriteReview, Long> {
    Optional<UserFavoriteReview> findByUserIdAndReviewId(Long userId, Long reviewId);

    List<UserFavoriteReview> findAllByUserIdAndReviewId(Long userId, Long reviewId);

    List<UserFavoriteReview> findAllByReviewId(Long reviewId);

    List<Boolean> deleteAllByReviewId(Long reviewId);

    @Query(value = "select distinct d.token from UserFavoriteReview as f inner join Review as r on f.review.id = r.id inner join User as u on f.user.id = u.id inner join DeviceTokenInfo as d on u.id = d.user.id where u.id <> :userId and r.id = :reviewId and d.disable = false ")
    List<String> getAllDeviceTokenWithoutUser(@Param("userId") Long userId, @Param("reviewId") Long reviewId);

    Long countAllByReviewId(Long reviewId);

    @Query(value = "update UserFavoriteReview as uf  set uf.deleteFlag= :deleteFlag where  uf.review.id= :reviewId")
    List<Boolean> updateStatusByReviewId(@Param("reviewId") Long reviewId, @Param("deleteFlag") Boolean deleteFlag );

    Boolean existsByReviewIdAndUserId (Long reviewId, Long userId );
}