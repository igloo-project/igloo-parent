package org.iglooproject.test.business.nullables.model;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.iglooproject.jpa.business.generic.model.GenericEntity;
import org.iglooproject.test.business.nullables.model.embeddable.NullablesSection;

@Entity
public class EntityNullable extends GenericEntity<Long, EntityNullable> {

	private static final long serialVersionUID = -6305584811510441696L;

	@Id
	@GeneratedValue
	private Long id;

	@Embedded
	private NullablesSection simpleSection = new NullablesSection();

	@Embedded
	@AttributeOverride(name = "basicFalseColumnFalse", column = @Column(nullable = true))
	@AttributeOverride(name = "basicFalseColumnTrue", column = @Column(nullable = false))
	@AttributeOverride(name = "basicTrueColumnFalse", column = @Column(nullable = true))
	@AttributeOverride(name = "basicTrueColumnTrue", column = @Column(nullable = false))
	private NullablesSection overridesSection = new NullablesSection();

	@Override
	public Long getId() {
		return id;
	}

	@Override
	public void setId(Long id) {
		this.id = id;
	}

	public NullablesSection getSimpleSection() {
		if (simpleSection == null) {
			simpleSection = new NullablesSection();
		}
		return simpleSection;
	}

	public void setSimpleSection(NullablesSection simpleSection) {
		this.simpleSection = simpleSection;
	}

	public NullablesSection getOverridesSection() {
		if (overridesSection == null) {
			overridesSection = new NullablesSection();
		}
		return overridesSection;
	}

	public void setOverridesSection(NullablesSection overridesSection) {
		this.overridesSection = overridesSection;
	}

}
