package io.storj;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Represents a set of restriction to apply to {@link ApiKey}s and {@link Scope}s.
 */
public class Caveat {

    private io.storj.libuplink.mobile.Caveat caveat;

    Caveat(Builder builder) {
        this.caveat = new io.storj.libuplink.mobile.Caveat();
        this.caveat.setDisallowReads(builder.disallowReads);
        this.caveat.setDisallowWrites(builder.disallowWrites);
        this.caveat.setDisallowLists(builder.disallowLists);
        this.caveat.setDisallowDeletes(builder.disallowDeletes);
        if (builder.notAfter != null) {
            this.caveat.setNotAfter(builder.notAfter.getTime() / 1000);
        }
        if (builder.notBefore != null) {
            this.caveat.setNotBefore(builder.notBefore.getTime() / 1000);
        }
        this.caveat.setNonce(builder.nonce);
        for (CaveatPath path : builder.caveatPaths) {
            this.caveat.addCaveatPath(path.internal());
        }
    }

    io.storj.libuplink.mobile.Caveat internal() {
        return this.caveat;
    }

    /**
     * Builder for {@link Caveat} objects.
     */
    public static class Builder {

        private boolean disallowReads;
        private boolean disallowWrites;
        private boolean disallowLists;
        private boolean disallowDeletes;
        private Date notAfter;
        private Date notBefore;
        private byte[] nonce;
        private List<CaveatPath> caveatPaths = new ArrayList<>();

        /**
         * Determines if reads are disallowed.
         *
         * @param disallowReads set to <code>true</code> to disallow reads
         * @return a reference to this object
         */
        public Builder disallowReads(boolean disallowReads) {
            this.disallowReads = disallowReads;
            return this;
        }

        /**
         * Determines if writes are disallowed.
         *
         * @param disallowWrites set to <code>true</code> to disallow writes
         * @return a reference to this object
         */
        public Builder disallowWrites(boolean disallowWrites) {
            this.disallowWrites = disallowWrites;
            return this;
        }

        /**
         * Determines if listing buckets and objects is disallowed.
         *
         * @param disallowLists set to <code>true</code> to disallow listings.
         * @return a reference to this object
         */
        public Builder disallowLists(boolean disallowLists) {
            this.disallowLists = disallowLists;
            return this;
        }

        /**
         * Determines if deletes are disallowed.
         *
         * @param disallowDeletes set to <code>true</code> to disallow deletes.
         * @return a reference to this object
         */
        public Builder disallowDeletes(boolean disallowDeletes) {
            this.disallowDeletes = disallowDeletes;
            return this;
        }

        /**
         * Determines the latest date of the caveat validity.
         *
         * @param notAfter a {@link Date}
         * @return a reference to this object
         */
        public Builder notAfter(Date notAfter) {
            this.notAfter = notAfter;
            return this;
        }

        /**
         * Determines the earlies date of the caveat validity.
         *
         * @param notBefore a {@link Date}
         * @return a reference to this object
         */
        public Builder notBefore(Date notBefore) {
            this.notBefore = notBefore;
            return this;
        }

        Builder nonce(byte[] nonce) {
            this.nonce = nonce;
            return this;
        }

        Builder addAllowedPath(CaveatPath caveatPath) {
            this.caveatPaths.add(caveatPath);
            return this;
        }

        /**
         * Creates the new {@link Caveat} object from this builder.
         *
         * @return a {@link Caveat}
         */
        public Caveat build() {
            return new Caveat(this);
        }
    }

}
