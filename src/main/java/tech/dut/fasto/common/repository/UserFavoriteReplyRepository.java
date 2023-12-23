package tech.dut.fasto.common.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tech.dut.fasto.common.domain.UserFavoriteReply;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserFavoriteReplyRepository extends JpaRepository<UserFavoriteReply, Long> {
    Optional<UserFavoriteReply> findByUserIdAndReplyId(Long userId, Long replyId);

    List<UserFavoriteReply> findAllByUserIdAndReplyId(Long userId, Long replyId);

    List<UserFavoriteReply> findAllByReplyIdIn(List<Long> replyIds);

    List<UserFavoriteReply> findAllByReplyId(Long replyId);

    List<Boolean> deleteAllByReplyId(Long replyId);

}