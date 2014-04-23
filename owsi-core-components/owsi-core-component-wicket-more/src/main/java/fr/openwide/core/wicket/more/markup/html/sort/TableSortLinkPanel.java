package fr.openwide.core.wicket.more.markup.html.sort;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.navigation.paging.IPageable;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;

import fr.openwide.core.jpa.more.business.sort.ISort;
import fr.openwide.core.jpa.more.business.sort.ISort.SortOrder;
import fr.openwide.core.wicket.behavior.ClassAttributeAppender;
import fr.openwide.core.wicket.more.markup.html.sort.model.CompositeSortModel;

public class TableSortLinkPanel<T extends ISort<?>> extends Panel {

	private static final long serialVersionUID = 9088886381233297454L;
	
	private final IPageable pageable;
	
	private final CompositeSortModel<T> compositeSortModel;
	
	private final T sort;
	
	private CycleMode cycleMode = CycleMode.NONE_DEFAULT;
	
	private ISortIconStyle sortIconCssStyle = SortIconStyle.DEFAULT;
	
	public static enum CycleMode {
		NONE_DEFAULT {
			@Override
			protected SortOrder getNext(ISort<?> sort, SortOrder currentOrder) {
				if (currentOrder == null) {
					return sort.getDefaultOrder();
				} else {
					return null;
				}
			}
		},
		NONE_DEFAULT_REVERSE {
			@Override
			protected SortOrder getNext(ISort<?> sort, SortOrder currentOrder) {
				if (currentOrder == null) {
					return sort.getDefaultOrder();
				} else if (currentOrder.equals(sort.getDefaultOrder())){
					return sort.getDefaultOrder().reverse();
				} else {
					return null;
				}
			}
		},
		DEFAULT_REVERSE {
			@Override
			protected SortOrder getNext(ISort<?> sort, SortOrder currentOrder) {
				SortOrder defaultWay = sort.getDefaultOrder();
				SortOrder defaultReverseWay = defaultWay.reverse();
				
				if (currentOrder == null || currentOrder.equals(defaultReverseWay)) {
					return defaultWay;
				} else {
					return defaultReverseWay;
				}
			}
		};
		
		protected abstract SortOrder getNext(ISort<?> sort, SortOrder currentOrder);
	}


	public TableSortLinkPanel(String id, CompositeSortModel<T> compositeSortModel, T sort, IPageable pageable) {
		this(id, compositeSortModel, sort, pageable, null);
	}
	
	public TableSortLinkPanel(String id, CompositeSortModel<T> compositeSortModel, T sort, IPageable pageable, IModel<String> tooltipTextModel) {
		super(id, compositeSortModel);
		
		this.compositeSortModel = compositeSortModel;
		this.pageable = pageable;
		this.sort = sort;
		
		AjaxLink<?> sortLink = new AjaxLink<Void>("sortLink") {
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick(AjaxRequestTarget target) {
				TableSortLinkPanel.this.doOnClick(target);
			}
		};
		sortLink.add(new AttributeAppender("title", tooltipTextModel));
		sortLink.add(new ClassAttributeAppender(new LoadableDetachableModel<String>() {
			private static final long serialVersionUID = 1L;
			
			@Override
			protected String load() {
				if (TableSortLinkPanel.this.isSortActive()) {
					return "active";
				}
				return null;
			}
		}));
		add(sortLink);
		
		WebMarkupContainer iconAscContainer = new WebMarkupContainer("iconAsc") {
			private static final long serialVersionUID = 1L;
			@Override
			protected void onConfigure() {
				super.onConfigure();
				
				SortOrder displayedSort = TableSortLinkPanel.this.getDisplayedSort();
				setVisible(SortOrder.ASC.equals(displayedSort));
			}
		};
		iconAscContainer.add(new ClassAttributeAppender(new AbstractReadOnlyModel<String>() {
			private static final long serialVersionUID = 1L;
			@Override
			public String getObject() {
				return sortIconCssStyle.getAscIconCssClasses();
			}
		}));
		sortLink.add(iconAscContainer);
		
		WebMarkupContainer iconDescContainer = new WebMarkupContainer("iconDesc") {
			private static final long serialVersionUID = 1L;
			@Override
			protected void onConfigure() {
				super.onConfigure();
				
				SortOrder displayedSort = TableSortLinkPanel.this.getDisplayedSort();
				setVisible(SortOrder.DESC.equals(displayedSort));
			}
		};
		iconDescContainer.add(new ClassAttributeAppender(new AbstractReadOnlyModel<String>() {
			private static final long serialVersionUID = 1L;
			@Override
			public String getObject() {
				return sortIconCssStyle.getDescIconCssClasses();
			}
		}));
		sortLink.add(iconDescContainer);
	}
	
	public TableSortLinkPanel<T> cycleMode(CycleMode cycleMode) {
		this.cycleMode = cycleMode;
		return this;
	}
	
	public TableSortLinkPanel<T> iconStyle(ISortIconStyle iconStyle) {
		this.sortIconCssStyle = iconStyle;
		return this;
	}
	
	private boolean isSortActive() {
		return compositeSortModel.getOrder(sort) != null;
	}
	
	private SortOrder getDisplayedSort() {
		final SortOrder currentOrder = compositeSortModel.getOrder(sort);
		return sort.getDefaultOrder().asDefaultFor(currentOrder);
	}
	
	protected void switchSort() {
		final SortOrder currentOrder = compositeSortModel.getOrder(sort);
		final SortOrder nextOrder = cycleMode.getNext(sort, currentOrder);
		compositeSortModel.setOrder(sort, nextOrder);
	}

	protected void doOnClick(AjaxRequestTarget target) {
		switchSort();
		pageable.setCurrentPage(0);
		refreshOnSort(target);
	}
	
	protected void refreshOnSort(AjaxRequestTarget target) {
		setResponsePage(getPage());
	}
}
