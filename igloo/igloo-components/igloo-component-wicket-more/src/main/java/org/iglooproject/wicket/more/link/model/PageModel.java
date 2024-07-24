package org.iglooproject.wicket.more.link.model;

import org.apache.wicket.Page;
import org.apache.wicket.PageReference;
import org.apache.wicket.model.LoadableDetachableModel;

public class PageModel<P extends Page> extends LoadableDetachableModel<P> {

  private static final long serialVersionUID = -8810298985673150374L;

  private PageReference pageReference;

  public static <P extends Page> PageModel<P> of(P page) {
    return new PageModel<>(page);
  }

  public PageModel() {}

  public PageModel(P page) {
    super(page);
  }

  @SuppressWarnings("unchecked")
  @Override
  protected P load() {
    if (pageReference == null) {
      return null;
    } else {
      return (P) pageReference.getPage();
    }
  }

  @Override
  protected void onDetach() {
    if (isAttached()) {
      P page = getObject();
      pageReference = page == null ? null : page.getPageReference();
      super.onDetach();
    }
  }
}
