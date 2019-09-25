package io.storj;

import java.util.Iterator;
import java.util.NoSuchElementException;

import io.storj.libuplink.mobile.Bucket;

class ObjectIterator implements Iterator<ObjectInfo>, Iterable<ObjectInfo> {

    private Bucket bucket;
    private ListOptions options;

    private io.storj.libuplink.mobile.ObjectList currentPage;
    private int pageIndex = 0;
    private String cursor;

    ObjectIterator(Bucket bucket, ListOptions options) throws StorjException {
        this.bucket = bucket;
        this.options = options;
        this.cursor = options.getCursor();

        nextPage();
    }

    @Override
    public boolean hasNext() {
        while (pageIndex >= currentPage.length()) {
            checkPageIndexOutOfBounds(pageIndex, currentPage.length());
            if (!currentPage.more()) {
                return false;
            }
            try {
                nextPage();
            } catch (StorjException e) {
                throw new RuntimeException(e);
            }
        }
        return true;
    }

    @Override
    public ObjectInfo next() {
        try {
            while (pageIndex >= currentPage.length()) {
                checkPageIndexOutOfBounds(pageIndex, currentPage.length());
                if (currentPage.more()) {
                    nextPage();
                } else {
                    throw new NoSuchElementException();
                }
            }
            io.storj.libuplink.mobile.ObjectInfo info = currentPage.item(pageIndex++);
            return new ObjectInfo(info);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void nextPage() throws StorjException {
        try {
            if (currentPage != null) {
                cursor = currentPage.item(currentPage.length() - 1).getPath();
            }
            currentPage = bucket.listObjects(new ListOptions.Builder().
                    setPrefix(options.getPrefix()).
                    setCursor(cursor).
                    setDelimiter(options.getDelimiter()).
                    setRecursive(options.isRecursive()).
                    setDirection(ListDirection.AFTER).
                    setPageSize(options.getPageSize()).
                    build().internal());
            pageIndex = 0;
        } catch (Exception e) {
            throw ExceptionUtil.toStorjException(e);
        }
    }

    private void checkPageIndexOutOfBounds(int pageIndex, long pageLength) {
        if (pageIndex > pageLength) {
            throw new IllegalStateException(String.format("page index (%d) is greater than page length (%d)", pageIndex, pageLength));
        }
    }

    @Override
    public Iterator<ObjectInfo> iterator() {
        return this;
    }
}
