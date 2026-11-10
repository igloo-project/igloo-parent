package org.iglooproject.wicket.more.markup.repeater.table.builder.action;

import igloo.bootstrap.renderer.IBootstrapRenderer;
import igloo.wicket.component.PlaceholderContainer;
import igloo.wicket.factory.IOneParameterComponentFactory;
import java.util.Objects;
import org.apache.wicket.Component;
import org.apache.wicket.MarkupContainer;
import org.apache.wicket.Page;
import org.apache.wicket.model.IModel;
import org.iglooproject.functional.SerializableFunction2;
import org.iglooproject.wicket.more.link.dto.base.IPageLinkGenerator;
import org.iglooproject.wicket.more.link.dto.component.PageLinkBookmarkablePageLink;
import org.iglooproject.wicket.more.markup.repeater.table.column.CoreActionColumnLinkPanel;

/**
 * Action column link built with a {@link IPageLinkGenerator} (link DTO), the generator being
 * created from the row model by a function.
 *
 * @see ActionColumnLinkBuilder
 */
public class ActionColumnPageLinkDtoBuilder<T>
    extends AbstractActionColumnElementBuilder<
        T, PageLinkBookmarkablePageLink<? extends Page>, ActionColumnPageLinkDtoBuilder<T>> {

  private static final long serialVersionUID = 1L;

  private boolean hideIfInvalid = false;

  public ActionColumnPageLinkDtoBuilder(
      IBootstrapRenderer<? super T> renderer,
      SerializableFunction2<? super IModel<? extends T>, IPageLinkGenerator<? extends Page>>
          pageLinkGeneratorFunction) {
    super(renderer, factory(pageLinkGeneratorFunction));
  }

  private static <T>
      IOneParameterComponentFactory<PageLinkBookmarkablePageLink<? extends Page>, IModel<T>>
          factory(
              SerializableFunction2<? super IModel<? extends T>, IPageLinkGenerator<? extends Page>>
                  pageLinkGeneratorFunction) {
    Objects.requireNonNull(pageLinkGeneratorFunction);
    return (wicketId, rowModel) -> pageLinkGeneratorFunction.apply(rowModel).link(wicketId);
  }

  @Override
  public final MarkupContainer create(String wicketId, IModel<T> rowModel) {
    return new CoreActionColumnLinkPanel<T>(wicketId, rowModel) {
      private static final long serialVersionUID = 1L;

      @Override
      protected Component getLink(String wicketId, IModel<T> rowModel) {
        PageLinkBookmarkablePageLink<? extends Page> link =
            getFactory().create("link", rowModel);
        decorateLink(link, rowModel);
        return link;
      }

      @Override
      protected PlaceholderContainer getPlaceholder(String wicketId, IModel<T> rowModel) {
        PlaceholderContainer placeholder = new PlaceholderContainer("linkPlaceholder");
        decoratePlaceholder(placeholder, rowModel);
        return placeholder;
      }
    };
  }

  @Override
  protected void decorateLink(
      PageLinkBookmarkablePageLink<? extends Page> link, IModel<T> rowModel) {
    super.decorateLink(link, rowModel);
    if (hideIfInvalid) {
      link.hideIfInvalid();
    }
  }

  @Override
  public void detach() {
    // nothing to do
  }

  public ActionColumnPageLinkDtoBuilder<T> hideIfInvalid() {
    this.hideIfInvalid = true;
    return thisAsF();
  }
}
