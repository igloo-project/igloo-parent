package org.iglooproject.wicket.more.markup.repeater;

import igloo.wicket.factory.IComponentFactory;
import igloo.wicket.factory.IOneParameterComponentFactory;
import java.util.Iterator;
import org.apache.wicket.Component;
import org.apache.wicket.DequeueContext;
import org.apache.wicket.DequeueContext.Bookmark;
import org.apache.wicket.MarkupContainer;
import org.apache.wicket.markup.IMarkupFragment;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.repeater.AbstractRepeater;
import org.apache.wicket.markup.repeater.RepeatingView;

public class FactoryRepeatingView extends RepeatingView implements IRefreshableOnDemandRepeater {

  private static final long serialVersionUID = -5285541426332338090L;

  public FactoryRepeatingView(String id) {
    super(id);
  }

  @Override
  protected void onInitialize() {
    super.onInitialize();
    getParent().setOutputMarkupId(true);
  }

  @Override
  public void refreshItems() {
    // Nothing to do : items have already been added or removed.
  }

  /**
   * Wraps the components before adding them to the view, so the uniqueness of their ID is no longer
   * relevant.
   *
   * <p>This allows to add components to this view without worrying at all about what their ID
   * should be.
   */
  public void addWrapped(Component... components) {
    for (Component componentToWrap : components) {
      Wrapper wrapper = new Wrapper(newChildId());
      wrapper.add(componentToWrap);
      add(wrapper);
    }
  }

  private static final class Wrapper extends WebMarkupContainer {
    private static final long serialVersionUID = 1L;

    public Wrapper(String id) {
      super(id);
    }

    @Override
    public MarkupContainer remove(Component componentToRemove) {
      MarkupContainer result = super.remove(componentToRemove);
      // Remove the wrapper if the child is removed
      if (size() == 0) {
        remove();
      }
      return result;
    }

    /**
     * @see AbstractRepeater#dequeue()
     */
    @Override
    protected final void onRender() {
      Iterator<? extends Component> it = iterator();
      while (it.hasNext()) {
        Component child = it.next();
        if (child == null) {
          throw new IllegalStateException(
              "The render iterator returned null for a child. Container: "
                  + this.toString()
                  + "; Iterator="
                  + it.toString());
        }
        child.render();
      }
    }

    /**
     * @see AbstractRepeater#dequeue()
     */
    @Override
    public IMarkupFragment getMarkup(final Component child) {
      return getMarkup();
    }

    /**
     * @see AbstractRepeater#dequeue()
     */
    @Override
    public void dequeue(DequeueContext dequeue) {
      if (size() > 0) {
        Bookmark bookmark = dequeue.save();
        for (Component child : this) {
          if (child instanceof MarkupContainer) {
            dequeue.popContainer(); // pop the repeater
            MarkupContainer container = (MarkupContainer) child;
            dequeue.pushContainer(container);
            container.dequeue(dequeue);
            dequeue.restore(bookmark);
          }
        }
      }

      dequeue.skipToCloseTag();
    }
  }

  public <T extends Component> T add(IComponentFactory<T> componentFactory) {
    T component = componentFactory.create(newChildId());
    add(component);
    return component;
  }

  public <T extends Component, P> T add(
      IOneParameterComponentFactory<T, P> componentFactory, P parameter) {
    T component = componentFactory.create(newChildId(), parameter);
    add(component);
    return component;
  }

  public void addAll(Iterable<? extends IComponentFactory<?>> factories) {
    for (IComponentFactory<?> componentFactory : factories) {
      add(componentFactory);
    }
  }

  public <P> void addAll(
      Iterable<? extends IOneParameterComponentFactory<?, ? super P>> factories, P parameter) {
    for (IOneParameterComponentFactory<?, ? super P> componentFactory : factories) {
      add(componentFactory, parameter);
    }
  }
}
