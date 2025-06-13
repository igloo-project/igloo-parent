package test.patterns.indexingdependency;

import jakarta.persistence.Basic;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Transient;
import java.time.Instant;
import org.hibernate.search.mapper.pojo.automaticindexing.ReindexOnUpdate;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.AssociationInverseSide;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.GenericField;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.Indexed;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.IndexedEmbedded;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.IndexingDependency;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.ObjectPath;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.PropertyValue;

@Entity
@Indexed
public class FirstLevel {

  @Id public Long id;

  @IndexedEmbedded(includePaths = "thirdLevel.attribute")
  @ManyToOne
  public SecondLevel secondLevel;

  @GenericField @Basic public String attribute;

  @Basic String other;

  protected FirstLevel() {}

  public FirstLevel(Long id) {
    this();
    this.id = id;
  }

  @Transient
  @GenericField
  @IndexingDependency(reindexOnUpdate = ReindexOnUpdate.NO)
  public Instant getIndexedOn() {
    return Instant.now();
  }

  @Transient
  @IndexingDependency(
      derivedFrom =
          @ObjectPath({
            @PropertyValue(propertyName = "secondLevel"),
            @PropertyValue(propertyName = "thirdLevel"),
          }))
  @AssociationInverseSide(
      inversePath =
          @ObjectPath({
            @PropertyValue(propertyName = "secondLevel"),
            @PropertyValue(propertyName = "firstLevel")
          }))
  @IndexedEmbedded(includePaths = {"attribute"})
  public ThirdLevel getThirdLevel() {
    return secondLevel.thirdLevel;
  }
}
