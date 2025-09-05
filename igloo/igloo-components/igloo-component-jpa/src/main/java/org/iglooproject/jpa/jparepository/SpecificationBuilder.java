package org.iglooproject.jpa.jparepository;

import org.springframework.data.jpa.domain.Specification;

/**
 * {@link Specification#and} and {@link Specification#or} methods does not modify the original
 * specification. The method instead creates a new specification with the new condition.
 *
 * <p>This builder is used to simplify the creation of specifications with conditions
 *
 * @param <T> the type of the Root the resulting Specification operates on
 */
public class SpecificationBuilder<T> {
  Specification<T> spec;

  public SpecificationBuilder() {
    this.spec = (root, query, builder) -> null;
  }

  public SpecificationBuilder(Specification<T> spec) {
    this.spec = spec;
  }

  public SpecificationBuilder<T> and(Specification<T> spec) {
    this.spec = this.spec.and(spec);
    return this;
  }

  public SpecificationBuilder<T> or(Specification<T> spec) {
    this.spec = this.spec.or(spec);
    return this;
  }

  public Specification<T> build() {
    return this.spec;
  }
}
