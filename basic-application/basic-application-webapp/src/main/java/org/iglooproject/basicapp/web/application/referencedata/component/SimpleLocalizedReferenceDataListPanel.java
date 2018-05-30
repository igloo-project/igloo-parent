package org.iglooproject.basicapp.web.application.referencedata.component;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.model.ResourceModel;
import org.iglooproject.basicapp.core.business.referencedata.model.LocalizedReferenceData;
import org.iglooproject.basicapp.core.business.referencedata.search.LocalizedReferenceDataSort;
import org.iglooproject.basicapp.core.util.binding.Bindings;
import org.iglooproject.basicapp.web.application.referencedata.form.AbstractGenericReferenceDataPopup;
import org.iglooproject.basicapp.web.application.referencedata.form.SimpleLocalizedReferenceDataPopup;
import org.iglooproject.basicapp.web.application.referencedata.model.AbstractLocalizedReferenceDataDataProvider;
import org.iglooproject.basicapp.web.application.referencedata.model.SimpleLocalizedReferenceDataDataProvider;
import org.iglooproject.functional.SerializableSupplier2;
import org.iglooproject.wicket.more.markup.html.sort.SortIconStyle;
import org.iglooproject.wicket.more.markup.html.sort.TableSortLink.CycleMode;
import org.iglooproject.wicket.more.markup.repeater.table.DecoratedCoreDataTablePanel;
import org.iglooproject.wicket.more.markup.repeater.table.builder.DataTableBuilder;
import org.iglooproject.wicket.more.markup.repeater.table.builder.state.IAddedCoreColumnState;

public class SimpleLocalizedReferenceDataListPanel<T extends LocalizedReferenceData<? super T>> 
		extends AbstractReferenceDataListPanel<T, LocalizedReferenceDataSort, AbstractLocalizedReferenceDataDataProvider<T, LocalizedReferenceDataSort>> {

	private static final long serialVersionUID = -4026683202098875499L;

	protected SerializableSupplier2<T> supplier;

	public SimpleLocalizedReferenceDataListPanel(
			String id,
			SerializableSupplier2<T> supplier,
			SimpleLocalizedReferenceDataDataProvider<T, LocalizedReferenceDataSort> dataProvider
	) {
		super(id, dataProvider, dataProvider.getSortModel());
		this.supplier = supplier;
		setOutputMarkupId(true);
	}

	@Override
	protected T getNewInstance() {
		return this.supplier.get();
	}

	@Override
	protected AbstractGenericReferenceDataPopup<T> createPopup(String wicketId) {
		return new SimpleLocalizedReferenceDataPopup<T>(wicketId) {
			private static final long serialVersionUID = 1L;
			@Override
			protected void refresh(AjaxRequestTarget target) {
				target.add(resultats);
			}
		};
	}

	@Override
	protected IAddedCoreColumnState<T, LocalizedReferenceDataSort> addColumns(
			DataTableBuilder<T, LocalizedReferenceDataSort> builder
	) {
		return builder
					.addLabelColumn(new ResourceModel("business.localizedReferenceData.label.fr"), Bindings.localizedReferenceData().label().fr())
							.withSort(LocalizedReferenceDataSort.LABEL_FR, SortIconStyle.ALPHABET, CycleMode.NONE_DEFAULT_REVERSE)
							.withClass("text text-md")
					.addLabelColumn(new ResourceModel("business.localizedReferenceData.label.en"), Bindings.localizedReferenceData().label().en())
							.withSort(LocalizedReferenceDataSort.LABEL_EN, SortIconStyle.ALPHABET, CycleMode.NONE_DEFAULT_REVERSE)
							.withClass("text text-md");
	}

	@Override
	protected Component createSearchForm(
			String wicketId,
			AbstractLocalizedReferenceDataDataProvider<T, LocalizedReferenceDataSort> dataProvider,
			DecoratedCoreDataTablePanel<T, LocalizedReferenceDataSort> table
	) {
		return new SimpleLocalizedReferenceDataSearchPanel<T>(wicketId, dataProvider, table);
	}

}
