package io.storj;

public class RedundancyScheme {
	
	private io.storj.libuplink.mobile.RedundancyScheme scheme;

	RedundancyScheme(io.storj.libuplink.mobile.RedundancyScheme scheme) {
		this.scheme = scheme;
	}
	
	public RedundancyAlgorithm getAlgorithm() {
		return RedundancyAlgorithm.fromValue(scheme.getAlgorithm());
	}

	public short getRequiredShares() {
		return scheme.getRequiredShares();
	}
	
	public short getRepairShares() {
		return scheme.getRepairShares();
	}
	
	public short getSuccessShares() {
		return scheme.getOptimalShares();
	}
	
	public short getTotalShares() {
		return scheme.getTotalShares();
	}
	
	public int getShareSize() {
		return scheme.getShareSize();
	}

	io.storj.libuplink.mobile.RedundancyScheme internal() {
		return scheme;
	}

	private RedundancyScheme(Builder builder) {
		this.scheme = new io.storj.libuplink.mobile.RedundancyScheme();
		if (builder.algorithm != null) {
			this.scheme.setAlgorithm(builder.algorithm.getValue());
		}
		this.scheme.setRequiredShares(builder.required);
		this.scheme.setRepairShares(builder.repair);
		this.scheme.setOptimalShares(builder.success);
		this.scheme.setTotalShares(builder.total);
		this.scheme.setShareSize(builder.shareSize);
	}

	public static class Builder {

		private RedundancyAlgorithm algorithm;
		private short required;
		private short repair;
		private short success;
		private short total;
		private int shareSize;

		public Builder setAlgorithm(RedundancyAlgorithm algorithm) {
			this.algorithm = algorithm;
			return this;
		}

		public Builder setRequiredShares(short count) {
			this.required = count;
			return this;
		}

		public Builder setRepairShares(short count) {
			this.repair = count;
			return this;
		}

		public Builder setSuccessShares(short count) {
			this.success = count;
			return this;
		}

		public Builder setTotalShares(short count) {
			this.total = count;
			return this;
		}

		public Builder setShareSize(int size) {
			this.shareSize = size;
			return this;
		}

		public RedundancyScheme build() {
			return new RedundancyScheme(this);
		}
	}
}
