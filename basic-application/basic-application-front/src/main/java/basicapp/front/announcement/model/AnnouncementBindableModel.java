package basicapp.front.announcement.model;

import basicapp.back.business.announcement.model.Announcement;
import basicapp.back.business.announcement.model.atomic.AnnouncementType;
import basicapp.back.util.binding.Bindings;
import igloo.wicket.model.Detachables;
import java.time.LocalDate;
import java.time.LocalTime;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.iglooproject.wicket.more.bindable.model.BindableModel;
import org.iglooproject.wicket.more.model.GenericEntityModel;

public class AnnouncementBindableModel extends BindableModel<Announcement> {

  private static final long serialVersionUID = -666421439042205567L;

  private final IModel<LocalDate> interruptionStartLocalDateModel = Model.of();
  private final IModel<LocalTime> interruptionStartLocalTimeModel = Model.of();
  private final IModel<LocalDate> interruptionEndLocalDateModel = Model.of();
  private final IModel<LocalTime> interruptionEndLocalTimeModel = Model.of();
  private final IModel<LocalDate> publicationStartLocalDateModel = Model.of();
  private final IModel<LocalTime> publicationStartLocalTimeModel = Model.of();
  private final IModel<LocalDate> publicationEndLocalDateModel = Model.of();
  private final IModel<LocalTime> publicationEndLocalTimeModel = Model.of();

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
  protected void onWriteAll() {
    super.onWriteAll();

    Announcement announcement = getObject();

    if (publicationStartLocalDateModel.getObject() != null
        && publicationStartLocalTimeModel.getObject() != null) {
      announcement
          .getPublication()
          .setStartDateTime(
              publicationStartLocalDateModel
                  .getObject()
                  .atTime(publicationStartLocalTimeModel.getObject()));
    } else {
      announcement.getPublication().setStartDateTime(null);
    }

    if (publicationEndLocalDateModel.getObject() != null
        && publicationEndLocalTimeModel.getObject() != null) {
      announcement
          .getPublication()
          .setEndDateTime(
              publicationEndLocalDateModel
                  .getObject()
                  .atTime(publicationEndLocalTimeModel.getObject()));
    } else {
      announcement.getPublication().setEndDateTime(null);
    }

    if (interruptionStartLocalDateModel.getObject() != null
        && interruptionStartLocalTimeModel.getObject() != null) {
      announcement
          .getInterruption()
          .setStartDateTime(
              interruptionStartLocalDateModel
                  .getObject()
                  .atTime(interruptionStartLocalTimeModel.getObject()));
    } else {
      announcement.getInterruption().setStartDateTime(null);
    }

    if (interruptionEndLocalDateModel.getObject() != null
        && interruptionEndLocalTimeModel.getObject() != null) {
      announcement
          .getInterruption()
          .setEndDateTime(
              interruptionEndLocalDateModel
                  .getObject()
                  .atTime(interruptionEndLocalTimeModel.getObject()));
    } else {
      announcement.getInterruption().setEndDateTime(null);
    }
  }

  @Override
  protected void onReadAll() {
    super.onReadAll();

    Announcement announcement = getObject();

    publicationStartLocalDateModel.setObject(
        Bindings.announcement().publication().startDateTime().toLocalDate().apply(announcement));
    publicationStartLocalTimeModel.setObject(
        Bindings.announcement().publication().startDateTime().toLocalTime().apply(announcement));
    publicationEndLocalDateModel.setObject(
        Bindings.announcement().publication().endDateTime().toLocalDate().apply(announcement));
    publicationEndLocalTimeModel.setObject(
        Bindings.announcement().publication().endDateTime().toLocalTime().apply(announcement));
    interruptionStartLocalDateModel.setObject(
        Bindings.announcement().interruption().startDateTime().toLocalDate().apply(announcement));
    interruptionStartLocalTimeModel.setObject(
        Bindings.announcement().interruption().startDateTime().toLocalTime().apply(announcement));
    interruptionEndLocalDateModel.setObject(
        Bindings.announcement().interruption().endDateTime().toLocalDate().apply(announcement));
    interruptionEndLocalTimeModel.setObject(
        Bindings.announcement().interruption().endDateTime().toLocalTime().apply(announcement));
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

  public IModel<LocalDate> getInterruptionStartLocalDateModel() {
    return interruptionStartLocalDateModel;
  }

  public IModel<LocalTime> getInterruptionStartLocalTimeModel() {
    return interruptionStartLocalTimeModel;
  }

  public IModel<LocalDate> getInterruptionEndLocalDateModel() {
    return interruptionEndLocalDateModel;
  }

  public IModel<LocalTime> getInterruptionEndLocalTimeModel() {
    return interruptionEndLocalTimeModel;
  }

  public IModel<LocalDate> getPublicationStartLocalDateModel() {
    return publicationStartLocalDateModel;
  }

  public IModel<LocalTime> getPublicationStartLocalTimeModel() {
    return publicationStartLocalTimeModel;
  }

  public IModel<LocalDate> getPublicationEndLocalDateModel() {
    return publicationEndLocalDateModel;
  }

  public IModel<LocalTime> getPublicationEndLocalTimeModel() {
    return publicationEndLocalTimeModel;
  }

  @Override
  protected void onDetach() {
    super.onDetach();
    Detachables.detach(
        publicationStartLocalDateModel,
        publicationStartLocalTimeModel,
        publicationEndLocalDateModel,
        publicationEndLocalTimeModel,
        interruptionStartLocalDateModel,
        interruptionStartLocalTimeModel,
        interruptionEndLocalDateModel,
        interruptionEndLocalTimeModel);
  }
}
