package basicapp.front.common.renderer;

import basicapp.back.util.time.DateTimePattern;
import igloo.wicket.renderer.Renderer;
import java.time.Instant;
import java.util.Locale;

public abstract class InstantRenderer extends Renderer<Instant> {

  private static final long serialVersionUID = 6997048072226585653L;

  private static final Renderer<Instant> INSTANCE =
      new InstantRenderer() {
        private static final long serialVersionUID = 1L;

        @Override
        public String render(Instant value, Locale locale) {
          return Renderer.fromDateTimePattern(DateTimePattern.DATETIME).render(value, locale);
        }
      }.nullsAsNull();

  public static Renderer<Instant> get() {
    return INSTANCE;
  }

  private InstantRenderer() {}
}
