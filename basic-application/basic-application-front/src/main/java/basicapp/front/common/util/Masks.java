package basicapp.front.common.util;

import org.apache.wicket.model.ResourceModel;
import org.iglooproject.wicket.more.markup.html.template.js.jquery.plugins.mask.MaskOptions;

public class Masks {

  public static final String DATE = "09r09r9999";

  public static MaskOptions dateOptions() {
    return new MaskOptions()
        .translation('r', "/[\\/]/", '/')
        .placeholder(new ResourceModel("datetime.pattern.shortDate.placeholder"));
  }

  public static final String TIME = "00:00";

  public static MaskOptions timeOptions() {
    return new MaskOptions().placeholder(new ResourceModel("datetime.pattern.time.placeholder"));
  }

  private Masks() {}
}
