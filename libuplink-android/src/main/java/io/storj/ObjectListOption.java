package io.storj;

import io.storj.libuplink.mobile.ListOptions;

/**
 * Options for objects listing.
 */
public class ObjectListOption {

    private enum Key {
        PREFIX,
        CURSOR,
        RECURSIVE,
        PAGE_SIZE
    }

    private Key key;

    private Object value;

    ObjectListOption(Key key, Object value) {
        this.key = key;
        this.value = value;
    }

    /**
     * Prefix option.
     *
     * @param prefix prefix for listing
     * @return prefix object list option
     */
    public static ObjectListOption prefix(String prefix) {
        return new ObjectListOption(Key.PREFIX, prefix);
    }

    /**
     * Cursor option.
     *
     * @param cursor cursor for listing
     * @return cursor object list option
     */
    public static ObjectListOption cursor(String cursor) {
        return new ObjectListOption(Key.CURSOR, cursor);
    }

    /**
     * Recursive option.
     *
     * @param recursive true if listing should work in recursive way
     * @return recursive object list option
     */
    public static ObjectListOption recursive(boolean recursive) {
        return new ObjectListOption(Key.RECURSIVE, recursive);
    }

    /**
     * Page size option.
     *
     * @param pageSize number of elements for one page
     * @return page size object list option
     */
    public static ObjectListOption pageSize(int pageSize) {
        return new ObjectListOption(Key.PAGE_SIZE, pageSize);
    }

    static io.storj.libuplink.mobile.ListOptions internal(ObjectListOption... options) {
        io.storj.libuplink.mobile.ListOptions listOptions = new ListOptions();
        for (ObjectListOption option : options) {
            if (option.key == Key.PREFIX) {
                listOptions.setPrefix(option.value.toString());
            } else if (option.key == Key.CURSOR) {
                listOptions.setCursor(option.value.toString());
            } else if (option.key == Key.RECURSIVE) {
                listOptions.setRecursive((Boolean) option.value);
            } else if (option.key == Key.PAGE_SIZE) {
                listOptions.setLimit((Integer) option.value);
            }
        }
        listOptions.setDirection(2); // after
        return listOptions;
    }

}