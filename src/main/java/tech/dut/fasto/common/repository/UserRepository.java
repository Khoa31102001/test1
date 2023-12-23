package tech.dut.fasto.common.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tech.dut.fasto.common.domain.User;
import tech.dut.fasto.common.domain.enumeration.Provider;
import tech.dut.fasto.common.domain.enumeration.UserStatus;
import tech.dut.fasto.web.rest.admin.user.dto.response.AdminUserResponseDto;


import java.util.Optional;

@SuppressWarnings("unused")
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmailAndStatus(String email, UserStatus status);

    Optional<User> findByIdAndStatus(Long id, UserStatus status);

    Optional<User> findByEmail(String email);

    boolean existsByPhoneNumberIgnoreCase(String phoneNumber);

    boolean existsByEmailIgnoreCaseAndProvider(String email, Provider provider);

    Optional<User> findByAuthCode(String resetToken);

    Optional<User> findFirstByOauthIdAndProvider(String oauthId, Provider provider);

    Optional<User> findFirstByEmail(String email);

    Optional<User> findByEmailAndProvider(String email, Provider provider);

    boolean existsById(Long id);

    @Query(value = "select new tech.dut.fasto.web.rest.admin.user.dto.response.AdminUserResponseDto(u.id, u.email, u.provider, u.phoneNumber, u.status) " +
            "from User as u inner join UserRole as ur on ur.user.id=u.id inner join Role as r on r.id = ur.role.id inner join UserInformation as uf on u.id = uf.user.id where (:query IS NULL OR uf.firstName like concat('%',:query,'%') OR uf.lastName like concat('%',:query,'%')) " +
            " and (:status is null OR u.status = :status)")
    Page<AdminUserResponseDto> getAllUsers(Pageable pageable, @Param("query") String query, @Param("status") UserStatus status);

}