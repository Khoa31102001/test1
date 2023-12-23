package tech.dut.fasto.common.repository;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tech.dut.fasto.common.domain.Product;
import tech.dut.fasto.common.domain.enumeration.ProductStatus;
import tech.dut.fasto.web.rest.admin.product.dto.response.AdminProductResponseDto;
import tech.dut.fasto.web.rest.shop.product.dto.response.ShopProductResponseDto;
import tech.dut.fasto.web.rest.user.bill.dto.response.BillProductResponseDto;
import tech.dut.fasto.web.rest.user.product.dto.response.UserProductResponseDto;
import tech.dut.fasto.web.rest.user.shop.dto.response.UserShopProductResponseDto;

import java.util.List;
import java.util.Optional;


@SuppressWarnings("unused")
@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    @Query(value = "select new tech.dut.fasto.web.rest.shop.product.dto.response.ShopProductResponseDto(p.id, p.name,p.image,p.description,p.price, p.status, p.deleteFlag, p.countPay) " + "from Product as p inner join Category as c on c.id = p.category.id inner join Shop as s on p.shop.id = s.id where s.id = :shopId and p.deleteFlag = false and (:query IS NULL OR p.name like concat('%',:query,'%'))")
    Page<ShopProductResponseDto> getAllProducts(Pageable pageable, @Param("shopId") Long shopId, @Param("query") String query);

    @Query(value = "select new tech.dut.fasto.web.rest.shop.product.dto.response.ShopProductResponseDto(p.id, p.name,p.image,p.description,p.price, p.status, p.deleteFlag, p.countPay)  " + "from Product as p inner join Category as c on c.id=p.category.id inner join Shop as s on p.shop.id = s.id where s.id = :shopId and p.status = :status and (:query IS NULL OR p.name like concat('%',:query,'%'))")
    Page<ShopProductResponseDto> getAllProductsAndStatus(Pageable pageable, @Param("shopId") Long shopId, @Param("status") ProductStatus status, @Param("query") String query);

    Optional<Product> findByIdAndShopId(Long id, Long shopId);

    Optional<Product> findByIdAndShopIdAndDeleteFlagIsFalse(Long id, Long shopId);

    Optional<Product> findByIdAndDeleteFlagIsFalse(Long id);

    @Query(value = "select new tech.dut.fasto.web.rest.user.product.dto.response.UserProductResponseDto(p.id, p.name,p.image,p.description,p.price, p.status, c.name, c.id, p.countPay, s.id, s.name) " + "from Product as p inner join Category as c on c.id=p.category.id inner join Shop as s on p.shop.id = s.id where s.id = :shopId and p.deleteFlag = false and (:query IS NULL OR p.name like concat('%',:query,'%')) order by p.countPay desc")
    Page<UserProductResponseDto> getTopProductShop(Pageable pageable, @Param("shopId") Long shopId, @Param("query") String query);

    @Query(value = "select new tech.dut.fasto.web.rest.user.product.dto.response.UserProductResponseDto(p.id, p.name,p.image,p.description,p.price, p.status, c.name, c.id, p.countPay, s.id, s.name) " + "from Product as p inner join Category as c on c.id=p.category.id inner join Shop as s on p.shop.id = s.id inner join Location as l on s.address.id = l.id inner join User as u on u.id = s.user.id where  u.status = 'ACTIVATED' and HaversineDistance(:x,:y,l.x,l.y) <=10000 and p.deleteFlag = false and (:query IS NULL OR p.name like concat('%',:query,'%')) order by p.countPay desc")
    Page<UserProductResponseDto> getAllProductShop(Pageable pageable,  @Param("query") String query, @Param("x") Double x, @Param("y") Double y);

    @Query(value = "select new tech.dut.fasto.web.rest.user.product.dto.response.UserProductResponseDto(p.id, p.name,p.image,p.description,p.price, p.status, c.name, c.id, p.countPay, s.id, s.name) " + "from Product as p inner join Category as c on c.id=p.category.id inner join Shop as s on p.shop.id = s.id where  p.deleteFlag = false order by p.countPay desc")
    Page<UserProductResponseDto> getTopProduct(Pageable pageable);

    @Query(value = "select p from Product as  p where p.id in :ids")
    List<Product> getProductInIds(@Param("ids") List<Long> ids);

    @Query(value = "select new tech.dut.fasto.web.rest.user.shop.dto.response.UserShopProductResponseDto(p.id, p.name,p.image,p.description,p.price, p.status, c.name, c.id, p.countPay,s.id, s.name)  " + "from Product as p inner join Category as c on c.id=p.category.id inner join Shop as s on p.shop.id = s.id where s.id = :shopId and p.status = :status")
    List<UserShopProductResponseDto> getListAllProductsAndStatus(@Param("shopId") Long shopId, @Param("status") ProductStatus status);

    @Query(value = "select new tech.dut.fasto.web.rest.user.bill.dto.response.BillProductResponseDto(p.id, p.name,p.image,p.description,p.price, p.status, c.name,c.id, p.countPay, s.id, s.name) " + "from Product as p inner join Category as c on c.id = p.category.id inner join Shop as s on p.shop.id = s.id where p.id in :ids and p.deleteFlag = false")
    List<BillProductResponseDto> getProductResponseInIds(@Param("ids") List<Long> ids);

    @Query(value = "select new tech.dut.fasto.web.rest.admin.product.dto.response.AdminProductResponseDto(p.id, p.name,p.image,p.description,p.price, p.status, p.deleteFlag, p.countPay, s.id, s.name,c.id, c.name) " + "from Product as p inner join Category as c on c.id = p.category.id inner join Shop as s on p.shop.id = s.id where p.deleteFlag = false and (:query IS NULL OR p.name like concat('%',:query,'%'))")
    Page<AdminProductResponseDto> getAllProductsByAdmin(Pageable pageable, @Param("query") String query);
    @Query(value = "select new tech.dut.fasto.web.rest.admin.product.dto.response.AdminProductResponseDto(p.id, p.name,p.image,p.description,p.price, p.status, p.deleteFlag, p.countPay, s.id, s.name,c.id, c.name)  " + "from Product as p inner join Category as c on c.id=p.category.id inner join Shop as s on p.shop.id = s.id where p.status = :status and (:query IS NULL OR p.name like concat('%',:query,'%'))")
    Page<AdminProductResponseDto> getAllProductsAndStatusByAdmin(Pageable pageable, @Param("status") ProductStatus status, @Param("query") String query);

    @Query(value = "select new tech.dut.fasto.web.rest.admin.product.dto.response.AdminProductResponseDto(p.id, p.name,p.image,p.description,p.price, p.status, p.deleteFlag, p.countPay, s.id, s.name,c.id, c.name)  " + "from Product as p inner join Category as c on c.id=p.category.id inner join Shop as s on p.shop.id = s.id where p.id = :productId")
    Optional<AdminProductResponseDto> getDetailsByAdmin(@Param("productId") Long productId);

    @Query(value = "select new tech.dut.fasto.web.rest.admin.product.dto.response.AdminProductResponseDto(p.id, p.name,p.image,p.description,p.price, p.status, p.deleteFlag, p.countPay, s.id, s.name,c.id, c.name) " + "from Product as p inner join Category as c on c.id = p.category.id inner join Shop as s on p.shop.id = s.id where s.id = :idShop and p.deleteFlag = false and (:query IS NULL OR p.name like concat('%',:query,'%'))")
    Page<AdminProductResponseDto> getAllProductsShopByAdmin(Pageable pageable, @Param("query") String query, @Param("idShop") Long idShop);

    @Query(value = "select new tech.dut.fasto.web.rest.admin.product.dto.response.AdminProductResponseDto(p.id, p.name,p.image,p.description,p.price, p.status, p.deleteFlag, p.countPay, s.id, s.name,c.id, c.name)  " + "from Product as p inner join Category as c on c.id=p.category.id inner join Shop as s on p.shop.id = s.id where s.id = :idShop and p.status = :status and (:query IS NULL OR p.name like concat('%',:query,'%'))")
    Page<AdminProductResponseDto> getAllProductsShopAndStatusByAdmin(Pageable pageable, @Param("status") ProductStatus status, @Param("query") String query, @Param("idShop") Long idShop);

}