package org.iglooproject.basicapp.web.application.administration.model;

import igloo.wicket.model.Detachables;
import java.util.Date;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.iglooproject.basicapp.core.business.announcement.model.Announcement;
import org.iglooproject.basicapp.core.business.announcement.model.atomic.AnnouncementType;
import org.iglooproject.basicapp.core.util.DateUtils2;
import org.iglooproject.basicapp.core.util.binding.Bindings;
import org.iglooproject.commons.util.CloneUtils;
import org.iglooproject.wicket.more.bindable.model.BindableModel;
import org.iglooproject.wicket.more.model.GenericEntityModel;

public class AnnouncementBindableModel extends BindableModel<Announcement> {

  private static final long serialVersionUID = -666421439042205567L;

  private final IModel<Date> interruptionStartTimeModel = Model.of();
  private final IModel<Date> interruptionEndTimeModel = Model.of();
  private final IModel<Date> publicationStartTimeModel = Model.of();
  private final IModel<Date> publicationEndTimeModel = Model.of();

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

    announcement
        .getPublication()
        .setStartDateTime(
            DateUtils2.addTime(
                announcement.getPublication().getStartDateTime(),
                publicationStartTimeModel.getObject()));
    announcement
        .getPublication()
        .setEndDateTime(
            DateUtils2.addTime(
                announcement.getPublication().getEndDateTime(),
                publicationEndTimeModel.getObject()));
    announcement
        .getInterruption()
        .setStartDateTime(
            DateUtils2.addTime(
                announcement.getInterruption().getStartDateTime(),
                interruptionStartTimeModel.getObject()));
    announcement
        .getInterruption()
        .setEndDateTime(
            DateUtils2.addTime(
                announcement.getInterruption().getEndDateTime(),
                interruptionEndTimeModel.getObject()));
  }

  @Override
  protected void onReadAll() {
    super.onReadAll();

    Announcement announcement = getObject();

    publicationStartTimeModel.setObject(
        CloneUtils.clone(announcement.getPublication().getStartDateTime()));
    publicationEndTimeModel.setObject(
        CloneUtils.clone(announcement.getPublication().getEndDateTime()));
    interruptionStartTimeModel.setObject(
        CloneUtils.clone(announcement.getInterruption().getStartDateTime()));
    interruptionEndTimeModel.setObject(
        CloneUtils.clone(announcement.getInterruption().getEndDateTime()));
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

  public IModel<Date> getInterruptionStartTimeModel() {
    return interruptionStartTimeModel;
  }

  public IModel<Date> getInterruptionEndTimeModel() {
    return interruptionEndTimeModel;
  }

  public IModel<Date> getPublicationStartTimeModel() {
    return publicationStartTimeModel;
  }

  public IModel<Date> getPublicationEndTimeModel() {
    return publicationEndTimeModel;
  }

  @Override
  protected void onDetach() {
    super.onDetach();
    Detachables.detach(
        publicationEndTimeModel,
        publicationStartTimeModel,
        interruptionStartTimeModel,
        interruptionEndTimeModel);
  }
}
