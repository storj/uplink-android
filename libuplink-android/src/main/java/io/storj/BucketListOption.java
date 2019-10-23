package io.storj;

/**
 * Options for listing buckets.
 *
 * @see Project#listBuckets(BucketListOption...)
 */
public class BucketListOption {

    private enum Key {
        CURSOR,
        PAGE_SIZE
    }

    private Key key;

    private Object value;

    BucketListOption(Key key, Object value) {
        this.key = key;
        this.value = value;
    }

    /**
     * Option for the starting cursor in the bucket listing. The name of the first bucket in the
     * result listing will be <b>after</b> the cursor.
     *
     * @param cursor a {@link String} with starting cursor
     * @return a {@link BucketListOption}
     */
    public static BucketListOption cursor(String cursor) {
        return new BucketListOption(Key.CURSOR, cursor);
    }

    /**
     * Option for the internal page size used in the iterator of the bucket listing.
     *
     * <p>While iterating over the bucket listing, the iterator makes remote calls to the satellite
     * for receiving portion if the listing. The page size determines how many buckets are returned
     * on each remote call. Clients may tune the page size for performance reasons.</p>
     *
     * @param pageSize the page size
     * @return a {@link BucketListOption}
     */
    public static BucketListOption pageSize(int pageSize) {
        return new BucketListOption(Key.PAGE_SIZE, pageSize);
    }

    static BucketListOptions internal(BucketListOption... options) {
        BucketListOptions listOptions = new BucketListOptions();
        for (BucketListOption option : options) {
            if (option.key == Key.CURSOR) {
                listOptions.cursor = option.value.toString();
            } else if (option.key == Key.PAGE_SIZE) {
                listOptions.pageSize = (Integer) option.value;
            }
        }

        return listOptions;
    }

    static class BucketListOptions {
        String cursor;
        int pageSize;
    }

}
