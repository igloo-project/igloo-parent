package basicapp.front.referencedata.component;

import static basicapp.front.common.util.CssClassConstants.BTN_TABLE_ROW_ACTION;

import basicapp.back.business.referencedata.model.ReferenceData;
import basicapp.front.common.renderer.ActionRenderers;
import basicapp.front.referencedata.form.AbstractReferenceDataPopup;
import igloo.bootstrap.modal.AjaxModalOpenBehavior;
import igloo.bootstrap.modal.OneParameterModalOpenAjaxAction;
import igloo.wicket.component.EnclosureContainer;
import igloo.wicket.condition.Condition;
import java.util.function.Function;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.panel.Fragment;
import org.apache.wicket.model.IModel;
import org.hibernate.search.engine.search.sort.dsl.SearchSortFactory;
import org.hibernate.search.engine.search.sort.dsl.SortFinalStep;
import org.iglooproject.functional.SerializableSupplier2;
import org.iglooproject.jpa.more.business.sort.ISort;
import org.iglooproject.jpa.more.search.query.IHibernateSearchSearchQuery;
import org.iglooproject.jpa.more.search.query.ISearchQueryData;
import org.iglooproject.wicket.more.markup.html.link.BlankLink;
import org.iglooproject.wicket.more.markup.repeater.table.DecoratedCoreDataTablePanel.AddInPlacement;
import org.iglooproject.wicket.more.markup.repeater.table.builder.state.IColumnState;
import org.iglooproject.wicket.more.markup.repeater.table.builder.state.IDecoratedBuildState;
import org.iglooproject.wicket.more.model.search.query.SearchQueryDataProvider;
import org.wicketstuff.wiquery.core.events.MouseEvent;

public abstract class AbstractReferenceDataListPanel<
        T extends ReferenceData<? super T>,
        S extends ISort<Function<SearchSortFactory, SortFinalStep>>,
        D extends ISearchQueryData<T>,
        P extends SearchQueryDataProvider<T, S, D, ? extends IHibernateSearchSearchQuery<T, S, D>>>
    extends AbstractReferenceDataSimpleListPanel<T, S, D, P> {

  private static final long serialVersionUID = -8240552205613934114L;

  protected final SerializableSupplier2<T> supplier;

  private final AbstractReferenceDataPopup<T> popup;

  public AbstractReferenceDataListPanel(
      String id, P dataProvider, SerializableSupplier2<T> supplier) {
    super(id, dataProvider);

    this.supplier = supplier;

    popup = createPopup("popup");
    add(popup);
  }

  protected abstract AbstractReferenceDataPopup<T> createPopup(String wicketId);

  protected AbstractReferenceDataPopup<T> getPopup() {
    return popup;
  }

  protected Condition getAddCondition() {
    return Condition.alwaysTrue();
  }

  protected Condition getEditCondition() {
    return Condition.alwaysTrue();
  }

  protected Condition getEditCondition(IModel<? extends T> itemModel) {
    return Condition.alwaysTrue();
  }

  @Override
  protected IColumnState<T, S> addActionColumn(IColumnState<T, S> builder) {
    builder
        .addActionColumn()
        .addAction(
            ActionRenderers.edit(),
            new OneParameterModalOpenAjaxAction<IModel<T>>(getPopup()) {
              private static final long serialVersionUID = 1L;

              @Override
              protected void onShow(AjaxRequestTarget target, IModel<T> parameter) {
                super.onShow(target, parameter);
                getPopup().setUpEdit(parameter.getObject());
              }
            })
        .when(itemModel -> getEditCondition(itemModel))
        .withClassOnElements(BTN_TABLE_ROW_ACTION)
        .end()
        .when(getEditCondition())
        .withClass("cell-w-actions-1x cell-w-fit");
    return super.addActionColumn(builder);
  }

  @Override
  protected IDecoratedBuildState<T, S> decorate(IDecoratedBuildState<T, S> builder) {
    return super.decorate(builder).addIn(AddInPlacement.HEADING_RIGHT, GlobalActionsFragment::new);
  }

  private class GlobalActionsFragment extends Fragment {

    private static final long serialVersionUID = 1L;

    public GlobalActionsFragment(String id) {
      super(id, "globalActionsFragment", AbstractReferenceDataListPanel.this);

      add(Condition.anyChildVisible(GlobalActionsFragment.this).thenShow());

      add(
          new EnclosureContainer("actionsContainer")
              .anyChildVisible()
              .add(
                  new BlankLink("add")
                      .add(
                          new AjaxModalOpenBehavior(getPopup(), MouseEvent.CLICK) {
                            private static final long serialVersionUID = 1L;

                            @Override
                            protected void onShow(AjaxRequestTarget target) {
                              getPopup().setUpAdd(supplier.get());
                            }
                          })
                      .add(getAddCondition().thenShow())));
    }
  }
}
