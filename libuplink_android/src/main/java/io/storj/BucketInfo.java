package io.storj;

public class BucketInfo {

	private io.storj.libuplink.mobile.BucketInfo info;

	BucketInfo(io.storj.libuplink.mobile.BucketInfo info) {
		this.info = info;
	}

	public String getName() {
		return info.getName();
	}
}
