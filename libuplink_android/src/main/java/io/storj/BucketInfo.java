package io.storj;

import android.support.annotation.Nullable;

import java.util.Date;

public class BucketInfo {

	private io.storj.libuplink.mobile.BucketInfo info;

	BucketInfo(io.storj.libuplink.mobile.BucketInfo info) {
		this.info = info;
	}

	public String getName() {
		return info.getName();
	}

	public Date getCreated() {
		return new Date(info.getCreated());
	}

	public CipherSuite getPathCipher() {
		return CipherSuite.fromValue(info.getPathCipher());
	}

	public long getSegmentsSize() {
		return info.getSegmentsSize();
	}

	public RedundancyScheme getRedundancyScheme() {
		return new RedundancyScheme(info.getRedundancyScheme());
	}

	public EncryptionParameters getEncryptionParameters() {
		return new EncryptionParameters(info.getEncryptionParameters());
	}

	@Override
	public boolean equals(@Nullable Object obj) {
		if (!(obj instanceof BucketInfo)) {
			return false;
		}
		BucketInfo that = (BucketInfo) obj;
		return info.equals(that.info);
	}

	@Override
	public int hashCode() {
		return info.hashCode();
	}
}
