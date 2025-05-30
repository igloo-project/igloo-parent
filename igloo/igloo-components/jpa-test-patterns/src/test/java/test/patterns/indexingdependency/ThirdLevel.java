package test.patterns.indexingdependency;

import jakarta.persistence.Basic;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Transient;
import java.time.Instant;
import java.util.List;
import org.hibernate.search.mapper.pojo.automaticindexing.ReindexOnUpdate;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.GenericField;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.Indexed;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.IndexingDependency;

@Entity
@Indexed
public class ThirdLevel {

  @Id public Long id;

  @OneToMany(fetch = FetchType.LAZY, mappedBy = "thirdLevel")
  public List<SecondLevel> secondLevel;

  @GenericField @Basic public String attribute;

  @Basic public String other;

  protected ThirdLevel() {}

  public ThirdLevel(Long id) {
    this();
    this.id = id;
  }

  @Transient
  @GenericField
  @IndexingDependency(reindexOnUpdate = ReindexOnUpdate.NO)
  public Instant getIndexedOn() {
    return Instant.now();
  }
}
