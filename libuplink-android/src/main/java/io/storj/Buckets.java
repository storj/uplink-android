package io.storj;

import java.util.Iterator;
import java.util.function.Consumer;

import io.storj.internal.Uplink;

public class Buckets {

    private Uplink.Project.ByReference project;
    private BucketListOption[] options;

    Buckets(Uplink.Project.ByReference project, BucketListOption... options) {
        this.project = project;
        this.options = options;
    }

    private class InternalIterator implements Iterator<BucketInfo>, Iterable<BucketInfo> {

        private Uplink.BucketIterator.ByReference cIterator;

        private BucketInfo currentItem;
        private Boolean hasNext = null;

        public InternalIterator(Uplink.BucketIterator.ByReference cIterator) {
            this.cIterator = cIterator;
        }

        @Override
        public boolean hasNext() {
            if (this.hasNext == null) {
                this.hasNext = io.storj.internal.Uplink.INSTANCE.bucket_iterator_next(this.cIterator);
                if (this.hasNext) {
                    Uplink.Bucket.ByReference bucket = io.storj.internal.Uplink.INSTANCE.bucket_iterator_item(this.cIterator);
                    this.currentItem = new BucketInfo(bucket);
                    io.storj.internal.Uplink.INSTANCE.free_bucket(bucket);
                }
            }
            return this.hasNext;
        }

        @Override
        public BucketInfo next() {
            if (currentItem != null) {
                this.hasNext = io.storj.internal.Uplink.INSTANCE.bucket_iterator_next(this.cIterator);
                BucketInfo result = this.currentItem;
                if (hasNext) {
                    Uplink.Bucket.ByReference bucket = io.storj.internal.Uplink.INSTANCE.bucket_iterator_item(this.cIterator);
                    this.currentItem = new BucketInfo(bucket);
                    io.storj.internal.Uplink.INSTANCE.free_bucket(bucket);
                }
                return result;
            } else {
                this.hasNext = io.storj.internal.Uplink.INSTANCE.bucket_iterator_next(this.cIterator);
                if (this.hasNext) {
                    Uplink.Bucket.ByReference bucket = io.storj.internal.Uplink.INSTANCE.bucket_iterator_item(this.cIterator);
                    BucketInfo result = new BucketInfo(bucket);
                    io.storj.internal.Uplink.INSTANCE.free_bucket(bucket);
                    return result;
                }
            }
            return null;
        }

        @Override
        public Iterator<BucketInfo> iterator() {
            return this;
        }
    }

    public void iterate(Consumer<BucketInfo> bi) throws StorjException {
        Uplink.BucketIterator.ByReference cIterator = io.storj.internal.Uplink.INSTANCE.list_buckets(project, BucketListOption.internal(options));
        try {
            for (BucketInfo bucketInfo : new InternalIterator(cIterator)){
                bi.accept(bucketInfo);
            }
            Uplink.Error.ByReference error = io.storj.internal.Uplink.INSTANCE.bucket_iterator_err(cIterator);
            ExceptionUtil.handleError(error);
        } finally {
            io.storj.internal.Uplink.INSTANCE.free_bucket_iterator(cIterator);
        }
    }

}