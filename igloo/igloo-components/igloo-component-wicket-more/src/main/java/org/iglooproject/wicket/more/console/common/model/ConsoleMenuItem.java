package org.iglooproject.wicket.more.console.common.model;

import com.google.common.collect.Lists;
import java.io.Serializable;
import java.util.List;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.request.UrlUtils;
import org.bindgen.Bindable;

@Bindable
public class ConsoleMenuItem implements Serializable {

  private static final long serialVersionUID = -4624715918901057500L;

  private String name;

  private String displayStringKey;

  private String urlFragment;

  private Class<? extends WebPage> pageClass;

  private List<ConsoleMenuItemRelatedPage> relatedPages;

  public ConsoleMenuItem(
      String name,
      String displayStringKey,
      String urlFragment,
      Class<? extends WebPage> pageClass) {
    this.name = name;
    this.displayStringKey = displayStringKey;
    this.urlFragment = UrlUtils.normalizePath(urlFragment);
    this.pageClass = pageClass;
    this.relatedPages = Lists.newArrayList();
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getDisplayStringKey() {
    return displayStringKey;
  }

  public void setDisplayStringKey(String displayStringKey) {
    this.displayStringKey = displayStringKey;
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

  public List<ConsoleMenuItemRelatedPage> getRelatedPages() {
    return relatedPages;
  }

  public void setRelatedPages(List<ConsoleMenuItemRelatedPage> relatedPages) {
    this.relatedPages = relatedPages;
  }

  public void addRelatedPage(ConsoleMenuItemRelatedPage relatedPage) {
    relatedPages.add(relatedPage);
  }
}
