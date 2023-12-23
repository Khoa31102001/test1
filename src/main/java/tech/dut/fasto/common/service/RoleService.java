package tech.dut.fasto.common.service;

import tech.dut.fasto.common.domain.Role;

public interface RoleService {
    Role findOneByName(String roleName);
}
