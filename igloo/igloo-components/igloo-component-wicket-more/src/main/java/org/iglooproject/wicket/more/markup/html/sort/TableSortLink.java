package org.iglooproject.wicket.more.markup.html.sort;

import com.google.common.collect.Maps;
import igloo.wicket.behavior.ClassAttributeAppender;
import java.util.Map;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.navigation.paging.IPageable;
import org.apache.wicket.markup.html.panel.IMarkupSourcingStrategy;
import org.apache.wicket.markup.html.panel.PanelMarkupSourcingStrategy;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.iglooproject.jpa.more.business.sort.ISort;
import org.iglooproject.jpa.more.business.sort.ISort.SortOrder;
import org.iglooproject.wicket.more.markup.html.sort.model.CompositeSortModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * CAUTION when extending : this "link" uses a PANEL markup sourcing strategy, for implementation
 * purposes.
 */
public class TableSortLink<T extends ISort<?>> extends AjaxLink<Void> {

  private static final long serialVersionUID = 9088886381233297454L;

  private static final Logger LOGGER = LoggerFactory.getLogger(TableSortLink.class);

  private static final String SORT_BUTTON_CSS_CLASS = "btn btn-tab-sort";

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
        } else if (currentOrder.equals(sort.getDefaultOrder())) {
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

  public TableSortLink(String id, CompositeSortModel<T> compositeSortModel, T sort) {
    this(id, compositeSortModel, sort, null, null);
  }

  public TableSortLink(
      String id,
      CompositeSortModel<T> compositeSortModel,
      T sort,
      IModel<String> tooltipTextModel) {
    this(id, compositeSortModel, sort, null, tooltipTextModel);
  }

  public TableSortLink(
      String id, CompositeSortModel<T> compositeSortModel, T sort, IPageable pageable) {
    this(id, compositeSortModel, sort, pageable, null);
  }

  public TableSortLink(
      String id,
      CompositeSortModel<T> compositeSortModel,
      T sort,
      IPageable pageable,
      IModel<String> tooltipTextModel) {
    super(id);

    this.compositeSortModel = compositeSortModel;
    this.pageable = pageable;
    this.sort = sort;

    add(new AttributeAppender("title", tooltipTextModel));
    add(new ClassAttributeAppender(SORT_BUTTON_CSS_CLASS));
    add(
        new ClassAttributeAppender(
            new LoadableDetachableModel<String>() {
              private static final long serialVersionUID = 1L;

              @Override
              protected String load() {
                if (TableSortLink.this.isSortActive()) {
                  return "active";
                }
                return null;
              }
            }));

    WebMarkupContainer iconAscContainer =
        new WebMarkupContainer("iconAsc") {
          private static final long serialVersionUID = 1L;

          @Override
          protected void onConfigure() {
            super.onConfigure();

            SortOrder displayedSort = TableSortLink.this.getDisplayedSort();
            setVisible(SortOrder.ASC.equals(displayedSort));
          }
        };
    iconAscContainer.add(
        new ClassAttributeAppender(
            new IModel<String>() {
              private static final long serialVersionUID = 1L;

              @Override
              public String getObject() {
                return sortIconCssStyle.getAscIconCssClasses();
              }
            }));
    add(iconAscContainer);

    WebMarkupContainer iconDescContainer =
        new WebMarkupContainer("iconDesc") {
          private static final long serialVersionUID = 1L;

          @Override
          protected void onConfigure() {
            super.onConfigure();

            SortOrder displayedSort = TableSortLink.this.getDisplayedSort();
            setVisible(SortOrder.DESC.equals(displayedSort));
          }
        };
    iconDescContainer.add(
        new ClassAttributeAppender(
            new IModel<String>() {
              private static final long serialVersionUID = 1L;

              @Override
              public String getObject() {
                return sortIconCssStyle.getDescIconCssClasses();
              }
            }));
    add(iconDescContainer);
  }

  @Override
  protected void onDetach() {
    super.onDetach();
    compositeSortModel.detach();
  }

  @Override
  protected IMarkupSourcingStrategy newMarkupSourcingStrategy() {
    return new PanelMarkupSourcingStrategy(false);
  }

  @Override
  public void onClick(AjaxRequestTarget target) {
    switchSort();
    if (pageable != null) {
      pageable.setCurrentPage(0);
    }
    refreshOnSort(target);
  }

  protected void switchSort() {
    Map<?, ?> activeSortBeforeSwitch = Maps.newLinkedHashMap(compositeSortModel.getActiveSort());
    SortOrder currentOrder = compositeSortModel.getSelectedOrder(sort);
    SortOrder nextOrder = cycleMode.getNext(sort, currentOrder);
    compositeSortModel.setOrder(sort, nextOrder);
    Map<?, ?> activeSortAfterSwitch = Maps.newLinkedHashMap(compositeSortModel.getActiveSort());
    if (activeSortBeforeSwitch.equals(activeSortAfterSwitch)) {
      /* In some cases, switching from "no order" to the default order will result in no change at all
       * (for instance if the composite sort model uses this sort as the single default sort)
       * In those cases, we try to trigger another switch so that the composite sort will change as expected
       */
      if (LOGGER.isWarnEnabled()) {
        activeSortBeforeSwitch = Maps.newLinkedHashMap(compositeSortModel.getActiveSort());
      }
      nextOrder = cycleMode.getNext(sort, nextOrder);
      compositeSortModel.setOrder(sort, nextOrder);
      if (LOGGER.isWarnEnabled()) {
        activeSortAfterSwitch = Maps.newLinkedHashMap(compositeSortModel.getActiveSort());
        if (activeSortBeforeSwitch.equals(activeSortAfterSwitch)) {
          LOGGER.warn(
              "A switch in a TableSortLink failed to trigger any change in the CompositeSortModel even after a second try.");
        }
      }
    }
  }

  public TableSortLink<T> cycleMode(CycleMode cycleMode) {
    this.cycleMode = cycleMode;
    return this;
  }

  public TableSortLink<T> iconStyle(ISortIconStyle iconStyle) {
    this.sortIconCssStyle = iconStyle;
    return this;
  }

  protected boolean isSortActive() {
    return compositeSortModel.getActiveOrder(sort) != null;
  }

  protected SortOrder getDisplayedSort() {
    final SortOrder currentOrder = compositeSortModel.getActiveOrder(sort);
    return sort.getDefaultOrder().asDefaultFor(currentOrder);
  }

  protected void refreshOnSort(AjaxRequestTarget target) {
    setResponsePage(getPage());
  }
}
