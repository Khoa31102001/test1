package tech.dut.fasto.common.repository;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tech.dut.fasto.common.domain.BillItem;
import tech.dut.fasto.common.domain.enumeration.BillStatus;
import tech.dut.fasto.web.rest.shop.billitem.dto.response.BillItemResponseDto;
import tech.dut.fasto.web.rest.shop.turnover.dto.response.TopProductResponseDto;
import tech.dut.fasto.web.rest.shop.turnover.dto.response.TurnoverProductResponseDto;

import java.time.Instant;
import java.util.List;


@SuppressWarnings("unused")
@Repository
public interface BillItemRepository extends JpaRepository<BillItem, Long> {
    List<BillItem> findAllByBillId(Long billId);

    @Query(value = "select new tech.dut.fasto.web.rest.shop.billitem.dto.response.BillItemResponseDto(p.name, bi.amount, p.price, s.name, c.name, p.image) from BillItem as bi inner join Bill as b on b.id = bi.bill.id inner join" +
            " Product as p on bi.product.id = p.id inner join Category as c on c.id = p.category.id inner join Shop as s " +
            " on s.id = bi.shop.id where b.id = :billId")
    List<BillItemResponseDto> getAllBillItemByBillId(@Param("billId") Long billId);

    @Query(value = "SELECT new tech.dut.fasto.web.rest.shop.turnover.dto.response.TurnoverProductResponseDto(p.id, p.name, sum(bitem.amount) , (sum(bitem.amount) * p.price))" +
            "FROM BillItem as bitem inner join Product as p on bitem.product.id  = p.id inner join Shop as s on s.id = bitem.shop.id " +
            "where s.id = :shopId and ( :dateFrom IS NULL OR bitem.createdAt >= :dateFrom) and ( :dateTo  IS NULL OR bitem.createdAt <= :dateTo) " +
            "group by p.id")
    List<TurnoverProductResponseDto> getAllProductPurchase(@Param("dateFrom") Instant dateFrom, @Param("dateTo") Instant dateTo, @Param("shopId") Long shopId);

    @Query(value = "SELECT new tech.dut.fasto.web.rest.shop.turnover.dto.response.TopProductResponseDto(p.id, p.name, sum(bitem.amount) , p.image , p.price) FROM " +
            "BillItem as bitem inner join Shop as s on bitem.shop.id = s.id inner join Bill as b on bitem.bill.id = b.id inner join Product as p on bitem.product.id = p.id " +
            "where s.id = :shopId and (:dateFrom IS NULL OR bitem.createdAt >= :dateFrom) and (:dateTo IS NULL OR bitem.createdAt <= :dateTo) and b.status = :status group by p.id")
    Page<TopProductResponseDto> getTopProduct(@Param("dateFrom") Instant dateFrom, @Param("dateTo") Instant dateTo, @Param("shopId") Long shopId, Pageable pageable, @Param("status") BillStatus status);

}