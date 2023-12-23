package tech.dut.fasto.common.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tech.dut.fasto.common.domain.UserRole;

import java.util.Optional;

@SuppressWarnings("unused")
@Repository
public interface UserRoleRepository extends JpaRepository<UserRole, Long> {
    Optional<UserRole> findByUserEmailAndRoleName(String email, String roleName);
}