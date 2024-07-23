package igloo.wicket.markup.html.form;

import org.apache.wicket.markup.html.navigation.paging.IPageable;
import org.apache.wicket.model.IModel;
import org.apache.wicket.util.lang.Args;

public class PageableSearchForm<T> extends org.apache.wicket.markup.html.form.Form<T> {

  private static final long serialVersionUID = 4136547733729224074L;

  private final IPageable pageable;

  public PageableSearchForm(String id, IPageable pageable) {
    super(id);
    Args.notNull(pageable, "pageable");
    this.pageable = pageable;
  }

  public PageableSearchForm(String id, IModel<T> model, IPageable pageable) {
    super(id, model);
    Args.notNull(pageable, "pageable");
    this.pageable = pageable;
  }

  @Override
  protected void onSubmit() {
    // when we submit the search form, we go back to the first page
    if (pageable != null) {
      pageable.setCurrentPage(0);
    }
    super.onSubmit();
  }
}
