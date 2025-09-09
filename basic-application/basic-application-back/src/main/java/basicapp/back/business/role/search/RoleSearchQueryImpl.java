package basicapp.back.business.role.search;

import static org.iglooproject.jpa.more.util.jparepository.JpaRepositoryUtils.createCriteriaOrders;
import static org.iglooproject.jpa.more.util.jparepository.JpaRepositoryUtils.createPageRequest;

import basicapp.back.business.role.model.Role;
import basicapp.back.business.role.model.Role_;
import basicapp.back.business.role.repository.IRoleRepository;
import basicapp.back.business.role.repository.RoleSpecifications;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.iglooproject.jpa.jparepository.SpecificationBuilder;
import org.iglooproject.jpa.more.business.sort.ISort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
public class RoleSearchQueryImpl implements IRoleSearchQuery {

  private final IRoleRepository roleRepository;
  private final EntityManager entityManager;

  @Autowired
  public RoleSearchQueryImpl(IRoleRepository roleRepository, EntityManager entityManager) {
    this.roleRepository = roleRepository;
    this.entityManager = entityManager;
  }

  @Override
  public List<Role> list(
      RoleSearchQueryData data, Map<RoleSort, SortOrder> sorts, Integer offset, Integer limit) {
    if (!checkLimit(limit)) {
      return List.of();
    }

    return roleRepository
        .findAll(predicateContributor(data), createPageRequest(sorts, offset, limit))
        .getContent();
  }

  @Override
  public long size(RoleSearchQueryData data) {
    return roleRepository.count(predicateContributor(data));
  }

  @Override
  public Collection<Long> listIds(RoleSearchQueryData data, Map<RoleSort, SortOrder> sorts) {
    CriteriaBuilder cb = entityManager.getCriteriaBuilder();
    CriteriaQuery<Long> query = cb.createQuery(Long.class);
    Root<Role> root = query.from(Role.class);

    CriteriaQuery<Long> select =
        query.select(root.get(Role_.ID)).orderBy(createCriteriaOrders(sorts, cb, root));

    Optional.ofNullable(predicateContributor(data))
        .map(spec -> spec.toPredicate(root, query, cb))
        .ifPresent(select::where);

    return entityManager.createQuery(query).getResultList();
  }

  private Specification<Role> predicateContributor(RoleSearchQueryData data) {
    SpecificationBuilder<Role> specificationBuilder = new SpecificationBuilder<>();
    if (data.getUser() != null) {
      specificationBuilder.and(RoleSpecifications.user(data.getUser()));
    }
    return specificationBuilder.build();
  }
}
