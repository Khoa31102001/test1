package tech.dut.fasto.common.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tech.dut.fasto.common.domain.UserFavoriteReReply;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserFavoriteReReplyRepository extends JpaRepository<UserFavoriteReReply, Long> {
    Optional<UserFavoriteReReply> findByUserIdAndReReplyId(Long userId, Long reReplyId);

    List<Boolean> deleteAllByReReplyId(Long reReplyId);
}