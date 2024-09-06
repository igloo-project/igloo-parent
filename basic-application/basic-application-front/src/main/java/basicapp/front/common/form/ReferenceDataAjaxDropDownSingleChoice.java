package basicapp.front.common.form;

import basicapp.back.business.referencedata.model.ReferenceData;
import basicapp.back.business.referencedata.search.BasicReferenceDataSearchQueryData;
import basicapp.back.business.referencedata.search.BasicReferenceDataSort;
import basicapp.back.business.referencedata.search.IBasicReferenceDataSearchQuery;
import basicapp.front.referencedata.renderer.ReferenceDataRenderer;
import com.google.common.collect.ImmutableMap;
import igloo.wicket.model.Detachables;
import igloo.wicket.spring.SpringBeanLookupCache;
import java.util.Map;
import org.apache.wicket.model.IModel;
import org.iglooproject.jpa.more.business.generic.model.search.EnabledFilter;
import org.iglooproject.jpa.more.business.sort.ISort.SortOrder;
import org.iglooproject.wicket.more.application.CoreWicketApplication;
import org.iglooproject.wicket.more.markup.html.select2.AbstractLongIdGenericEntityChoiceProvider;
import org.iglooproject.wicket.more.markup.html.select2.GenericSelect2AjaxDropDownSingleChoice;
import org.wicketstuff.select2.Response;

public class ReferenceDataAjaxDropDownSingleChoice<T extends ReferenceData<? super T>>
    extends GenericSelect2AjaxDropDownSingleChoice<T> {

  private static final long serialVersionUID = 7076114890845943476L;

  public ReferenceDataAjaxDropDownSingleChoice(String id, IModel<T> model, Class<T> clazz) {
    this(id, model, new ChoiceProvider<>(clazz));
  }

  public ReferenceDataAjaxDropDownSingleChoice(
      String id, IModel<T> model, ChoiceProvider<T> choiceProvider) {
    super(id, model, choiceProvider);
  }

  private static class ChoiceProvider<T extends ReferenceData<? super T>>
      extends AbstractLongIdGenericEntityChoiceProvider<T> {

    private static final long serialVersionUID = 1L;

    private final SpringBeanLookupCache<IBasicReferenceDataSearchQuery<T>>
        referenceDataSearchQueryLookupCache;

    public ChoiceProvider(Class<T> clazz) {
      super(clazz, ReferenceDataRenderer.get());
      this.referenceDataSearchQueryLookupCache =
          SpringBeanLookupCache.<IBasicReferenceDataSearchQuery<T>>of(
              () -> CoreWicketApplication.get().getApplicationContext(),
              IBasicReferenceDataSearchQuery.class,
              clazz);
    }

    @Override
    protected void query(String term, int offset, int limit, Response<T> response) {
      BasicReferenceDataSearchQueryData<T> data = new BasicReferenceDataSearchQueryData<>();
      data.setLabel(term);
      data.setEnabledFilter(EnabledFilter.ENABLED_ONLY);
      Map<BasicReferenceDataSort, SortOrder> sorts =
          ImmutableMap.of(
              BasicReferenceDataSort.POSITION, BasicReferenceDataSort.POSITION.getDefaultOrder(),
              BasicReferenceDataSort.LABEL_FR, BasicReferenceDataSort.LABEL_FR.getDefaultOrder(),
              BasicReferenceDataSort.LABEL_EN, BasicReferenceDataSort.LABEL_EN.getDefaultOrder(),
              BasicReferenceDataSort.ID, BasicReferenceDataSort.ID.getDefaultOrder());
      response.addAll(referenceDataSearchQueryLookupCache.get().list(data, sorts, offset, limit));
    }

    @Override
    public void detach() {
      super.detach();
      Detachables.detach(referenceDataSearchQueryLookupCache);
    }
  }
}
