package io.storj;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import io.storj.internal.Uplink;

public class ObjectInfo {

    public class SystemMetadata {
        private Date created;
        private Date expires;
        private long contentLength;

        protected SystemMetadata(Date created, Date expires, long contentLength){
            this.created = created;
            this.expires = expires;
            this.contentLength= contentLength;
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

    private String key;
    private boolean isPrefix;
    private SystemMetadata system;
    private Map<String, String> custom;

    ObjectInfo(){

    }

    ObjectInfo(Uplink.Object object){
        this.key = object.key.getString(0);
        this.isPrefix = object.is_prefix == 1;
        this.system = new SystemMetadata(new Date(object.system.created),
                new Date(object.system.expires), object.system.content_length);

        // TODO add custom metadata
        this.custom = new HashMap<>();
//        for (long i =0; i< object.custom.count.longValue();i++){
//            object.custom.entries.
//        }
//        object.custom.
    }

    public String getKey() {
        return key;
    }

    public boolean isPrefix() {
        return isPrefix;
    }

    public SystemMetadata getSystem() {
        return system;
    }

    public Map<String, String> getCustom() {
        return custom;
    }
}
