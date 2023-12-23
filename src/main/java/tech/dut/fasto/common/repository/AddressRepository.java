package tech.dut.fasto.common.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tech.dut.fasto.common.domain.Location;
import tech.dut.fasto.common.domain.Shop;

import java.util.Optional;

@SuppressWarnings("unused")
@Repository
public interface AddressRepository extends JpaRepository<Location, Long> {
    Optional<Location> findByXAndY(Double x, Double y);

    boolean existsByXAndYAndShopIsNot(Double x, Double y, Shop shop);
}