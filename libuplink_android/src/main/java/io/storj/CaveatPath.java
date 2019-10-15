package io.storj;

import static java.nio.charset.StandardCharsets.UTF_8;

class CaveatPath {

    private io.storj.libuplink.mobile.CaveatPath path;

    CaveatPath(String bucket, String encryptedPathPrefix) {
        path = new io.storj.libuplink.mobile.CaveatPath();
        path.setBucket(bucket.getBytes(UTF_8));
        path.setEncryptedPathPrefix(encryptedPathPrefix.getBytes(UTF_8));
    }

    io.storj.libuplink.mobile.CaveatPath internal() {
        return path;
    }
}
