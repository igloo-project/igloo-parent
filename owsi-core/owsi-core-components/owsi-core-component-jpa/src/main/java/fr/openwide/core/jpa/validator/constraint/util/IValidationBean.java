package fr.openwide.core.jpa.validator.constraint.util;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.javatuples.KeyValue;

import fr.openwide.core.commons.util.fieldpath.FieldPath;

public interface IValidationBean extends Serializable {

	Map<FieldPath, List<KeyValue<FieldPath, Object>>> getReelRootPaths();

}
