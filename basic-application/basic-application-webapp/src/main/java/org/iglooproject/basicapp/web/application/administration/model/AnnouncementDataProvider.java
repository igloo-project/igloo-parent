package org.iglooproject.basicapp.web.application.administration.model;

import com.google.common.collect.ImmutableMap;
import igloo.wicket.model.Detachables;
import org.apache.wicket.injection.Injector;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.iglooproject.basicapp.core.business.announcement.model.Announcement;
import org.iglooproject.basicapp.core.business.announcement.search.AnnouncementSort;
import org.iglooproject.basicapp.core.business.announcement.search.IAnnouncementSearchQuery;
import org.iglooproject.basicapp.core.business.announcement.service.IAnnouncementService;
import org.iglooproject.jpa.more.business.search.query.ISearchQuery;
import org.iglooproject.wicket.more.markup.html.sort.model.CompositeSortModel;
import org.iglooproject.wicket.more.markup.html.sort.model.CompositeSortModel.CompositingStrategy;
import org.iglooproject.wicket.more.model.AbstractSearchQueryDataProvider;
import org.iglooproject.wicket.more.model.GenericEntityModel;

public class AnnouncementDataProvider
    extends AbstractSearchQueryDataProvider<Announcement, AnnouncementSort> {

  private static final long serialVersionUID = 2391048007215147522L;

  @SpringBean private IAnnouncementService announcementService;

  private final CompositeSortModel<AnnouncementSort> sortModel =
      new CompositeSortModel<>(
          CompositingStrategy.LAST_ONLY,
          ImmutableMap.of(
              AnnouncementSort.PUBLICATION_START_DATE_TIME,
                  AnnouncementSort.PUBLICATION_START_DATE_TIME.getDefaultOrder(),
              AnnouncementSort.ID, AnnouncementSort.ID.getDefaultOrder()),
          ImmutableMap.of(AnnouncementSort.ID, AnnouncementSort.ID.getDefaultOrder()));

  public AnnouncementDataProvider() {
    Injector.get().inject(this);
  }

  @Override
  public IModel<Announcement> model(Announcement object) {
    return GenericEntityModel.of(object);
  }

  public CompositeSortModel<AnnouncementSort> getSortModel() {
    return sortModel;
  }

  @Override
  protected ISearchQuery<Announcement, AnnouncementSort> getSearchQuery() {
    return createSearchQuery(IAnnouncementSearchQuery.class).sort(sortModel.getObject());
  }

  @Override
  public void detach() {
    super.detach();
    Detachables.detach(sortModel);
  }
}
