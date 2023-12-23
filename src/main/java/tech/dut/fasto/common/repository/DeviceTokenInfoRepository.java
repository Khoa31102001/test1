package tech.dut.fasto.common.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tech.dut.fasto.common.domain.DeviceTokenInfo;

import java.util.List;
import java.util.Optional;

@Repository
public interface DeviceTokenInfoRepository extends JpaRepository<DeviceTokenInfo, Long> {

    Optional<DeviceTokenInfo> findByToken(String token);

    Optional<DeviceTokenInfo> findByTokenAndUserId(String token, Long userId);

    Optional<DeviceTokenInfo> findByDisableAndUserId(boolean disable, Long userId);

    @Query(value = "select d.token from DeviceTokenInfo as d where d.user.id = :userId and d.disable=false")
    List<String> getAllByUserId(@Param("userId") Long userId);


}