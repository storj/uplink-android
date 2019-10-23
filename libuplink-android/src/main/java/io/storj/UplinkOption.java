package io.storj;

import io.storj.libuplink.mobile.Config;

/**
 * Options for configuring {@link Uplink}.
 */
public class UplinkOption {

    private enum Key {
        MAX_INLINE_SIZE,
        MAX_MEMORY,
        TEMP_DIR,
        SKIP_PEER_CA_WHITELIST
    }

    private Key key;

    private Object value;

    UplinkOption(Key key, Object value) {
        this.key = key;
        this.value = value;
    }

    /**
     * Option for the maximum size of inline objects. The uplink will attempt to store a new object
     * below this size in the satellite's metainfo. Objects at or below this size will be marked for
     * inline storage, and objects above this size will not. (The satellite may reject the inline
     * storage and require remote storage, still.)
     *
     * @param maxInlineSize the maximum inline size in bytes
     * @return an {@link UplinkOption}
     */
    public static UplinkOption maxInlineSize(long maxInlineSize) {
        return new UplinkOption(Key.MAX_INLINE_SIZE, maxInlineSize);
    }

    /**
     * Option for the default maximum amount of memory to be allocated for read buffers while
     * performing erasure decoding on the downloaded data. If set to zero, the library default
     * (4 MiB) will be used. If set to a negative value, the system will use the
     * smallest amount of memory possible.
     *
     * @param maxMemory the maximum memory for read buffers in bytes
     * @return an {@link UplinkOption}
     */
    public static UplinkOption maxMemory(long maxMemory) {
        return new UplinkOption(Key.MAX_MEMORY, maxMemory);
    }

    /**
     * Option for temp directory to be used during upload. If not set, OS temp directory will be
     * used.
     *
     * @param tempDir path to temp directory
     * @return an {@link UplinkOption}
     */
    public static UplinkOption tempDir(String tempDir) {
        return new UplinkOption(Key.TEMP_DIR, tempDir);
    }

    /**
     * Option that determines whether to require all remote hosts to have identity certificates
     * signed by Certificate Authorities in the default whitelist. If set to true, the whitelist
     * will be ignored.
     *
     * @param skipPeerCAWhitelist set to <code>true</code> to ignore the whilelist, otherwise set to
     *                            <code>false</code>
     * @return an {@link UplinkOption}
     */
    public static UplinkOption skipPeerCAWhitelist(boolean skipPeerCAWhitelist) {
        return new UplinkOption(Key.SKIP_PEER_CA_WHITELIST, skipPeerCAWhitelist);
    }

    static UplinkOptions internal(UplinkOption... options) {
        UplinkOptions uplinkOptions = new UplinkOptions();
        uplinkOptions.config = new Config();

        for (UplinkOption option : options) {
            if (option.key == Key.MAX_INLINE_SIZE) {
                uplinkOptions.config.setMaxInlineSize((Long) option.value);
            } else if (option.key == Key.MAX_MEMORY) {
                uplinkOptions.config.setMaxMemory((Long) option.value);
            } else if (option.key == Key.TEMP_DIR) {
                uplinkOptions.tempDir = option.value.toString();
            } else if (option.key == Key.SKIP_PEER_CA_WHITELIST) {
                uplinkOptions.config.setSkipPeerCAWhitelist((Boolean) option.value);
            }
        }
        return uplinkOptions;
    }

    static class UplinkOptions {
        String tempDir;
        io.storj.libuplink.mobile.Config config;
    }

}
