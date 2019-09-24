package io.storj;

public class RedundancyScheme {
	
	private io.storj.libuplink.mobile.RedundancyScheme scheme;
	
	public RedundancyScheme(RedundancyAlgorithm algorithm, short required, short repair, short success, short total, int shareSize) {
		this.scheme = new io.storj.libuplink.mobile.RedundancyScheme();
		this.scheme.setAlgorithm(algorithm.getValue());
		this.scheme.setRequiredShares(required);
		this.scheme.setRepairShares(repair);
		this.scheme.setOptimalShares(success);
		this.scheme.setTotalShares(total);
		this.scheme.setShareSize(shareSize);
	}
	
	public RedundancyAlgorithm getAlgorithm() {
		return RedundancyAlgorithm.fromValue(this.scheme.getAlgorithm());
	}

	public short getRequiredShares() {
		return this.scheme.getRequiredShares();
	}
	
	public short getRepairShares() {
		return this.scheme.getRepairShares();
	}
	
	public short getSuccessShares() {
		return this.scheme.getOptimalShares();
	}
	
	public short getTotalShares() {
		return this.scheme.getTotalShares();
	}
	
	public int getShareSize() {
		return this.scheme.getShareSize();
	}
	
	io.storj.libuplink.mobile.RedundancyScheme internal() {
		return scheme;
	}
}
