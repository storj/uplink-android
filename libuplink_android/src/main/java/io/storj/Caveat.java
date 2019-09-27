package io.storj;

import java.util.ArrayList;
import java.util.List;

public class Caveat {

    private io.storj.libuplink.mobile.Caveat caveat;

    Caveat(Builder builder) {
        this.caveat = new io.storj.libuplink.mobile.Caveat();
        this.caveat.setDisallowReads(builder.disallowReads);
        this.caveat.setDisallowWrites(builder.disallowWrites);
        this.caveat.setDisallowLists(builder.disallowLists);
        this.caveat.setDisallowDeletes(builder.disallowDeletes);
        this.caveat.setNotAfter(builder.notAfter);
        this.caveat.setNotBefore(builder.notBefore);
        this.caveat.setNonce(builder.nonce);
        for (CaveatPath path: builder.caveatPaths) {
            this.caveat.addCaveatPath(path.internal());
        }
    }

    io.storj.libuplink.mobile.Caveat internal(){
        return this.caveat;
    }

    public static class Builder {

        private boolean disallowReads;
        private boolean disallowWrites;
        private boolean disallowLists;
        private boolean disallowDeletes;
        private long notAfter;
        private long notBefore;
        private byte[] nonce;
        private List<CaveatPath> caveatPaths = new ArrayList<>();

        public Builder disallowReads(boolean disallowReads) {
            this.disallowReads = disallowReads;
            return this;
        }

        public Builder disallowWrites(boolean disallowWrites) {
            this.disallowWrites = disallowWrites;
            return this;
        }


        public Builder disallowLists(boolean disallowLists) {
            this.disallowLists = disallowLists;
            return this;
        }

        public Builder disallowDeletes(boolean disallowDeletes) {
            this.disallowDeletes = disallowDeletes;
            return this;
        }

        public Builder notAfter(long notAfter) {
            this.notAfter = notAfter;
            return this;
        }

        public Builder notBefore(long notBefore) {
            this.notBefore = notBefore;
            return this;
        }

        public Builder nonce(byte[] nonce) {
            this.nonce = nonce;
            return this;
        }

        public Builder addCaveatPath(CaveatPath caveatPath) {
            this.caveatPaths.add(caveatPath);
            return this;
        }

        public Caveat build() {
            return new Caveat(this);
        }
    }

}
