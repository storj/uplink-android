package io.storj;

import android.support.annotation.Nullable;

import java.io.Serializable;
import java.util.Objects;

/**
 * Specifies the parameters and the algorithm for redundancy.
 */
public class RedundancyScheme implements Serializable {

    private RedundancyAlgorithm algorithm;
    private short required;
    private short repair;
    private short success;
    private short total;
    private int shareSize;

    RedundancyScheme(io.storj.libuplink.mobile.RedundancyScheme scheme) {
        this.algorithm = RedundancyAlgorithm.fromValue(scheme.getAlgorithm());
        this.required = scheme.getRequiredShares();
        this.repair = scheme.getRepairShares();
        this.success = scheme.getOptimalShares();
        this.total = scheme.getTotalShares();
        this.shareSize = scheme.getShareSize();
    }

    public RedundancyAlgorithm getAlgorithm() {
        return algorithm;
    }

    public short getRequiredShares() {
        return required;
    }

    public short getRepairShares() {
        return repair;
    }

    public short getSuccessShares() {
        return success;
    }

    public short getTotalShares() {
        return total;
    }

    public int getShareSize() {
        return shareSize;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RedundancyScheme that = (RedundancyScheme) o;
        return required == that.required &&
                repair == that.repair &&
                success == that.success &&
                total == that.total &&
                shareSize == that.shareSize &&
                algorithm == that.algorithm;
    }

    @Override
    public int hashCode() {
        return Objects.hash(algorithm, required, repair, success, total, shareSize);
    }

    io.storj.libuplink.mobile.RedundancyScheme internal() {
        io.storj.libuplink.mobile.RedundancyScheme scheme = new io.storj.libuplink.mobile.RedundancyScheme();
        if (algorithm != null) {
            scheme.setAlgorithm(algorithm.getValue());
        }
        scheme.setRequiredShares(required);
        scheme.setRepairShares(repair);
        scheme.setOptimalShares(success);
        scheme.setTotalShares(total);
        scheme.setShareSize(shareSize);
        return scheme;
    }

    private RedundancyScheme(Builder builder) {
        this.algorithm = builder.algorithm;
        this.required = builder.required;
        this.repair = builder.repair;
        this.success = builder.success;
        this.total = builder.total;
        this.shareSize = builder.shareSize;
    }

    /**
     * Builder for RedundancyScheme object.
     */
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
