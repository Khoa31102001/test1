package tech.dut.fasto.common.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.dut.fasto.common.domain.Role;
import tech.dut.fasto.common.repository.RoleRepository;
import tech.dut.fasto.common.service.RoleService;
import tech.dut.fasto.errors.FastoAlertException;


import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    private final MessageService messageService;


    @Override
    @Transactional(readOnly = true)
    public Role findOneByName(String roleName) {
        Optional<Role> optionalRole = roleRepository.findByName(roleName);
        if(optionalRole.isEmpty()){
            String codeMessage = messageService.getMessage("error.code.bad.request");
            String message = messageService.getMessage("error.role.not.found", List.of(roleName));
            throw new FastoAlertException(codeMessage, message);
        }
        return optionalRole.get();
    }
}
