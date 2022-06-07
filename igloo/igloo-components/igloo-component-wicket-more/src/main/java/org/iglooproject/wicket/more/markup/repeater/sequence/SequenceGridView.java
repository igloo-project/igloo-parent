package org.iglooproject.wicket.more.markup.repeater.sequence;

import java.util.List;

import org.apache.wicket.WicketRuntimeException;
import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.RepeatingView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.iglooproject.wicket.model.Detachables;
import org.iglooproject.wicket.model.ISequenceProvider;

public class SequenceGridView<T> extends SequenceView<T> {
	private static final long serialVersionUID = 1L;

	public static final String CELL_REPEATER_ID = "cells";
	public static final String CELL_ITEM_ID = "cell";

	private final List<? extends ICellPopulator<T>> populators;

	public SequenceGridView(final String id, final List<? extends ICellPopulator<T>> populators,
			final ISequenceProvider<T> sequenceProvider) {
		super(id, sequenceProvider);
		this.populators = populators;
	}

	@Override
	protected void onDetach() {
		super.onDetach();
		Detachables.detach(populators);
	}

	@Override
	protected final void populateItem(final Item<T> item) {
		RepeatingView cells = new RepeatingView(CELL_REPEATER_ID);
		item.add(cells);

		int populatorsNumber = populators.size();
		for (int i = 0; i < populatorsNumber; i++) {
			ICellPopulator<T> populator = populators.get(i);
			IModel<ICellPopulator<T>> populatorModel = new Model<>(populator);
			Item<ICellPopulator<T>> cellItem = newCellItem(cells.newChildId(), i, populatorModel);
			cells.add(cellItem);

			populator.populateItem(cellItem, CELL_ITEM_ID, item.getModel());

			if (cellItem.get("cell") == null) {
				throw new WicketRuntimeException(
						populator.getClass().getName()
						+ ".populateItem() failed to add a component with id [" + CELL_ITEM_ID + "] to the provided"
						+ " [cellItem] object. Make sure you call add() on cellItem and make sure you gave the added"
						+ " component passed in 'componentId' id."
						+ " (*cellItem*.add(new MyComponent(*componentId*, rowModel) )");
			}
		}
	}

	@Override
	protected final Item<T> newItem(final String id, final int index, final IModel<T> model) {
		return newRowItem(id, index, model);
	}

	protected final List<? extends ICellPopulator<T>> internalGetPopulators() {
		return populators;
	}

	protected Item<ICellPopulator<T>> newCellItem(final String id, final int index,
			final IModel<ICellPopulator<T>> model) {
		return new Item<>(id, index, model);
	}

	protected Item<T> newRowItem(final String id, final int index, final IModel<T> model) {
		return new Item<>(id, index, model);
	}

}
