package io.storj;

import java.util.Iterator;

import io.storj.internal.Uplink;

public class Buckets {

    private io.storj.internal.Uplink.Project project;
    private BucketListOption[] options;

    Buckets(io.storj.internal.Uplink.Project project, BucketListOption... options) {
        this.project = project;
        this.options = options;
    }

    public interface BucketsIterator {
        void iterate(Iterable<BucketInfo> i);
    }

    private class InternalIterator implements Iterator<BucketInfo>, Iterable<BucketInfo> {

        private Uplink.BucketIterator cIterator;

        private BucketInfo currentItem;
        private Boolean hasNext = null;

        public InternalIterator(Uplink.BucketIterator cIterator) {
            this.cIterator = cIterator;
        }

        @Override
        public boolean hasNext() {
            if (this.hasNext == null) {
                this.hasNext = io.storj.internal.Uplink.INSTANCE.bucket_iterator_next(this.cIterator);
                if (this.hasNext) {
                    Uplink.Bucket bucket = io.storj.internal.Uplink.INSTANCE.bucket_iterator_item(this.cIterator);
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
                    Uplink.Bucket bucket = io.storj.internal.Uplink.INSTANCE.bucket_iterator_item(this.cIterator);
                    this.currentItem = new BucketInfo(bucket);
                    io.storj.internal.Uplink.INSTANCE.free_bucket(bucket);
                }
                return result;
            } else {
                this.hasNext = io.storj.internal.Uplink.INSTANCE.bucket_iterator_next(this.cIterator);
                if (this.hasNext) {
                    Uplink.Bucket bucket = io.storj.internal.Uplink.INSTANCE.bucket_iterator_item(this.cIterator);
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

    public void iterate(BucketsIterator bi) throws StorjException {
        Uplink.BucketIterator cIterator = io.storj.internal.Uplink.INSTANCE.list_buckets(project, BucketListOption.internal(options));
        try {
            bi.iterate(new InternalIterator(cIterator));
            Uplink.Error error = io.storj.internal.Uplink.INSTANCE.bucket_iterator_err(cIterator);
            ExceptionUtil.handleError(error);
        } finally {
            io.storj.internal.Uplink.INSTANCE.free_bucket_iterator(cIterator);
        }
    }

}