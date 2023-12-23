package tech.dut.fasto.common.repository;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tech.dut.fasto.common.domain.ReReply;
import tech.dut.fasto.web.rest.user.community.dto.response.ReReplyResponseDto;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReReplyRepository extends JpaRepository<ReReply, Long> {
    Optional<ReReply> findByIdAndReviewDeleteFlagIsFalse(Long id);

    List<Boolean> deleteAllByReplyId(Long replyId);

    @Query(value = "select distinct r from ReReply as rl inner join Reply as r on rl.reply.id = r.id inner join User as u on rl.user.id = u.id inner join DeviceTokenInfo as d on u.id = d.user.id where u.id <> :userId and r.id = :replyId ")
    List<String> getAllDeviceTokenWithoutUser(@Param("userId") Long userId, @Param("replyId") Long replyId);

    @Query(value = "select new tech.dut.fasto.web.rest.user.community.dto.response.ReReplyResponseDto(" +
            "rrl.id," +
            "r.id," +
            "rrl.content, rl.id, u.id, COUNT(ufrrl.reReply.id), rrl.modifiedAt ) " +
            "FROM ReReply as rrl " +
            "left JOIN UserFavoriteReReply as ufrrl " +
            "ON rrl.id = ufrrl.reReply.id " +
            "INNER JOIN Reply as rl " +
            "ON rrl.reply.id = rl.id " +
            "INNER JOIN Review as r " +
            "ON rl.review.id = r.id " +
            "INNER JOIN User as u " +
            "ON rrl.user.id = u.id " +
            "WHERE (rl.id.id= :replyId) " +
            "AND r.deleteFlag = FALSE " +
            "GROUP BY rrl.id " +
            "ORDER BY COUNT(ufrrl.reReply.id) DESC ")
    Page<ReReplyResponseDto> getAllTopReReplyByReplyId(@Param("replyId") Long replyId, Pageable pageable);

    @Query(value = "select new tech.dut.fasto.web.rest.user.community.dto.response.ReReplyResponseDto(" +
            "rrl.id," +
            "r.id," +
            "rrl.content, rl.id , u.id, COUNT(ufrrl.reReply.id), rrl.modifiedAt) " +
            "FROM ReReply as rrl " +
            "left JOIN UserFavoriteReReply as ufrrl " +
            "ON rrl.id = ufrrl.reReply.id " +
            "INNER JOIN Reply as rl " +
            "ON rrl.reply.id = rl.id " +
            "INNER JOIN Review as r " +
            "ON rl.review.id = r.id " +
            "INNER JOIN User as u " +
            "ON rrl.user.id = u.id " +
            "WHERE (rl.id= :replyId) " +
            "AND r.deleteFlag = FALSE "+
            "GROUP BY rrl.id  "
    )
    Page<ReReplyResponseDto> getAllReReply(@Param("replyId") Long replyId, Pageable pageable);

}