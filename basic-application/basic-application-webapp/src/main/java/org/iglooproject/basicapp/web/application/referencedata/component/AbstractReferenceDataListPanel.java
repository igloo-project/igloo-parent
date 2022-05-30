package org.iglooproject.basicapp.web.application.referencedata.component;

import static org.iglooproject.basicapp.web.application.common.util.CssClassConstants.BTN_TABLE_ROW_ACTION;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.panel.Fragment;
import org.apache.wicket.model.IModel;
import org.iglooproject.basicapp.web.application.common.renderer.ActionRenderers;
import org.iglooproject.basicapp.web.application.referencedata.form.AbstractReferenceDataPopup;
import org.iglooproject.functional.SerializableSupplier2;
import org.iglooproject.jpa.more.business.referencedata.model.GenericReferenceData;
import org.iglooproject.jpa.more.business.sort.ISort;
import org.iglooproject.wicket.api.condition.Condition;
import org.iglooproject.wicket.modal.AjaxModalOpenBehavior;
import org.iglooproject.wicket.modal.OneParameterModalOpenAjaxAction;
import org.iglooproject.wicket.more.markup.html.basic.EnclosureContainer;
import org.iglooproject.wicket.more.markup.html.link.BlankLink;
import org.iglooproject.wicket.more.markup.html.sort.model.CompositeSortModel;
import org.iglooproject.wicket.more.markup.repeater.table.DecoratedCoreDataTablePanel.AddInPlacement;
import org.iglooproject.wicket.more.markup.repeater.table.builder.state.IColumnState;
import org.iglooproject.wicket.more.markup.repeater.table.builder.state.IDecoratedBuildState;
import org.iglooproject.wicket.more.model.AbstractSearchQueryDataProvider;
import org.wicketstuff.wiquery.core.events.MouseEvent;

public abstract class AbstractReferenceDataListPanel
		<
			T extends GenericReferenceData<? super T, ?>,
			S extends ISort<?>,
			D extends AbstractSearchQueryDataProvider<T, S>
		>
		extends AbstractReferenceDataSimpleListPanel<T, S, D> {

	private static final long serialVersionUID = -8240552205613934114L;

	protected final SerializableSupplier2<T> supplier;

	private final AbstractReferenceDataPopup<T> popup;

	public AbstractReferenceDataListPanel(String id, D dataProvider, CompositeSortModel<S> sortModel, SerializableSupplier2<T> supplier) {
		super(id, dataProvider, sortModel);
		
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
				.addAction(ActionRenderers.edit(), new OneParameterModalOpenAjaxAction<IModel<T>>(getPopup()) {
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
				.withClass("cell-w-actions-1x");
		return super.addActionColumn(builder);
	}

	@Override
	protected IDecoratedBuildState<T, S> decorate(IDecoratedBuildState<T, S> builder) {
		return super.decorate(builder)
			.addIn(AddInPlacement.HEADING_RIGHT, GlobalActionsFragment::new);
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
							.add(new AjaxModalOpenBehavior(getPopup(), MouseEvent.CLICK) {
								private static final long serialVersionUID = 1L;
								@Override
								protected void onShow(AjaxRequestTarget target) {
									getPopup().setUpAdd(supplier.get());
								}
							})
							.add(getAddCondition().thenShow())
					)
			);
		}
	}

}
