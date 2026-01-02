package basicapp.front.announcement.model;

import basicapp.back.business.announcement.model.Announcement;
import basicapp.back.business.announcement.model.atomic.AnnouncementType;
import basicapp.back.util.binding.Bindings;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.iglooproject.wicket.more.bindable.model.BindableModel;
import org.iglooproject.wicket.more.model.GenericEntityModel;

public class AnnouncementBindableModel extends BindableModel<Announcement> {

  private static final long serialVersionUID = 1L;

  public AnnouncementBindableModel() {
    this(new GenericEntityModel<>(new Announcement()));
  }

  public AnnouncementBindableModel(IModel<Announcement> mainModel) {
    super(mainModel);

    bindWithCache(Bindings.announcement().type(), new Model<>());
    bindWithCache(Bindings.announcement().interruption().startDateTime(), new Model<>());
    bindWithCache(Bindings.announcement().interruption().endDateTime(), new Model<>());
    bindWithCache(Bindings.announcement().title(), new Model<>());
    bindWithCache(Bindings.announcement().description(), new Model<>());
    bindWithCache(Bindings.announcement().publication().startDateTime(), new Model<>());
    bindWithCache(Bindings.announcement().publication().endDateTime(), new Model<>());
    bindWithCache(Bindings.announcement().enabled(), new Model<>());
  }

  @Override
  public void setObject(Announcement announcement) {
    if (announcement == null) {
      announcement = new Announcement();
    }

    if (announcement.getType() == null) {
      announcement.setType(AnnouncementType.SERVICE_INTERRUPTION);
    }

    super.setObject(announcement);
  }
}
