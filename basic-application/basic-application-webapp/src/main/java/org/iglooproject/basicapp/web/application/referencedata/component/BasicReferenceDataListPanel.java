package org.iglooproject.basicapp.web.application.referencedata.component;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.model.ResourceModel;
import org.iglooproject.basicapp.core.business.referencedata.model.ReferenceData;
import org.iglooproject.basicapp.core.business.referencedata.search.ReferenceDataSort;
import org.iglooproject.basicapp.core.util.binding.Bindings;
import org.iglooproject.basicapp.web.application.referencedata.form.AbstractReferenceDataPopup;
import org.iglooproject.basicapp.web.application.referencedata.form.BasicReferenceDataPopup;
import org.iglooproject.basicapp.web.application.referencedata.model.AbstractReferenceDataDataProvider;
import org.iglooproject.basicapp.web.application.referencedata.model.BasicReferenceDataDataProvider;
import org.iglooproject.functional.SerializableSupplier2;
import org.iglooproject.wicket.more.markup.html.sort.SortIconStyle;
import org.iglooproject.wicket.more.markup.html.sort.TableSortLink.CycleMode;
import org.iglooproject.wicket.more.markup.repeater.table.DecoratedCoreDataTablePanel;
import org.iglooproject.wicket.more.markup.repeater.table.builder.DataTableBuilder;
import org.iglooproject.wicket.more.markup.repeater.table.builder.state.IColumnState;

public class BasicReferenceDataListPanel<T extends ReferenceData<? super T>> 
		extends AbstractReferenceDataListPanel<T, ReferenceDataSort, AbstractReferenceDataDataProvider<T, ReferenceDataSort>> {

	private static final long serialVersionUID = -4026683202098875499L;

	public BasicReferenceDataListPanel(
		String id,
		SerializableSupplier2<T> supplier,
		Class<T> clazz
	) {
		this(id, supplier, BasicReferenceDataDataProvider.forItemType(clazz));
	}

	public BasicReferenceDataListPanel(
		String id,
		SerializableSupplier2<T> supplier,
		AbstractReferenceDataDataProvider<T, ReferenceDataSort> dataProvider
	) {
		super(id, dataProvider, dataProvider.getSortModel(), supplier);
		setOutputMarkupId(true);
	}

	@Override
	protected AbstractReferenceDataPopup<T> createPopup(String wicketId) {
		return new BasicReferenceDataPopup<T>(wicketId) {
			private static final long serialVersionUID = 1L;
			@Override
			protected void refresh(AjaxRequestTarget target) {
				target.add(results);
			}
		};
	}

	@Override
	protected IColumnState<T, ReferenceDataSort> addColumns(DataTableBuilder<T, ReferenceDataSort> builder) {
		return super.addColumns(builder)
			.addLabelColumn(new ResourceModel("business.referenceData.label.fr"), Bindings.referenceData().label().fr())
				.withSort(ReferenceDataSort.LABEL_FR, SortIconStyle.ALPHABET, CycleMode.NONE_DEFAULT_REVERSE)
				.withClass("text text-md")
			.addLabelColumn(new ResourceModel("business.referenceData.label.en"), Bindings.referenceData().label().en())
				.withSort(ReferenceDataSort.LABEL_EN, SortIconStyle.ALPHABET, CycleMode.NONE_DEFAULT_REVERSE)
				.withClass("text text-md");
	}

	@Override
	protected Component createSearchForm(
		String wicketId,
		AbstractReferenceDataDataProvider<T, ReferenceDataSort> dataProvider,
		DecoratedCoreDataTablePanel<T, ReferenceDataSort> table
	) {
		return new BasicReferenceDataSearchPanel<T>(wicketId, dataProvider, table);
	}

}
