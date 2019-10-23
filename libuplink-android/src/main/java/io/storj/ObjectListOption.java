package io.storj;

import io.storj.libuplink.mobile.ListOptions;

/**
 * Options for listing objects.
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
     * Option for the path prefix to filter the listing results.
     *
     * @param prefix a {@link String} with path prefix
     * @return a {@link ObjectListOption}
     */
    public static ObjectListOption prefix(String prefix) {
        return new ObjectListOption(Key.PREFIX, prefix);
    }

    /**
     * Option for the starting cursor in the object listing. The path of the first object in the
     * result listing will be <b>after</b> the cursor.
     *
     * @param cursor a {@link String} with starting cursor
     * @return a {@link ObjectListOption}
     */
    public static ObjectListOption cursor(String cursor) {
        return new ObjectListOption(Key.CURSOR, cursor);
    }

    /**
     * Option for recursive listing.
     *
     * @param recursive <code>true</code> for recursive listing, <code>false</code> otherwise
     * @return a {@link ObjectListOption}
     */
    public static ObjectListOption recursive(boolean recursive) {
        return new ObjectListOption(Key.RECURSIVE, recursive);
    }

    /**
     * Option for the internal page size used in the iterator of the object listing.
     *
     * <p>While iterating over the object listing, the iterator makes remote calls to the satellite
     * for receiving portion if the listing. The page size determines how many objects are returned
     * on each remote call. Clients may tune the page size for performance reasons.</p>
     *
     * @param pageSize the page size
     * @return a {@link ObjectListOption}
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
