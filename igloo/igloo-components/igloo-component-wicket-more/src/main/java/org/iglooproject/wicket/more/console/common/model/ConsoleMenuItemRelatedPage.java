package org.iglooproject.wicket.more.console.common.model;

import java.io.Serializable;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.request.UrlUtils;

public class ConsoleMenuItemRelatedPage implements Serializable {

  private static final long serialVersionUID = -4624715918901057500L;

  private String urlFragment;

  private Class<? extends WebPage> pageClass;

  public ConsoleMenuItemRelatedPage(String urlFragment, Class<? extends WebPage> pageClass) {
    this.urlFragment = UrlUtils.normalizePath(urlFragment);
    this.pageClass = pageClass;
  }

  public String getUrlFragment() {
    return urlFragment;
  }

  public void setUrlFragment(String urlFragment) {
    this.urlFragment = urlFragment;
  }

  public Class<? extends WebPage> getPageClass() {
    return pageClass;
  }

  public void setPageClass(Class<? extends WebPage> pageClass) {
    this.pageClass = pageClass;
  }
}
