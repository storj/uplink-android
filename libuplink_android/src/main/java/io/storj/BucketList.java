package io.storj;

public class BucketList {
	
	private io.storj.libuplink.mobile.BucketList list;
	
	BucketList(io.storj.libuplink.mobile.BucketList list) {
		this.list = list;
	}
	
	public BucketInfo getItem(int index) throws StorjException {
		try {
			io.storj.libuplink.mobile.BucketInfo info = this.list.item(index);
			return new BucketInfo(info);
		} catch (Exception e) {
			throw ExceptionUtil.toStorjException(e);
		}
	}
	
	public boolean hasMore() {
		return this.list.more();
	}
	
	public int getLength() {
		return (int) this.list.length();
	}

}
