package basicapp.back.business.announcement.search;

import static org.iglooproject.jpa.more.util.jparepository.JpaRepositoryUtils.createPageRequest;

import basicapp.back.business.announcement.model.Announcement;
import basicapp.back.business.announcement.repository.IAnnouncementRepository;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import org.iglooproject.jpa.jparepository.SpecificationBuilder;
import org.iglooproject.jpa.more.business.sort.ISort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
public class AnnouncementSearchQueryImpl implements IAnnouncementSearchQuery {

  private final IAnnouncementRepository announcementRepository;

  @Autowired
  public AnnouncementSearchQueryImpl(IAnnouncementRepository announcementRepository) {
    this.announcementRepository = announcementRepository;
  }

  @Override
  public Collection<Announcement> list(
      AnnouncementSearchQueryData data,
      Map<AnnouncementSort, SortOrder> sorts,
      Integer offset,
      Integer limit) {
    if (!checkLimit(limit)) {
      return List.of();
    }

    return announcementRepository
        .findAll(predicateContributor(data), createPageRequest(sorts, offset, limit))
        .getContent();
  }

  @Override
  public long size(AnnouncementSearchQueryData data) {
    return announcementRepository.count(predicateContributor(data));
  }

  private Specification<Announcement> predicateContributor(
      AnnouncementSearchQueryData data) { // NOSONAR
    return new SpecificationBuilder<Announcement>().build();
  }
}
