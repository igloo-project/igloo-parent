package org.igloo.storage.api;

import javax.annotation.Nullable;

public interface IMimeTypeResolver {

	String resolve(@Nullable String filename);

}
