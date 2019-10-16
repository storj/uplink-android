package io.storj;

/**
 * Options for bucket listing.
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

    public static BucketListOption cursor(String cursor) {
        return new BucketListOption(Key.CURSOR, cursor);
    }

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
