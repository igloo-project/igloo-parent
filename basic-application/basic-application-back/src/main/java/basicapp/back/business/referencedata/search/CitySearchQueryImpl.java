package basicapp.back.business.referencedata.search;

import basicapp.back.business.referencedata.model.City;
import java.util.function.BiConsumer;
import org.hibernate.search.engine.search.predicate.dsl.SearchPredicateFactory;
import org.hibernate.search.engine.search.predicate.dsl.SimpleBooleanPredicateClausesCollector;

public class CitySearchQueryImpl
    extends AbstractReferenceDataSearchQueryImpl<City, CitySort, CitySearchQueryData>
    implements ICitySearchQuery {

  public CitySearchQueryImpl() {
    super(City.class);
  }

  @Override
  protected BiConsumer<SearchPredicateFactory, SimpleBooleanPredicateClausesCollector<?>>
      predicateContributor(CitySearchQueryData data) {
    return super.predicateContributor(data)
        .andThen(
            (f, root) -> {
              if (data.getPostalCode() != null) {
                root.add(f.match().field(City.POSTAL_CODE).matching(data.getPostalCode()));
              }
            });
  }
}
