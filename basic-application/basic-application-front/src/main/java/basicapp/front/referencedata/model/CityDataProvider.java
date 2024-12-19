package basicapp.front.referencedata.model;

import basicapp.back.business.referencedata.model.City;
import basicapp.back.business.referencedata.search.CitySearchQueryData;
import basicapp.back.business.referencedata.search.CitySort;
import basicapp.back.business.referencedata.search.ICitySearchQuery;
import basicapp.back.util.binding.Bindings;
import com.google.common.collect.ImmutableMap;
import java.util.function.UnaryOperator;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.iglooproject.wicket.more.markup.html.sort.model.CompositeSortModel;
import org.iglooproject.wicket.more.markup.html.sort.model.CompositeSortModel.CompositingStrategy;
import org.iglooproject.wicket.more.model.data.DataModel;
import org.iglooproject.wicket.more.model.search.query.SearchQueryDataProvider;

public class CityDataProvider
    extends SearchQueryDataProvider<City, CitySort, CitySearchQueryData, ICitySearchQuery> {

  private static final long serialVersionUID = 1L;

  @SpringBean private ICitySearchQuery searchQuery;

  private final CompositeSortModel<CitySort> sortModel =
      new CompositeSortModel<>(
          CompositingStrategy.LAST_ONLY,
          ImmutableMap.of(
              CitySort.POSITION, CitySort.POSITION.getDefaultOrder(),
              CitySort.LABEL_FR, CitySort.LABEL_FR.getDefaultOrder(),
              CitySort.LABEL_EN, CitySort.LABEL_EN.getDefaultOrder()),
          ImmutableMap.of(CitySort.ID, CitySort.ID.getDefaultOrder()));

  public CityDataProvider() {
    this(UnaryOperator.identity());
  }

  public CityDataProvider(UnaryOperator<DataModel<CitySearchQueryData>> dataModelOperator) {
    this(
        dataModelOperator.apply(
            new DataModel<>(CitySearchQueryData::new)
                .bind(Bindings.citySearchQueryData().label(), Model.of())
                .bind(Bindings.citySearchQueryData().postalCode(), Model.of())
                .bind(Bindings.citySearchQueryData().enabledFilter(), Model.of())));
  }

  public CityDataProvider(IModel<CitySearchQueryData> dataModel) {
    super(dataModel);
  }

  @Override
  public CompositeSortModel<CitySort> getSortModel() {
    return sortModel;
  }

  @Override
  protected ICitySearchQuery searchQuery() {
    return searchQuery;
  }
}