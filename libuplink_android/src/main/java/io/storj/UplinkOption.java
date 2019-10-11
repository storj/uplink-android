package io.storj;

import io.storj.libuplink.mobile.Config;

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

    public static UplinkOption maxInlineSize(long maxInlineSize) {
        return new UplinkOption(Key.MAX_INLINE_SIZE, maxInlineSize);
    }

    public static UplinkOption maxMemory(long maxMemory) {
        return new UplinkOption(Key.MAX_MEMORY, maxMemory);
    }

    public static UplinkOption tempDir(String tempDir) {
        return new UplinkOption(Key.TEMP_DIR, tempDir);
    }

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
