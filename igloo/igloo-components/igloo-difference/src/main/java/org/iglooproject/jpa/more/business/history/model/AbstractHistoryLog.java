package org.iglooproject.jpa.more.business.history.model;

import com.google.common.base.MoreObjects.ToStringHelper;
import com.google.common.collect.Lists;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import jakarta.persistence.Basic;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderColumn;
import jakarta.persistence.Transient;
import java.time.Instant;
import java.util.Collections;
import java.util.List;
import org.bindgen.Bindable;
import org.hibernate.Length;
import org.iglooproject.commons.util.collections.CollectionUtils;
import org.iglooproject.commons.util.fieldpath.FieldPath;
import org.iglooproject.jpa.more.business.history.model.embeddable.HistoryValue;

@Bindable
@SuppressFBWarnings("squid:S00107")
@MappedSuperclass
public abstract class AbstractHistoryLog<
        HL extends AbstractHistoryLog<HL, HET, HD>,
        HET extends Enum<HET>,
        HD extends AbstractHistoryDifference<HD, HL>>
    extends AbstractHistoryElement<HL, HL> {

  private static final long serialVersionUID = -1146280203615151992L;

  @Id @GeneratedValue private Long id;

  @Basic(optional = false)
  private Instant date;

  @Basic(optional = false)
  @Enumerated(EnumType.STRING)
  private HET eventType;

  @Embedded private HistoryValue subject;

  @Embedded private HistoryValue mainObject;

  @Embedded private HistoryValue object1 = new HistoryValue();

  @Embedded private HistoryValue object2 = new HistoryValue();

  @Embedded private HistoryValue object3 = new HistoryValue();

  @Embedded private HistoryValue object4 = new HistoryValue();

  @Basic
  @Column(length = Length.LONG32)
  private String comment;

  @OneToMany(
      mappedBy = "parentLog",
      cascade = CascadeType.ALL,
      orphanRemoval = true,
      fetch = FetchType.EAGER)
  @OrderColumn
  private List<HD> differences = Lists.newArrayList();

  protected AbstractHistoryLog() {
    // nothing to do
  }

  protected AbstractHistoryLog(Instant date, HET eventType, HistoryValue mainObject) {
    this.date = date;
    this.eventType = eventType;
    this.mainObject = mainObject;
  }

  @Override
  public Long getId() {
    return id;
  }

  @Override
  public void setId(Long id) {
    this.id = id;
  }

  @Override
  @SuppressWarnings("unchecked")
  @Transient
  public HL getRootLog() {
    return (HL) this;
  }

  @Override
  @Transient
  protected HL getParent() {
    return null;
  }

  @Override
  @Transient
  public FieldPath getRelativePath() {
    return FieldPath.ROOT;
  }

  public Instant getDate() {
    return date;
  }

  public void setDate(Instant date) {
    this.date = date;
  }

  public HET getEventType() {
    return eventType;
  }

  public void setEventType(HET action) {
    this.eventType = action;
  }

  public HistoryValue getSubject() {
    return subject;
  }

  public void setSubject(HistoryValue subject) {
    this.subject = subject;
  }

  public HistoryValue getMainObject() {
    return mainObject;
  }

  public void setMainObject(HistoryValue mainObject) {
    this.mainObject = mainObject;
  }

  public HistoryValue getObject1() {
    return object1;
  }

  public void setObject1(HistoryValue object1) {
    this.object1 = object1;
  }

  public HistoryValue getObject2() {
    return object2;
  }

  public void setObject2(HistoryValue object2) {
    this.object2 = object2;
  }

  public HistoryValue getObject3() {
    return object3;
  }

  public void setObject3(HistoryValue object3) {
    this.object3 = object3;
  }

  public HistoryValue getObject4() {
    return object4;
  }

  public void setObject4(HistoryValue object4) {
    this.object4 = object4;
  }

  public String getComment() {
    return comment;
  }

  public void setComment(String comment) {
    this.comment = comment;
  }

  public List<HD> getDifferences() {
    return Collections.unmodifiableList(differences);
  }

  public void setDifferences(List<HD> differences) {
    CollectionUtils.replaceAll(this.differences, differences);
  }

  @Override
  protected ToStringHelper toStringHelper() {
    return super.toStringHelper().add("object1", object1.getLabel());
  }
}
