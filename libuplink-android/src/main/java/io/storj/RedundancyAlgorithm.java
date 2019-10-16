package io.storj;

public enum RedundancyAlgorithm {

    REED_SOLOMON((byte) 1);

    private byte value;

    RedundancyAlgorithm(byte value) {
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
