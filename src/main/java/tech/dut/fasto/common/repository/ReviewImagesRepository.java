package tech.dut.fasto.common.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tech.dut.fasto.common.domain.ReviewImages;

import java.util.List;

@Repository
public interface ReviewImagesRepository extends JpaRepository<ReviewImages, Long> {
    boolean deleteByReviewId(Long reviewId);

    @Query(value = "select rvi.imagesUrl from ReviewImages as rvi inner join Review as r on rvi.review.id = r.id where r.deleteFlag = FALSE and r.id= :reviewId")
    List<String> getImagesByReview(@Param("reviewId") Long reviewId);
}