package basicapp.front.announcement.model;

import basicapp.back.business.announcement.model.Announcement;
import basicapp.back.business.announcement.search.AnnouncementSearchQueryData;
import basicapp.back.business.announcement.search.AnnouncementSort;
import basicapp.back.business.announcement.search.IAnnouncementSearchQuery;
import com.google.common.collect.ImmutableMap;
import java.util.function.UnaryOperator;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.iglooproject.wicket.more.markup.html.sort.model.CompositeSortModel;
import org.iglooproject.wicket.more.markup.html.sort.model.CompositeSortModel.CompositingStrategy;
import org.iglooproject.wicket.more.model.data.DataModel;
import org.iglooproject.wicket.more.model.search.query.SearchQueryDataProvider;

public class AnnouncementDataProvider
    extends SearchQueryDataProvider<
        Announcement, AnnouncementSort, AnnouncementSearchQueryData, IAnnouncementSearchQuery> {

  private static final long serialVersionUID = 1L;

  @SpringBean private IAnnouncementSearchQuery searchQuery;

  private final CompositeSortModel<AnnouncementSort> sortModel =
      new CompositeSortModel<>(
          CompositingStrategy.LAST_ONLY,
          ImmutableMap.of(
              AnnouncementSort.PUBLICATION_START_DATE_TIME,
                  AnnouncementSort.PUBLICATION_START_DATE_TIME.getDefaultOrder(),
              AnnouncementSort.ID, AnnouncementSort.ID.getDefaultOrder()),
          ImmutableMap.of(AnnouncementSort.ID, AnnouncementSort.ID.getDefaultOrder()));

  public AnnouncementDataProvider() {
    this(UnaryOperator.identity());
  }

  public AnnouncementDataProvider(
      UnaryOperator<DataModel<AnnouncementSearchQueryData>> dataModelOperator) {
    this(dataModelOperator.apply(new DataModel<>(AnnouncementSearchQueryData::new)));
  }

  public AnnouncementDataProvider(IModel<AnnouncementSearchQueryData> dataModel) {
    super(dataModel);
  }

  @Override
  public CompositeSortModel<AnnouncementSort> getSortModel() {
    return sortModel;
  }

  @Override
  protected IAnnouncementSearchQuery searchQuery() {
    return searchQuery;
  }
}
