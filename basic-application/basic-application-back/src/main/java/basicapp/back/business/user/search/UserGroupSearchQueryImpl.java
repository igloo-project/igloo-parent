package basicapp.back.business.user.search;

import basicapp.back.business.user.model.User;
import basicapp.back.business.user.model.UserGroup;
import com.google.common.collect.ImmutableList;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import org.hibernate.search.engine.search.predicate.dsl.SearchPredicateFactory;
import org.hibernate.search.engine.search.predicate.dsl.SimpleBooleanPredicateClausesCollector;
import org.hibernate.search.mapper.orm.Search;
import org.iglooproject.jpa.more.business.sort.ISort;
import org.springframework.stereotype.Service;

@Service
public class UserGroupSearchQueryImpl implements IUserGroupSearchQuery {

  @PersistenceContext private EntityManager entityManager;

  @Override
  public Collection<UserGroup> list(
      UserGroupSearchQueryData data,
      Map<UserGroupSort, ISort.SortOrder> sorts,
      Integer offset,
      Integer limit) {
    if (!checkLimit(limit)) {
      return List.of();
    }

    return Search.session(entityManager)
        .search(UserGroup.class)
        .where(predicateContributor(data))
        .sort(sortContributor(sorts))
        .fetchHits(offset, limit);
  }

  @Override
  public long size(UserGroupSearchQueryData data) {
    return Search.session(entityManager)
        .search(UserGroup.class)
        .where(predicateContributor(data))
        .fetchTotalHitCount();
  }

  private BiConsumer<
          ? super SearchPredicateFactory, ? super SimpleBooleanPredicateClausesCollector<?>>
      predicateContributor(UserGroupSearchQueryData data) {
    return (f, root) -> {
      root.add(f.matchAll());
      if (data.getName() != null) {
        root.add(f.match().field(UserGroup.NAME).matching(data.getName()));
      }
      if (data.getUser() != null) {
        matchUser(f, root, data.getUser());
      }
      if (data.getBasicUser() != null) {
        matchUser(f, root, data.getBasicUser());
      }
      if (data.getTechnicalUser() != null) {
        matchUser(f, root, data.getTechnicalUser());
      }
    };
  }

  private void matchUser(
      SearchPredicateFactory f, SimpleBooleanPredicateClausesCollector<?> root, User user) {
    Collection<Long> ids =
        user.getGroups().stream().map(UserGroup::getId).collect(ImmutableList.toImmutableList());
    if (ids.isEmpty()) {
      root.add(f.matchNone());
    } else {
      root.add(f.terms().field(UserGroup.ID).matchingAny(ids));
    }
  }
}
