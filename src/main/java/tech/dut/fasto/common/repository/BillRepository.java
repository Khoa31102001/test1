package tech.dut.fasto.common.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tech.dut.fasto.common.domain.Bill;
import tech.dut.fasto.common.domain.VoucherUser;
import tech.dut.fasto.common.domain.enumeration.BillStatus;
import tech.dut.fasto.common.domain.enumeration.VoucherProvider;
import tech.dut.fasto.web.rest.admin.turnover.dto.response.AdminTurnoverDiscountShopDto;
import tech.dut.fasto.web.rest.admin.turnover.dto.response.AdminTurnoverTotalShopResponseDto;
import tech.dut.fasto.web.rest.shop.bill.dto.response.BillUserResponseDto;
import tech.dut.fasto.web.rest.shop.turnover.dto.response.TurnoverTotalResponseDto;
import tech.dut.fasto.web.rest.user.bill.dto.response.UserBillResponseDto;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Optional;


@SuppressWarnings("unused")
@Repository
public interface BillRepository extends JpaRepository<Bill, Long> {



//    Optional<Bill> findByShopIdAndCode(@Param("shopId") Long id, @Param("code") String code);

    Optional<Bill> findByIdAndShopId( Long id, Long shopId);

    boolean existsByTotalPaymentAndId(BigDecimal amount, Long id);

    boolean existsByIdAndStatus(Long billId, BillStatus billStatus);

    Optional<Bill> findByIdAndStatusAndUserId(Long id, BillStatus status, Long userId);

    Optional<Bill> findByIdAndStatus(Long userId , BillStatus status);
    Optional<Bill> findByUserIdAndShopId(Long userId , Long shopId);
    Optional<Bill> findByUserIdAndShopIdAndStatus(Long userId , Long shopId, BillStatus billStatus);
    @Query(value = "select new tech.dut.fasto.web.rest.user.bill.dto.response.UserBillResponseDto(b.id,b.totalOrigin,b.totalPayment,b.totalVoucher, b.createdAt, b.status, s.id, s.name, s.banner, a.id, a.city, a.city, a.stateProvince, a.streetAddress, a.x, a.y, b.isRating, b.ratings) from Bill as b inner join User as u on b.user.id = u.id inner join Shop as s on b.shop.id = s.id inner join Location as a on s.address.id = a.id where u.id = :userId and (:status is null or b.status = :status) and (:query is null or s.name like concat('%',:query,'%')) order by b.createdAt desc")
    List<UserBillResponseDto> getAllByUserIdAndStatus(@Param("userId") Long id, @Param("status") BillStatus status, @Param("query") String query);

    @Query(value = "select new tech.dut.fasto.web.rest.shop.bill.dto.response.BillUserResponseDto(b.id,b.totalOrigin,b.totalPayment,b.totalVoucher, u.id, uf.firstName, uf.lastName, b.createdAt, b.status, b.isRating, b.ratings, uf.userImage, b.shippingFee) from Bill as b inner join Shop as s on b.shop.id = s.id inner join User as u on u.id = b.user.id inner join UserInformation as uf on u.userInformation.id = uf.id where s.id = :shopId and (:status  is null or b.status = :status) and (:query is null or uf.firstName like concat('%',:query,'%') or uf.lastName like concat('%',:query,'%') ) order by b.createdAt desc ")
    Page<BillUserResponseDto> getAllByShopId(@Param("shopId") Long id, @Param("status") BillStatus status, @Param("query") String query, Pageable pageable);

    @Query(value = "SELECT new tech.dut.fasto.web.rest.admin.turnover.dto.response.AdminTurnoverTotalShopResponseDto(sum(b.totalOrigin) , sum(b.totalVoucher) , sum(b.totalPayment), s.name, s.id, s.banner, s.description, s.ratings, s.startRatings) FROM Bill as b inner join Shop as s on s.id = b.shop.id " +
            "where (:dateFrom IS NULL OR b.createdAt >= :dateFrom) and (:dateTo IS NULL OR b.createdAt <= :dateTo)  and b.status= :status group by s.id")
    Page<AdminTurnoverTotalShopResponseDto> getAllTotalTurnoverShop(@Param("dateFrom") Instant dateFrom, @Param("dateTo") Instant dateTo, Pageable pageable,  @Param("status") BillStatus status);

    @Query(value = "SELECT new tech.dut.fasto.web.rest.admin.turnover.dto.response.AdminTurnoverTotalShopResponseDto(sum(b.totalOrigin) , sum(b.totalVoucher) , sum(b.totalPayment), s.name, s.id, s.banner, s.description, s.ratings, s.startRatings) FROM Bill as b inner join Shop as s on s.id = b.shop.id " +
            "where (:dateFrom IS NULL OR b.createdAt >= :dateFrom) and (:dateTo IS NULL OR b.createdAt <= :dateTo)  and b.status= :status group by s.id")
    List<AdminTurnoverTotalShopResponseDto> getAllTotalTurnoverShop(@Param("dateFrom") Instant dateFrom, @Param("dateTo") Instant dateTo,   @Param("status") BillStatus status);

    @Query(value = "SELECT new tech.dut.fasto.web.rest.admin.turnover.dto.response.AdminTurnoverTotalShopResponseDto(sum(b.totalOrigin) , sum(b.totalVoucher) , sum(b.totalPayment), s.name, s.id, s.banner, s.description, s.ratings, s.startRatings) FROM Bill as b inner join Shop as s on s.id = b.shop.id " +
            "where s.id = :shopId and (:dateFrom IS NULL OR b.createdAt >= :dateFrom) and (:dateTo IS NULL OR b.createdAt <= :dateTo) and b.status= :billStatus group by s.id")
    AdminTurnoverTotalShopResponseDto getTurnoverShop(@Param("dateFrom") Instant dateFrom, @Param("dateTo") Instant dateTo, @Param("shopId") Long shopId ,@Param("billStatus") BillStatus billStatus);

    @Query(value = "SELECT new tech.dut.fasto.web.rest.shop.turnover.dto.response.TurnoverTotalResponseDto(sum(b.totalOrigin) , sum(b.totalVoucher) , sum(b.totalPayment)) FROM Bill as b inner join Shop as s on b.shop.id = s.id " +
            "where s.id = :shopId and (:dateFrom IS NULL OR b.createdAt >= :dateFrom) and (:dateTo IS NULL OR b.createdAt <= :dateTo) and b.status= :billStatus group by s.id")
    TurnoverTotalResponseDto getTotalTurnover(@Param("dateFrom") Instant dateFrom, @Param("dateTo") Instant dateTo, @Param("shopId") Long shopId, @Param("billStatus") BillStatus status);

    @Query(value = "Select sum(b.totalVoucher) from Bill as b inner join Voucher as v on b.voucher.id = v.id inner join Shop as s on b.shop.id = s.id where s.id = :shopId and (:dateFrom IS NULL OR b.createdAt >= :dateFrom) and (:dateTo IS NULL OR b.createdAt <= :dateTo) and b.status = :billStatus and b.status= :billStatus and v.userType = :voucherProvider group by s.id ")
    BigDecimal getTotalVoucher(@Param("voucherProvider") VoucherProvider voucherProvider , @Param("dateFrom") Instant dateFrom, @Param("dateTo") Instant dateTo, @Param("shopId") Long shopId, @Param("billStatus") BillStatus billStatus);

    @Query(value = "Select new tech.dut.fasto.web.rest.admin.turnover.dto.response.AdminTurnoverDiscountShopDto(s.id, sum(b.totalVoucher)) from Bill as b inner join Voucher as v on b.voucher.id = v.id inner join Shop as s on b.shop.id = s.id where (:dateFrom IS NULL OR b.createdAt >= :dateFrom) and (:dateTo IS NULL OR b.createdAt <= :dateTo) and b.status = :billStatus and v.userType = :voucherProvider group by s.id ")
    List<AdminTurnoverDiscountShopDto> getAllTotalVoucherAdmin(@Param("voucherProvider") VoucherProvider voucherProvider , @Param("dateFrom") Instant dateFrom, @Param("dateTo") Instant dateTo, @Param("billStatus") BillStatus billStatus);
}