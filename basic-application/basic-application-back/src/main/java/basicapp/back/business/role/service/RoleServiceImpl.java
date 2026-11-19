package basicapp.back.business.role.service;

import basicapp.back.business.role.dao.IRoleDao;
import basicapp.back.business.role.model.Role;
import java.util.Objects;
import java.util.Optional;
import org.iglooproject.jpa.business.generic.service.GenericEntityServiceImpl;
import org.iglooproject.jpa.exception.SecurityServiceException;
import org.iglooproject.jpa.exception.ServiceException;
import org.jspecify.annotations.Nullable;
import org.springframework.stereotype.Service;

@Service
public class RoleServiceImpl extends GenericEntityServiceImpl<Long, Role> implements IRoleService {

  private final IRoleDao dao;

  public RoleServiceImpl(IRoleDao dao) {
    super(dao);
    this.dao = dao;
  }

  @Override
  public void saveRole(Role role) throws ServiceException, SecurityServiceException {
    Objects.requireNonNull(role);

    if (role.isNew()) {
      create(role);
    } else {
      update(role);
    }
  }

  @Override
  public @Nullable Role getByTitle(@Nullable String title) {
    return Optional.ofNullable(title).flatMap(dao::getByTitle).orElse(null);
  }
}
