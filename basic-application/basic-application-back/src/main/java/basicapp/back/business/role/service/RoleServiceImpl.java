package basicapp.back.business.role.service;

import basicapp.back.business.role.dao.IRoleDao;
import basicapp.back.business.role.model.Role;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;
import org.iglooproject.jpa.business.generic.service.GenericEntityServiceImpl;
import org.iglooproject.jpa.exception.SecurityServiceException;
import org.iglooproject.jpa.exception.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleServiceImpl extends GenericEntityServiceImpl<Long, Role> implements IRoleService {

  private final IRoleDao dao;

  @Autowired
  public RoleServiceImpl(IRoleDao dao) {
    super(dao);
    this.dao = dao;
  }

  @Override
  public List<Role> listByIds(Collection<Long> ids) {
    return Optional.ofNullable(ids)
        .filter(Predicate.not(Collection::isEmpty))
        .map(dao::listByIds)
        .orElse(List.of());
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
  public Role getByTitle(String title) {
    return Optional.ofNullable(title)
        .filter(Predicate.not(String::isEmpty))
        .map(dao::getByTitle)
        .orElse(null);
  }
}
