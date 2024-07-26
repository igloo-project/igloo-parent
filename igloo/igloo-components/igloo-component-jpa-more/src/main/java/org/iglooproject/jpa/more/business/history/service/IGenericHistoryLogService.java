package org.iglooproject.jpa.more.business.history.service;

import java.util.Date;
import java.util.List;
import org.iglooproject.jpa.business.generic.service.IGenericEntityService;
import org.iglooproject.jpa.exception.SecurityServiceException;
import org.iglooproject.jpa.exception.ServiceException;
import org.iglooproject.jpa.more.business.difference.service.IDifferenceService;
import org.iglooproject.jpa.more.business.difference.util.IDifferenceFromReferenceGenerator;
import org.iglooproject.jpa.more.business.difference.util.IHistoryDifferenceGenerator;
import org.iglooproject.jpa.more.business.history.model.AbstractHistoryDifference;
import org.iglooproject.jpa.more.business.history.model.AbstractHistoryLog;
import org.iglooproject.jpa.more.business.history.model.bean.AbstractHistoryLogAdditionalInformationBean;
import org.iglooproject.jpa.more.business.history.util.IHistoryDifferenceHandler;

public interface IGenericHistoryLogService<
        HL extends AbstractHistoryLog<HL, HET, HD>,
        HET extends Enum<HET>,
        HD extends AbstractHistoryDifference<HD, HL>,
        HLAIB extends AbstractHistoryLogAdditionalInformationBean>
    extends IGenericEntityService<Long, HL> {

  <T> void log(HET eventType, T mainObject, HLAIB additionalInformation)
      throws ServiceException, SecurityServiceException;

  <T> void logWithDifferences(
      HET eventType,
      T mainObject,
      HLAIB additionalInformation,
      IDifferenceService<T> differenceService)
      throws ServiceException, SecurityServiceException;

  @SuppressWarnings("unchecked")
  <T> void logWithDifferences(
      HET eventType,
      T mainObject,
      HLAIB additionalInformation,
      IDifferenceService<T> differenceService,
      IHistoryDifferenceHandler<? super T, ? super HL>... differenceHandlers)
      throws ServiceException, SecurityServiceException;

  /*
   * Using interfaces more generic than IDifferenceService here allows the caller to use
   * objects that compute differences in a customizable way (for instance in order to occasionally
   * ignore some fields).
   */
  @SuppressWarnings("unchecked")
  <T> void logWithDifferences(
      HET eventType,
      T mainObject,
      HLAIB additionalInformation,
      IDifferenceFromReferenceGenerator<T> differenceGenerator,
      IHistoryDifferenceGenerator<T> historyDifferenceGenerator,
      IHistoryDifferenceHandler<? super T, ? super HL>... differenceHandlers)
      throws ServiceException, SecurityServiceException;

  <T> HL logNow(
      Date date, HET eventType, List<HD> differences, T mainObject, HLAIB additionalInformation)
      throws ServiceException, SecurityServiceException;
}
