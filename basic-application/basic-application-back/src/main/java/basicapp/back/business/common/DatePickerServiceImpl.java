package basicapp.back.business.common;

import java.time.temporal.Temporal;
import java.util.List;
import java.util.Objects;
import org.springframework.stereotype.Service;

// TODO  RFO a supprimer après les tests
@Service
public class DatePickerServiceImpl implements IDatePickerService {
  @Override
  public void testNonNull(List<? extends Temporal> list) {
    list.forEach(Objects::requireNonNull);
  }
}
