package tech.dut.fasto.common.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tech.dut.fasto.common.domain.ShopImage;

import java.util.List;

@SuppressWarnings("unused")
@Repository
public interface ShopImageRepository extends JpaRepository<ShopImage, Long> {
    List<ShopImage> findAllByShopId(Long id);
}