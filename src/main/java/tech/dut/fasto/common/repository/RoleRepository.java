package tech.dut.fasto.common.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tech.dut.fasto.common.domain.Role;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(String roleName);
}