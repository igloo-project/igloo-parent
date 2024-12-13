package basicapp.back.business.common;

// TODO  RFO a supprimer après les tests

import java.time.temporal.Temporal;
import java.util.List;

public interface IDatePickerService {
  void testNonNull(List<? extends Temporal> list);
}
