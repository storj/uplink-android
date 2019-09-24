package io.storj;

public enum RedundancyAlgorithm {
	
	REEDSOLOMON((byte) 1);
	
	private byte value;
	
	private RedundancyAlgorithm(byte value) {
		this.value = value;
	}
	
	byte getValue() {
		return value;
	}

	public static RedundancyAlgorithm fromValue(byte value) {
		for (RedundancyAlgorithm v : RedundancyAlgorithm.values()) {
			if (v.value == value) {
				return v;
			}
		}
		return null;
	}

}
