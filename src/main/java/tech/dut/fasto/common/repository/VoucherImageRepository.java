package tech.dut.fasto.common.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tech.dut.fasto.common.domain.VoucherImage;

import java.util.List;

@SuppressWarnings("unused")
@Repository
public interface VoucherImageRepository extends JpaRepository<VoucherImage, Long> {
    List<VoucherImage> findAllByVoucherId(Long id);
}