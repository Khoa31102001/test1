package tech.dut.fasto.common.repository;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tech.dut.fasto.common.domain.Category;
import tech.dut.fasto.common.dto.CategoryDto;

import java.util.Optional;

@SuppressWarnings("unused")
@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    Optional<Category> findByIdAndDeleteFlagIsFalse(Long id);

    @Query(value = "select new tech.dut.fasto.common.dto.CategoryDto(c.id, c.name, c.categoryImage, c.deleteFlag) from Category as c where (:deleteFlag IS NULL OR c.deleteFlag = :deleteFlag)")
    Page<CategoryDto> getAllCategories(Pageable pageable, @Param("deleteFlag") Boolean deleteFlag);

    @Query(value = "select new tech.dut.fasto.common.dto.CategoryDto(c.id, c.name ,c.categoryImage, c.deleteFlag) " +
            "from Category as c where c.deleteFlag = false and (:query IS NULL OR c.name like concat('%',:query,'%'))")
    Page<CategoryDto> getAllCategoriesByAdmin(Pageable pageable, @Param("query") String query);

    Boolean existsByName(String name);

}