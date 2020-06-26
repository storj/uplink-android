package io.storj;

import io.storj.internal.PointerUtils;

/**
 * Options for configuring {@link Uplink}.
 */
public class UplinkOption {

    private enum Key {
        TEMP_DIR,
        USER_AGENT,
        DIAL_TIMEOUT,
    }
    private Key key;

    private Object value;

    UplinkOption(Key key, Object value) {
        this.key = key;
        this.value = value;
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

    public static UplinkOption userAgent(String userAgent) {
        return new UplinkOption(Key.USER_AGENT, userAgent);
    }

    public static UplinkOption userAgent(int dialTimeout) {
        return new UplinkOption(Key.DIAL_TIMEOUT, dialTimeout);
    }

    static io.storj.internal.Uplink.Config.ByValue internal(UplinkOption... options) {
        io.storj.internal.Uplink.Config.ByValue config = new io.storj.internal.Uplink.Config.ByValue();

        for (UplinkOption option : options) {
            if (option.key == Key.TEMP_DIR) {
                config.temp_directory = PointerUtils.toPointer(option.value.toString());
            } else if (option.key == Key.USER_AGENT) {
                config.user_agent = PointerUtils.toPointer(option.value.toString());
            } else if (option.key == Key.DIAL_TIMEOUT) {
                config.dial_timeout_milliseconds = (Integer) option.value;
            }
        }
        return config;
    }

}