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

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof RedundancyScheme)) {
            return false;
        }
        RedundancyScheme that = (RedundancyScheme) obj;
        return scheme.equals(that.scheme);
    }

    @Override
    public int hashCode() {
        return scheme.hashCode();
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

        /**
         * Algorithm determines the algorithm to be used for redundancy.
         *
         * @param algorithm algorithm for redundancy
         * @return the builder
         */
        public Builder setAlgorithm(RedundancyAlgorithm algorithm) {
            this.algorithm = algorithm;
            return this;
        }

        /**
         * RequiredShares is the minimum number of shares required to recover a
         * stripe, reed-solomon k.
         *
         * @param count
         * @return the builder
         */
        public Builder setRequiredShares(short count) {
            this.required = count;
            return this;
        }

        /**
         * RepairShares is the minimum number of safe shares that can remain
         * before a repair is triggered.
         *
         * @param count
         * @return the builder
         */
        public Builder setRepairShares(short count) {
            this.repair = count;
            return this;
        }

        /**
         * SuccessShares is the desired total number of shares for a segment.
         *
         * @param count
         * @return the builder
         */
        public Builder setSuccessShares(short count) {
            this.success = count;
            return this;
        }

        /**
         * TotalShares is the number of shares to encode. If it is larger than
         * OptimalShares, slower uploads of the excess shares will be aborted in
         * order to improve performance.
         *
         * @param count
         * @return the builder
         */
        public Builder setTotalShares(short count) {
            this.total = count;
            return this;
        }

        /**
         * ShareSize is the size in bytes for each erasure shares.
         *
         * @param size share size in bytes
         * @return the builder
         */
        public Builder setShareSize(int size) {
            this.shareSize = size;
            return this;
        }

        public RedundancyScheme build() {
            return new RedundancyScheme(this);
        }
    }
}
