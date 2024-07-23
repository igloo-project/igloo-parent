package org.iglooproject.jpa.business.generic.model;

import java.io.Serializable;

public interface IPredefinedIdEntity<K extends Serializable & Comparable<K>> {

  K getPredefinedId();
}
