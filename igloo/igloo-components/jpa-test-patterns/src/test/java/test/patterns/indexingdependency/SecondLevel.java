package test.patterns.indexingdependency;

import jakarta.persistence.Basic;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import java.util.List;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.GenericField;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.IndexedEmbedded;

@Entity
public class SecondLevel {

  @Id public Long id;

  @OneToMany(fetch = FetchType.LAZY, mappedBy = "secondLevel")
  public List<FirstLevel> firstLevel;

  @IndexedEmbedded(includePaths = "attribute")
  @ManyToOne
  public ThirdLevel thirdLevel;

  @GenericField @Basic public String attribute;

  protected SecondLevel() {}

  public SecondLevel(Long id) {
    this();
    this.id = id;
  }
}
