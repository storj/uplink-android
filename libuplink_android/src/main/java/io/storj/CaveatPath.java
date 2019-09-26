package io.storj;

public class CaveatPath {

    private byte[] bucket;
    private byte[] encryptedPathPrefix;

    public CaveatPath(byte[] bucket, byte[] encryptedPathPrefix){
        this.bucket = bucket;
        this.encryptedPathPrefix = encryptedPathPrefix;
    }

    io.storj.libuplink.mobile.CaveatPath internal(){
        io.storj.libuplink.mobile.CaveatPath path = new io.storj.libuplink.mobile.CaveatPath();
        path.setBucket(this.bucket);
        path.setEncryptedPathPrefix(this.bucket);
        return path;
    }
}
