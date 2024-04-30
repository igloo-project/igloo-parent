package org.iglooproject.basicapp.web.application.referencedata.component;

import static org.iglooproject.basicapp.web.application.common.util.CssClassConstants.CELL_DISPLAY_MD;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.model.ResourceModel;
import org.iglooproject.basicapp.core.business.referencedata.model.ReferenceData;
import org.iglooproject.basicapp.core.business.referencedata.search.BasicReferenceDataSearchQueryData;
import org.iglooproject.basicapp.core.business.referencedata.search.BasicReferenceDataSort;
import org.iglooproject.basicapp.core.util.binding.Bindings;
import org.iglooproject.basicapp.web.application.referencedata.form.AbstractReferenceDataPopup;
import org.iglooproject.basicapp.web.application.referencedata.form.BasicReferenceDataPopup;
import org.iglooproject.basicapp.web.application.referencedata.model.BasicReferenceDataDataProvider;
import org.iglooproject.functional.SerializableSupplier2;
import org.iglooproject.wicket.more.markup.html.sort.SortIconStyle;
import org.iglooproject.wicket.more.markup.html.sort.TableSortLink.CycleMode;
import org.iglooproject.wicket.more.markup.repeater.table.DecoratedCoreDataTablePanel;
import org.iglooproject.wicket.more.markup.repeater.table.builder.DataTableBuilder;
import org.iglooproject.wicket.more.markup.repeater.table.builder.state.IColumnState;

public class BasicReferenceDataListPanel<T extends ReferenceData<? super T>>
		extends AbstractReferenceDataListPanel<T, BasicReferenceDataSort, BasicReferenceDataSearchQueryData<T>, BasicReferenceDataDataProvider<T>> {

	private static final long serialVersionUID = -4026683202098875499L;

	public BasicReferenceDataListPanel(
		String id,
		SerializableSupplier2<T> supplier,
		Class<T> clazz
	) {
		super(id, new BasicReferenceDataDataProvider<>(clazz), supplier);
		setOutputMarkupId(true);
	}

	@Override
	protected AbstractReferenceDataPopup<T> createPopup(String wicketId) {
		return new BasicReferenceDataPopup<>(wicketId) {
			private static final long serialVersionUID = 1L;
			@Override
			protected void refresh(AjaxRequestTarget target) {
				target.add(results);
			}
		};
	}

	@Override
	protected IColumnState<T, BasicReferenceDataSort> addColumns(DataTableBuilder<T, BasicReferenceDataSort> builder) {
		return super.addColumns(builder)
			.addLabelColumn(new ResourceModel("business.referenceData.label.fr"), Bindings.referenceData().label().fr())
				.withSort(BasicReferenceDataSort.LABEL_FR, SortIconStyle.ALPHABET, CycleMode.NONE_DEFAULT_REVERSE)
				.withClass("cell-w-300")
			.addLabelColumn(new ResourceModel("business.referenceData.label.en"), Bindings.referenceData().label().en())
				.withSort(BasicReferenceDataSort.LABEL_EN, SortIconStyle.ALPHABET, CycleMode.NONE_DEFAULT_REVERSE)
				.withClass("cell-w-300")
				.withClass(CELL_DISPLAY_MD);
	}

	@Override
	protected Component createSearchForm(
		String wicketId,
		BasicReferenceDataDataProvider<T> dataProvider,
		DecoratedCoreDataTablePanel<T, BasicReferenceDataSort> table
	) {
		return new BasicReferenceDataSearchPanel<>(wicketId, dataProvider, table);
	}

}
