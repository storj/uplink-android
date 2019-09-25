package io.storj;

public class ReaderOptions {

    private io.storj.libuplink.mobile.ReaderOptions options;

    private ReaderOptions(Builder builder){
        this.options = new io.storj.libuplink.mobile.ReaderOptions();
    }

    io.storj.libuplink.mobile.ReaderOptions internal() {
        return options;
    }

    public static class Builder {
        public ReaderOptions build() {
            return new ReaderOptions(this);
        }
    }


}
