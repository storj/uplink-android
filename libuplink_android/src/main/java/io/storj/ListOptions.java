package io.storj;

import android.support.annotation.Nullable;

import java.util.List;

public class ListOptions {

	private io.storj.libuplink.mobile.ListOptions options;

	public String getPrefix() {
		return options.getPrefix();
	}

	public String getCursor() {
		return options.getCursor();
	}

	public char getDelimiter() {
		return (char) options.getDelimiter();
	}

	public boolean isRecursive() {
		return options.getRecursive();
	}

	public ListDirection getDirection() {
		return ListDirection.fromValue((byte) options.getDirection());
	}

	public int getPageSize() {
		return (int) options.getLimit();
	}

	@Override
	public boolean equals(@Nullable Object obj) {
		if (!(obj instanceof ListOptions)) {
			return false;
		}
		ListOptions that = (ListOptions) obj;
		return options.equals(that.options);
	}

	@Override
	public int hashCode() {
		return options.hashCode();
	}

	io.storj.libuplink.mobile.ListOptions internal() {
		return options;
	}

	private ListOptions(Builder builder) {
		this.options = new io.storj.libuplink.mobile.ListOptions();
		this.options.setPrefix(builder.prefix);
		this.options.setCursor(builder.cursor);
		this.options.setDelimiter(builder.delimiter);
		this.options.setRecursive(builder.recursive);
		if (builder.direction != null) {
			this.options.setDirection(builder.direction.getValue());
		}
		this.options.setLimit(builder.pageSize);
	}

	public static class Builder {

		private String prefix;
		private String cursor;
		private char delimiter;
		private boolean recursive;
		private ListDirection direction;
		private int pageSize;

		public Builder setPrefix(String prefix) {
			this.prefix = prefix;
			return this;
		}

		public Builder setCursor(String cursor) {
			this.cursor = cursor;
			return this;
		}

		public Builder setDelimiter(char delimiter) {
			this.delimiter = delimiter;
			return this;
		}

		public Builder setRecursive(boolean recursive) {
			this.recursive = recursive;
			return this;
		}

		public Builder setDirection(ListDirection direction) {
			this.direction = direction;
			return this;
		}

		public Builder setPageSize(int pageSize) {
			this.pageSize = pageSize;
			return this;
		}

		public ListOptions build() {
			return new ListOptions(this);
		}
	}
}
