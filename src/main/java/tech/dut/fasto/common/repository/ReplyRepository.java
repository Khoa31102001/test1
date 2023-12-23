package tech.dut.fasto.common.repository;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tech.dut.fasto.common.domain.Reply;
import tech.dut.fasto.web.rest.user.community.dto.response.ReplyResponseDto;

import java.util.List;
import java.util.Optional;


@Repository
public interface ReplyRepository extends JpaRepository<Reply, Long> {
    @Query(value = "select distinct d.token from Reply as rl inner join Review as r on rl.review.id = r.id inner join User as u on rl.user.id = u.id inner join DeviceTokenInfo as d on u.id = d.user.id where u.id <> :userId and r.id = :reviewId and d.disable = false ")
    List<String> getAllDeviceTokenWithoutUser(@Param("userId") Long userId, @Param("reviewId") Long reviewId);

    Optional<Reply> findByIdAndReviewDeleteFlagIsFalse(Long id);

    @Query(value = "select new tech.dut.fasto.web.rest.user.community.dto.response.ReplyResponseDto(" +
            "rl.id," +
            "r.id," +
            "rl.content , u.id, COUNT(ufrl.reply.id), rl.modifiedAt) " +
            "FROM Reply as rl " +
            "left JOIN UserFavoriteReply as ufrl " +
            "ON rl.id = ufrl.reply.id " +
            "left JOIN ReReply as rrl " +
            "ON rl.id = rrl.reply.id " +
            "INNER JOIN Review as r " +
            "ON rl.review.id = r.id " +
            "INNER JOIN User as u " +
            "ON rl.user.id = u.id " +
            "WHERE (r.id= :reviewId) " +
            "AND r.deleteFlag = FALSE " +
            "GROUP BY rl.id " +
            "ORDER BY COUNT(ufrl.reply.id) DESC ," +
            " COUNT(rl.review.id) DESC")
    Page<ReplyResponseDto> getAllTopReplyByReviewId(@Param("reviewId") Long reviewId, Pageable pageable);

    @Query(value = "select new tech.dut.fasto.web.rest.user.community.dto.response.ReplyResponseDto(" +
            "rl.id," +
            "r.id," +
            "rl.content, u.id , COUNT(ufrl.reply.id), rl.modifiedAt) " +
            "FROM Reply as rl " +
            "left JOIN UserFavoriteReply as ufrl " +
            "ON rl.id = ufrl.reply.id " +
            "INNER JOIN Review as r " +
            "ON rl.review.id = r.id " +
            "INNER JOIN User as u " +
            "ON rl.user.id = u.id " +
            "WHERE (r.id= :reviewId) " +
            "AND r.deleteFlag = FALSE " +
            "GROUP BY rl.id  "
    )
    Page<ReplyResponseDto> getAllReply(@Param("reviewId") Long reviewId, Pageable pageable);

}