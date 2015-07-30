package fr.openwide.core.jpa.more.business.file.model.path;

import java.util.Arrays;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.FilenameUtils;

import fr.openwide.core.jpa.more.business.file.model.path.SimpleFileStorePathGeneratorImpl;


public class HashTableFileStorePathGeneratorImpl extends SimpleFileStorePathGeneratorImpl {
	
	public static final int MIN_HASH_TABLE_BYTE_SIZE = 1;
	public static final int MAX_HASH_TABLE_BYTE_SIZE = 16;
	
	private final int hashSizeInBytes;

	public HashTableFileStorePathGeneratorImpl(int hashSizeInBytes) {
		if (!(MIN_HASH_TABLE_BYTE_SIZE <= hashSizeInBytes && hashSizeInBytes <= MAX_HASH_TABLE_BYTE_SIZE)) {
			throw new IllegalArgumentException("The hash size (in bytes) must be between "
					+ MIN_HASH_TABLE_BYTE_SIZE + " and " + MAX_HASH_TABLE_BYTE_SIZE);
		}
		this.hashSizeInBytes = hashSizeInBytes;
	}
	
	@Override
	protected String getFileKeyPath(String fileKey) {
		byte[] digest = DigestUtils.md5(fileKey.getBytes());
		String partialDigestString = Hex.encodeHexString(Arrays.copyOf(digest, hashSizeInBytes));
		
		return FilenameUtils.concat(partialDigestString, super.getFileKeyPath(fileKey));
	}

}
