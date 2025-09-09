package basicapp.back.business.role.service.business;

import basicapp.back.business.role.model.Role;
import basicapp.back.business.role.repository.IRoleRepository;
import java.util.List;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RoleServiceImpl implements IRoleService {

  private final IRoleRepository roleRepository;

  @Autowired
  public RoleServiceImpl(IRoleRepository roleRepository) {
    this.roleRepository = roleRepository;
  }

  @Override
  @Transactional
  public void saveRole(Role role) {
    Objects.requireNonNull(role);
    roleRepository.save(role);
  }

  @Override
  @Transactional(readOnly = true)
  public Role getByTitle(String intitule) {
    Objects.requireNonNull(intitule);
    return roleRepository.findByTitle(intitule).orElse(null);
  }

  @Override
  public List<Role> findAll() {
    return roleRepository.findAll();
  }
}
