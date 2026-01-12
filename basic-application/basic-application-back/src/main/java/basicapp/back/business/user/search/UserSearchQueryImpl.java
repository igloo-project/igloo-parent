package basicapp.back.business.user.search;

import static org.iglooproject.jpa.more.search.query.HibernateSearchUtils.wildcardTokensOr;

import basicapp.back.business.user.model.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiConsumer;
import org.hibernate.search.engine.search.predicate.dsl.SearchPredicateFactory;
import org.hibernate.search.engine.search.predicate.dsl.SimpleBooleanPredicateClausesCollector;
import org.hibernate.search.mapper.orm.Search;
import org.iglooproject.commons.util.exception.IllegalSwitchValueException;
import org.iglooproject.jpa.more.business.generic.model.search.EnabledFilter;
import org.iglooproject.jpa.more.business.sort.ISort;
import org.springframework.stereotype.Service;

@Service
public class UserSearchQueryImpl implements IUserSearchQuery {

  @PersistenceContext private EntityManager entityManager;

  @Override
  public Collection<User> list(
      UserSearchQueryData data,
      Map<UserSort, ISort.SortOrder> sorts,
      Integer offset,
      Integer limit) {
    if (!checkLimit(limit)) {
      return List.of();
    }

    return Search.session(entityManager)
        .search(User.class)
        .where(predicateContributor(data))
        .sort(sortContributor(sorts))
        .fetchHits(offset, limit);
  }

  @Override
  public long size(UserSearchQueryData data) {
    return Search.session(entityManager)
        .search(User.class)
        .where(predicateContributor(data))
        .fetchTotalHitCount();
  }

  @Override
  public Collection<Long> listIds(UserSearchQueryData data, Map<UserSort, ISort.SortOrder> sorts) {
    return Search.session(entityManager)
        .search(User.class)
        .select(f -> f.id(Long.class))
        .where(predicateContributor(data))
        .sort(sortContributor(sorts))
        .fetchAllHits();
  }

  private BiConsumer<
          ? super SearchPredicateFactory, ? super SimpleBooleanPredicateClausesCollector<?, ?>>
      predicateContributor(UserSearchQueryData data) {
    return (f, root) -> {
      root.add(f.matchAll());
      if (data.getTerm() != null) {
        root.add(
            f.or()
                .with(
                    or -> {
                      or.add(
                          f.simpleQueryString()
                              .field(User.LAST_NAME_AUTOCOMPLETE)
                              .matching(wildcardTokensOr(data.getTerm())));
                      or.add(
                          f.simpleQueryString()
                              .field(User.FIRST_NAME_AUTOCOMPLETE)
                              .matching(wildcardTokensOr(data.getTerm())));
                      or.add(
                          f.simpleQueryString()
                              .field(User.USERNAME_AUTOCOMPLETE)
                              .matching(wildcardTokensOr(data.getTerm())));
                    }));
      }
      if (data.getType() != null) {
        root.add(f.match().field(User.TYPE).matching(data.getType()));
      }
      if (data.getLastName() != null) {
        root.add(
            f.simpleQueryString()
                .field(User.LAST_NAME_AUTOCOMPLETE)
                .matching(wildcardTokensOr(data.getLastName())));
      }
      if (data.getFirstName() != null) {
        root.add(
            f.simpleQueryString()
                .field(User.FIRST_NAME_AUTOCOMPLETE)
                .matching(wildcardTokensOr(data.getFirstName())));
      }
      if (data.getEmail() != null) {
        root.add(
            f.simpleQueryString()
                .field(User.EMAIL_ADDRESS_AUTOCOMPLETE)
                .matching(wildcardTokensOr(data.getEmail())));
      }
      if (data.getRole() != null) {
        root.add(f.match().field(User.ROLES).matching(data.getRole()));
      }
      if (data.getActive() != null && !Objects.equals(data.getActive(), EnabledFilter.ALL)) {
        boolean active =
            switch (data.getActive()) {
              case DISABLED_ONLY -> false;
              case ENABLED_ONLY -> true;
              default -> throw new IllegalSwitchValueException(data.getActive());
            };
        root.add(f.match().field(User.ENABLED).matching(active));
      }
    };
  }
}
