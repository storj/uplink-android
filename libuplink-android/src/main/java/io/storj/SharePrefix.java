package io.storj;

public class SharePrefix {

    private String bucket;
    private String prefix;

    public SharePrefix(String bucket) {
        this.bucket = bucket;
    }

    public SharePrefix(String bucket, String prefix) {
        this.bucket = bucket;
        this.prefix = prefix;
    }

    public String getBucket() {
        return bucket;
    }

    public String getPrefix() {
        return prefix;
    }
}
