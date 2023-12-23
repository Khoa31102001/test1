package tech.dut.fasto.common.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tech.dut.fasto.common.domain.VoucherUser;

import java.util.Optional;

@SuppressWarnings("unused")
@Repository
public interface VoucherUserRepository extends JpaRepository<VoucherUser, Long> {

    boolean existsByUserIdAndVoucherId(Long userId, Long voucherId);
    Optional<VoucherUser> findByUserIdAndVoucherId(Long userId, Long voucherId);
}