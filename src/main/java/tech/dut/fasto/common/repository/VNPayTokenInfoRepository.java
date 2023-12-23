package tech.dut.fasto.common.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tech.dut.fasto.common.domain.VNPayTokenInfo;
import tech.dut.fasto.web.rest.user.vnpay.dto.response.UserCardResponseDto;

import java.util.List;
import java.util.Optional;

@Repository
public interface VNPayTokenInfoRepository extends JpaRepository<VNPayTokenInfo, Long> {
    Optional<VNPayTokenInfo> findByUserIdAndBankCodeAndCardNumber(Long userId, String bankCode, String cardNumber);

    Optional<VNPayTokenInfo> findByUserIdAndCardNumber(Long userId, String cardNumber);

    @Query(value = "SELECT vti.id FROM VNPayTokenInfo vti where vti.user.id = :userId")
    List<Long> userGetListVNPayTokenInfoIdByUserId(Long userId);

    List<VNPayTokenInfo> findAllByUserIdAndIdNot(Long userId, Long tokenId);

    Optional<VNPayTokenInfo> findByIdAndUserId(Long id, Long userId);

    boolean existsByTokenAndUserId(String token, Long userId);

    @Query(value = "SELECT new tech.dut.fasto.web.rest.user.vnpay.dto.response.UserCardResponseDto(vt.id, vt.cardNumber, vt.bankCode) FROM VNPayTokenInfo as vt where vt.user.id = :userId and vt.deleteFlag = false")
    List<UserCardResponseDto> getAllCardNumbers(@Param("userId") Long userId);
}