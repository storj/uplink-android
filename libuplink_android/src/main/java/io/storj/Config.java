package io.storj;

public class Config {
	
	private io.storj.libuplink.mobile.Config config;
	private String tempDir;

	public long getMaxInlineSize() {
		return config.getMaxInlineSize();
	}

	public long getMaxMemory() {
		return config.getMaxMemory();
	}

	public String getTempDir() {
		return tempDir;
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Config)) {
			return false;
		}
		Config that = (Config) obj;
		return config.equals(that.config);
	}

	@Override
	public int hashCode() {
		return config.hashCode();
	}

	io.storj.libuplink.mobile.Config internal() {
		return config;
	}

	private Config(Builder builder) {
		this.config = new io.storj.libuplink.mobile.Config();
		this.config.setMaxInlineSize(builder.maxInlineSize);
		this.config.setMaxMemory(builder.maxMemory);
		this.tempDir = builder.tempDir;
	}

	public static class Builder {

		private long maxInlineSize;
		private long maxMemory;
		private String tempDir;

		public Builder setMaxInlineSize(long size) {
			this.maxInlineSize = size;
			return this;
		}

		public Builder setMaxMemory(long size) {
			this.maxMemory = size;
			return this;
		}

		public Builder setTempDir(String dir) {
			this.tempDir = dir;
			return this;
		}

		public Config build() {
			return new Config(this);
		}
	}
}
