package basicapp.back.business.history.search;

import static org.iglooproject.jpa.more.util.jparepository.JpaRepositoryUtils.createPageRequest;

import basicapp.back.business.history.model.HistoryLog;
import basicapp.back.business.history.model.atomic.HistoryEventType;
import basicapp.back.business.history.repository.HistoryLogSpecifications;
import basicapp.back.business.history.repository.IHistoryLogRepository;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.EnumUtils;
import org.iglooproject.jpa.jparepository.SpecificationBuilder;
import org.iglooproject.jpa.more.business.sort.ISort;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
public class HistoryLogSearchQueryImpl implements IHistoryLogSearchQuery {

  private final IHistoryLogRepository historyLogRepository;

  @Autowired
  public HistoryLogSearchQueryImpl(IHistoryLogRepository historyLogRepository) {
    this.historyLogRepository = historyLogRepository;
  }

  @Override
  public Collection<HistoryLog> list(
      HistoryLogSearchQueryData data,
      Map<HistoryLogSort, ISort.SortOrder> sorts,
      Integer offset,
      Integer limit) {
    if (!checkLimit(limit)) {
      return List.of();
    }

    return historyLogRepository
        .findAll(predicateContributor(data), createPageRequest(sorts, offset, limit))
        .getContent();
  }

  @Override
  public long size(HistoryLogSearchQueryData data) {
    return historyLogRepository.count(predicateContributor(data));
  }

  private Specification<HistoryLog> predicateContributor(HistoryLogSearchQueryData data) {
    SpecificationBuilder<HistoryLog> builder = new SpecificationBuilder<>();
    if (data.getDateMin() != null || data.getDateMax() != null) {
      builder.and(HistoryLogSpecifications.date(data.getDateMin(), data.getDateMax()));
    }
    if (CollectionUtils.isNotEmpty(data.getEventTypes())) {
      builder.and(HistoryLogSpecifications.eventType(data.getEventTypes()));
    }
    if (data.getSubject() != null) {
      builder.and(HistoryLogSpecifications.subject(data.getSubject()));
    }
    if (data.getAllObjects() != null) {
      builder.and(
          HistoryLogSpecifications.mainObject(data.getAllObjects())
              .or(HistoryLogSpecifications.object1(data.getAllObjects()))
              .or(HistoryLogSpecifications.object2(data.getAllObjects()))
              .or(HistoryLogSpecifications.object3(data.getAllObjects()))
              .or(HistoryLogSpecifications.object4(data.getAllObjects())));
    }
    if (data.getMainObject() != null) {
      builder.and(HistoryLogSpecifications.mainObject(data.getAllObjects()));
    }
    if (data.getObject1() != null) {
      builder.and(HistoryLogSpecifications.object1(data.getAllObjects()));
    }
    if (data.getObject2() != null) {
      builder.and(HistoryLogSpecifications.object2(data.getAllObjects()));
    }
    if (data.getObject3() != null) {
      builder.and(HistoryLogSpecifications.object3(data.getAllObjects()));
    }
    if (data.getObject4() != null) {
      builder.and(HistoryLogSpecifications.object4(data.getAllObjects()));
    }
    if (CollectionUtils.isNotEmpty(data.getMandatoryDifferencesEventTypes())) {
      Collection<HistoryEventType> allowedWithoutDifferencesEventTypes =
          EnumUtils.getEnumList(HistoryEventType.class);
      allowedWithoutDifferencesEventTypes.removeAll(data.getMandatoryDifferencesEventTypes());
      builder.and(
          HistoryLogSpecifications.eventType(allowedWithoutDifferencesEventTypes)
              .or(HistoryLogSpecifications.hasHistoryDifference()));
    }
    return builder.build();
  }
}
