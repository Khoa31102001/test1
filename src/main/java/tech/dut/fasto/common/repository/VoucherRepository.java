package tech.dut.fasto.common.repository;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tech.dut.fasto.common.domain.Voucher;
import tech.dut.fasto.common.domain.enumeration.VoucherProvider;
import tech.dut.fasto.common.domain.enumeration.VoucherStatus;
import tech.dut.fasto.web.rest.admin.voucher.dto.response.AdminVoucherResponseDto;
import tech.dut.fasto.web.rest.shop.voucher.dto.response.ShopVoucherResponseDto;
import tech.dut.fasto.web.rest.user.voucher.dto.response.VoucherUserResponseDto;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@SuppressWarnings("unused")
@Repository
public interface VoucherRepository extends JpaRepository<Voucher, Long> {
    @Query(value = "select new tech.dut.fasto.web.rest.shop.voucher.dto.response.ShopVoucherResponseDto(v.id, v.name, v.startedAt, v.endedAt, v.userType, v.valueDiscount, v.voucherType, v.valueNeed, v.limitPerUser, v.maxDiscount, v.quantity, v.status, v.image, v.descriptions, s.id, s.name) " +
            "from Voucher as v inner join Shop as s on v.shop.id = s.id where (:query IS NULL OR v.name like concat('%',:query,'%')) and (:status IS NULL OR v.status = :status) and s.id = :shopId")
    Page<ShopVoucherResponseDto> getAllVouchers(Pageable pageable, @Param("shopId") Long shopId, @Param("query") String query, @Param("status") VoucherStatus status);

    @Query(value = "select new tech.dut.fasto.web.rest.shop.voucher.dto.response.ShopVoucherResponseDto(v.id, v.name, v.startedAt, v.endedAt, v.userType, v.valueDiscount, v.voucherType, v.valueNeed, v.limitPerUser, v.maxDiscount, v.quantity, v.status, v.image, v.descriptions, s.id, s.name) " +
            "from Voucher as v inner join Shop as s on v.shop.id = s.id where (:status IS NULL OR v.status = :status) and s.id = :shopId")
    List<ShopVoucherResponseDto> getListAllVouchers(@Param("shopId") Long shopId, @Param("status") VoucherStatus status);


    Optional<Voucher> findByIdAndShopId(Long id, Long shopId);

    Optional<Voucher> findByIdAndShopIdAndStatusNotLike(Long id, Long shopId, VoucherStatus status);

    Optional<Voucher> findByIdAndUserType(Long voucherId, VoucherProvider userType);

    Optional<Voucher> findByIdAndUserTypeAndStatusNotLike(Long voucherId, String userType, VoucherStatus status);


    @Query(value = "select new tech.dut.fasto.web.rest.shop.voucher.dto.response.ShopVoucherResponseDto(v.id, v.name, v.createdAt, v.endedAt, v.userType, v.valueDiscount, v.voucherType, v.valueNeed, v.limitPerUser, v.maxDiscount, v.quantity,v.status, v.image, v.descriptions, s.id, s.name) " +
            "from Voucher as v inner join Shop as s on s.id = v.shop.id where v.id =:id")
    ShopVoucherResponseDto getDetailVoucherByAdmin(@Param("id") Long id);

    Optional<Voucher> findByIdAndShopIdAndStatusNot(Long id, Long shopId, VoucherStatus status);

    @Query(value = "select new tech.dut.fasto.web.rest.admin.voucher.dto.response.AdminVoucherResponseDto(v.id, v.name, v.createdAt, v.endedAt, v.userType, v.valueDiscount, v.voucherType, v.valueNeed, v.limitPerUser, v.maxDiscount, v.quantity,v.status, v.image, v.descriptions) " +
            "from Voucher as v where (:query is null or v.name like concat('%',:query,'%')) and v.userType = :userType and (:status is null or v.status in :status)")
    Page<AdminVoucherResponseDto> getAllVoucherByAdmin(Pageable pageable, @Param("userType") VoucherProvider userType, @Param("query") String query, @Param("status") VoucherStatus status);

    @Query(value = "select new tech.dut.fasto.web.rest.admin.voucher.dto.response.AdminVoucherResponseDto(v.id, v.name, v.createdAt, v.endedAt, v.userType, v.valueDiscount, v.voucherType, v.valueNeed, v.limitPerUser, v.maxDiscount, v.quantity,v.status, v.image, v.descriptions) " +
            "from Voucher as v where v.userType = :userType and v.id =:id")
    AdminVoucherResponseDto getDetailVoucherByAdmin(@Param("userType") VoucherProvider userType, @Param("id") Long id);

    @Query(value = "select new tech.dut.fasto.web.rest.user.voucher.dto.response.VoucherUserResponseDto(v.id, v.name, v.createdAt, v.endedAt, v.userType, v.image, v.valueDiscount, v.voucherType, v.valueNeed, v.limitPerUser, v.maxDiscount, v.quantity,s.id, s.name, v.descriptions, v.limitPerUser)  from Voucher as v left join Shop as s on v.shop.id = s.id where (:userType is null or v.userType = :userType) and (:query is null or v.name like concat('%',:query,'%')) and v.status <> :status order by v.id desc")
    Page<VoucherUserResponseDto> getAllVoucherForUser(Pageable pageable, @Param("userType") VoucherProvider userType, @Param("status") VoucherStatus status, @Param("query") String query);

    @Query(value = "select new tech.dut.fasto.web.rest.user.voucher.dto.response.VoucherUserResponseDto(v.id, v.name, v.createdAt, v.endedAt, v.userType, v.image, v.valueDiscount, v.voucherType, v.valueNeed, v.limitPerUser, v.maxDiscount, v.quantity,s.id, s.name, v.descriptions, v.limitPerUser)  from Voucher as v left join Shop as s on v.shop.id = s.id where (s.id = :shopId OR v.userType = :userType) and  v.valueNeed <= :totalOrigin and v.status not in :status order by v.id desc")
    List<VoucherUserResponseDto> getAllVoucherUser(@Param("shopId") Long shopId, @Param("totalOrigin") BigDecimal totalOrigin, @Param("userType") VoucherProvider userType,  @Param("status") List<VoucherStatus> status);

}