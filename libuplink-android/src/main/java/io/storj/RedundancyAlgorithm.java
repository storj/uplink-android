package io.storj;

/**
 * Enumeration of the redundancy algorithms supported by Storj libraries for erasure encoding the
 * data stored on the network.
 */
public enum RedundancyAlgorithm {

    /**
     * Reed-Solomon erasure encoding algorithm.
     */
    REED_SOLOMON((byte) 1);

    private byte value;

    RedundancyAlgorithm(byte value) {
        this.value = value;
    }

    byte getValue() {
        return value;
    }

    static RedundancyAlgorithm fromValue(byte value) {
        for (RedundancyAlgorithm v : RedundancyAlgorithm.values()) {
            if (v.value == value) {
                return v;
            }
        }
        return null;
    }

}
