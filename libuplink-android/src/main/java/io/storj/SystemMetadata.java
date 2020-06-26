package io.storj;

import java.util.Date;

public class SystemMetadata {

    private Date created;
    private Date expires;
    private long contentLength;

    protected SystemMetadata(Date created, Date expires, long contentLength) {
        this.created = created;
        this.expires = expires;
        this.contentLength = contentLength;
    }

    public Date getCreated() {
        return created;
    }

    public Date getExpires() {
        return expires;
    }

    public long getContentLength() {
        return contentLength;
    }
}