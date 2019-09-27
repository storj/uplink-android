package io.storj;

public class CaveatPath {

    private io.storj.libuplink.mobile.CaveatPath path;

    public CaveatPath(String bucket, String encryptedPathPrefix) {
        io.storj.libuplink.mobile.CaveatPath path = new io.storj.libuplink.mobile.CaveatPath();
        path.setBucket(bucket.getBytes());
        path.setEncryptedPathPrefix(encryptedPathPrefix.getBytes());
    }

    io.storj.libuplink.mobile.CaveatPath internal() {
        return path;
    }
}
