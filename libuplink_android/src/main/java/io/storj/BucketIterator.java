package io.storj;

import java.util.Iterator;
import java.util.NoSuchElementException;

import io.storj.libuplink.mobile.Project;

class BucketIterator implements Iterator<BucketInfo>, Iterable<BucketInfo> {

    private io.storj.libuplink.mobile.Project project;
    private BucketListOptions options;

    private io.storj.libuplink.mobile.BucketList currentPage;
    private int pageIndex = 0;

    BucketIterator(Project project, BucketListOption... options) throws StorjException {
        this.project = project;
        this.options = BucketListOption.internal(options);

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
    public BucketInfo next() {
        try {
            while (pageIndex >= currentPage.length()) {
                checkPageIndexOutOfBounds(pageIndex, currentPage.length());
                if (currentPage.more()) {
                    nextPage();
                } else {
                    throw new NoSuchElementException();
                }
            }
            io.storj.libuplink.mobile.BucketInfo info = currentPage.item(pageIndex++);
            return new BucketInfo(info);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void nextPage() throws StorjException {
        try {
            String cursor = this.options.cursor;
            if (currentPage != null) {
                cursor = currentPage.item(currentPage.length() - 1).getName();
            }
            currentPage = project.listBuckets(cursor, this.options.pageSize);
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
    public Iterator<BucketInfo> iterator() {
        return this;
    }
}
