# android-libuplink

[![Storj.io](https://storj.io/img/storj-badge.svg)](https://storj.io)
[![Javadocs](https://img.shields.io/badge/javadoc-0.12.0-blue.svg)](https://storj.github.io/uplink-android/javadoc/0.12.0/)

Android bindings to Storj V3 libuplink.

## Requirements

* Android 5.0 Lollipop or newer

## Setup

Add the Gradle dependency to the `build.gradle` file of the app module:

```Gradle
dependencies {
    implementation 'io.storj:uplink-android:1.0.0'
}
```

## Usage

+ [Access Grant](#access-grant)
  - [Creating new access grant from passphrase](#creating-new-access-grant-from-passphrase)
  - [Sharing access grant](#sharing-access-grant)
  - [Serializing access grant to string](#serializing-access-grant-to-string)
  - [Parsing serialized access grant from string](#parsing-serialized-access-grant-from-string)
+ [Buckets](#buckets)
  - [Creating new bucket](#creating-new-bucket)
  - [Getting info about a bucket](#getting-info-about-a-bucket)
  - [Listing buckets](#listing-buckets)
  - [Deleting a bucket](#deleting-a-bucket)
+ [Objects](#objects)
  - [Downloading an object](#downloading-an-object)
  - [Downloading a range of an object](#downloading-a-range-of-an-object)
  - [Downloading an object with progress monitoring and cancellation](#downloading-an-object-with-progress-monitoring-and-cancellation)
  - [Uploading new object](#uploading-new-object)
  - [Uploading new object with progress monitoring and cancellation](#uploading-new-object-with-progress-monitoring-and-cancellation)
  - [Listing objects](#listing-objects)
  - [Deleting an object](#deleting-an-object)
+ [Sharing content](#sharing-content)

### Access Grant

The `Access Grant` contains all information required to access resources on the Storj network:
- Satellite Address where metadata is stored
- API Key for accessing a project on the satellite
- Encryption Access for decrypting the content

#### Creating new access grant from passphrase

New `Access Grant` can be requested from satellite with Satellite Address, API Key and passphrase.

```java
String satelliteAddress = "us-central-1.tardigrade.io:7777";
String serializedApiKey = "13Yqft7v...";
String passphrase = "super secret passphrase";

Uplink uplink = new Uplink();
Access access = uplink.requestAccessWithPassphrase(satelliteAddress, serializedApiKey, passphrase);
```

#### Sharing access grant

`Access Grant` can be shared in terms of allowed operations and access to specific buckets and objects.

```java
Permission permission = new Permission.Builder().allowDownload().allowList().build();
access.share(permission, new SharePrefix("my-bucket", "pictures/birthday/"));
```

#### Serializing access grant to string

`Access Grant` can be serialized to string for easy sharing.

```java
Access access = ...;
String serializedAccess = access.serialize(); 
```

#### Parsing serialized access grant from string

If received a serialized `Access Grant` as a string, it can be parsed to `Access` object.

```java
String serializedAccess = "13GRuHAW...";
Access access = Access.parse(serializedAccess);
```

### Buckets

#### Creating new bucket

```java
Access access = ...;
Uplink uplink = new Uplink();
try (Project project = uplink.openProject(access)) {
    project.createBucket("my-bucket");
}
```

#### Getting info about a bucket

```java
Access access = ...;
Uplink uplink = new Uplink();
try (Project project = uplink.openProject(access)) {
    BucketInfo info = project.statBucket("my-bucket"));
}
```

#### Listing buckets

```java
Access access = ...;
Uplink uplink = new Uplink();
try (Project project = uplink.openProject(access);
        BucketIterator iterator = project.listBuckets()) {
    for (BucketInfo info : iterator) {
        // Do something for each bucket.
    }
}
```

#### Deleting a bucket

```java
Access access = ...;
Uplink uplink = new Uplink();
try (Project project = uplink.openProject(access)) {
    BucketInfo info = project.deleteBucket("my-bucket"));
}
```

### Objects

#### Downloading an object

Below is the easiest way for downloading a complete object to a local file.

```java
Access access = ...;
Uplink uplink = new Uplink();
try (Project project = uplink.openProject(access);
        InputStream in = project.downloadObject("my-bucket", "key/to/my/object");
        OutputStream out = new FileOutputStream("path/to/local/file")) {
    byte[] buffer = new byte[8 * 1024];
    int bytesRead;
    while ((bytesRead = in.read(buffer)) != -1) {
        out.write(buffer, 0, bytesRead);
    }
}
```

#### Downloading a range of an object

If only a portion of the object should be downloaded, this can be specified with download options. The example below will download only 4 KiB from the object, starting at 1 KiB offset.

```java
Access access = ...;
Uplink uplink = new Uplink();
ObjectDownloadOption[] options = new ObjectDownloadOption[]{
        ObjectDownloadOption.offset(1024),
        ObjectDownloadOption.length(4096)
};
try (Project project = uplink.openProject(access);
        InputStream in = project.downloadObject("my-bucket", "key/to/my/object",
                options);
        OutputStream out = new FileOutputStream("path/to/local/file")) {
    byte[] buffer = new byte[8 * 1024];
    int bytesRead;
    while ((bytesRead = in.read(buffer)) != -1) {
        out.write(buffer, 0, bytesRead);
    }
}
```

#### Downloading an object with progress monitoring and cancellation

If progress monitoring and/or cancellation is important, the client can take advantage of the `ObjectInputStream` class.

As all operations in the Storj Java API are blocking, the client should use some means for asynchronous processing - like the `AsyncTask` from the Android platform.

The example below shows how to download with progress monitoring and cancellation using the `AsyncTask`:

```java
public class DownloadTask extends AsyncTask<Void, Long, Throwable> {

    private Access mAccess;
    private String mBucket;
    private ObjectInfo mFile;
    private File mLocalFile;

    private int mNotificationId;
    private long mDownloadedBytes;
    private long mLastNotifiedTime;

    DownloadTask(Access access, String bucket, ObjectInfo file, File localFile) {
        mAccess = access;
        mBucket = bucket;
        mFile = file;
        mLocalFile = localFile;
    }

    @Override
    protected Exception doInBackground(Void... params) {
        Uplink uplink = new Uplink();
        try (Project project = uplink.openProject(mAccess);
             ObjectInputStream in = project.downloadObject(mBucket, mFile.getKey());
             OutputStream out = new FileOutputStream(mLocalFile)) {
            byte[] buffer = new byte[128 * 1024];
            int len;
            while ((len = in.read(buffer)) != -1) {
                if (isCancelled()) {
                    return null;
                }
                out.write(buffer, 0, len);
                if (isCancelled()) {
                    return null;
                }
                publishProgress((long) len);
            }
        } catch (StorjException | IOException e) {
            return e;
        }

        return null;
    }

    @Override
    protected void onProgressUpdate(Long... params) {
        long increment = params[0];
        mDownloadedBytes += increment;

        long now = System.currentTimeMillis();

        // Calculate the progress in percents.
        int progress = (int) ((mDownloadedBytes * 100) / mFile.getSize());

        // Check if 1 second elapsed since last notification or progress is at 100%.
        if (progress == 100 || mLastNotifiedTime == 0 || now > mLastNotifiedTime + 1150) {
            // Place your code here to update the GUI with the new progress.
            mLastNotifiedTime = now;
        }
    }

    @Override
    protected void onPostExecute(Throwable t) {
        if (t != null) {
            String errorMessage = t.getMessage();
            // The download failed.
            // Place your code here to update the GUI with the error message.
            return;
        }

        // The download is successful.
        // Place your code here to update the GUI.
    }

    protected void onCancelled(Throwable t) {
        // The download was cancelled.
        // Place your code here to update the GUI.
    }

    /**
     * Call this method to cancel the download.
     */
    void cancel() {
        this.cancel(false);
    }
}
```

#### Uploading new object

Below is the easiest way for uploading new object.

Note the importance of specifying the location of the temp directory using the `UplinkOption.tempDir()` option. This is where the file being uploaded will be first encrypted before actually uploaded into pieces to the Storj network. For Android, it is recommended to set the temp directory to the application's cache directory.

```java
Access access = ...;
String tempDir = mActivity.getCacheDir().getPath();
Uplink uplink = new Uplink(UplinkOption.tempDir(tempDir));
try (Project project = uplink.openProject(access);
        OutputStream out = project.uploadObject("my-bucket", "key/to/my/object");
        InputStream in = new FileInputStream("path/to/local/file")) {
    byte[] buffer = new byte[8 * 1024];
    int bytesRead;
    while ((bytesRead = in.read(buffer)) != -1) {
        out.write(buffer, 0, bytesRead);
    }
}
```

#### Uploading new object with progress monitoring and cancellation

If progress monitoring and/or cancellation is important, the client can take advantage of the `ObjectOutputStream` class.

As all operations in the Storj Java API are blocking, the client should use some means for asynchronous processing - like the `AsyncTask` from the Android platform.

The example below shows how to upload with progress monitoring and cancellation using the `AsyncTask`.

Note the importance of specifying the location of the temp directory using the `UplinkOption.tempDir()` option. This is where the file being uploaded will be first encrypted before actually uploaded into pieces to the Storj network. For Android, it is recommended to set the temp directory to the application's cache directory.

```java
public class UploadTask extends AsyncTask<Void, Long, Throwable> {

    private Scope mScope;
    private String mBucket;
    private String objectPath;
    private File mFile;
    private String mTempDir;

    private long mFileSize;
    private long mUploadedBytes;
    private long mLastNotifiedTime;

    private ObjectOutputStream mOutputStream;

    UploadTask(Scope scope, String bucket, String objectPath, File file, String tempDir) {
        mScope = scope;
        mBucket = bucket;
        mObjectPath = objectPath;
        mFile = file;
        mTempDir = tempDir;
        mFileSize = mFile.length();
    }

    @Override
    protected Exception doInBackground(Void... params) {
        try (Uplink uplink = new Uplink(UplinkOption.tempDir(mTempDir));
             Project project = uplink.openProject(mScope);
             Bucket bucket = project.openBucket(mBucket, mScope);
             InputStream in = new FileInputStream(mFile.getPath());
             ObjectOutputStream out = new ObjectOutputStream(bucket, mObjectPath)) {
            mOutputStream = out;
            byte[] buffer = new byte[128 * 1024];
            int len;
            while ((len = in.read(buffer)) != -1) {
                if (isCancelled()) {
                    out.cancel();
                    return null;
                }
                out.write(buffer, 0, len);
                if (isCancelled()) {
                    out.cancel();
                    return null;
                }
                publishProgress((long) len);
            }
        } catch (StorjException | IOException e) {
            return e;
        }

        return null;
    }

    @Override
    protected void onProgressUpdate(Long... params) {
        long increment = params[0];
        mUploadedBytes += increment;

        long now = System.currentTimeMillis();

        // Calculate the progress in percents.
        int progress = (int) ((mUploadedBytes * 100) / mFileSize);

        // Check if 1 second elapsed since last notification or progress is at 100%.
        if (progress == 100 || mLastNotifiedTime == 0 || now > mLastNotifiedTime + 1150) {
            // Place your code here to update the GUI with the new progress.
            mLastNotifiedTime = now;
        }
    }

    @Override
    protected void onPostExecute(Throwable t) {
        if (t != null) {
            String errorMessage = t.getMessage();
            // The upload failed.
            // Place your code here to update the GUI with the error message.
            return;
        }

        // The upload is successful.
        // Place your code here to update the GUI.
    }

    protected void onCancelled(Throwable t) {
        // The upload was cancelled.
        // Place your code here to update the GUI.
    }

    /**
     * Call this method to cancel the upload.
     */
    void cancel() {
        this.cancel(false);
        mOutputStream.cancel();
    }
}
```

#### Listing objects

Listing the content of a bucket, non-recursive:

```java
Access access = ...;
Uplink uplink = new Uplink();
try (Project project = uplink.openProject(access)) {
    Iterable<ObjectInfo> objects = bucket.listObjects();
    for (ObjectInfo object : objects) {
        // Do something for each object.
    }
}
```

Listing all content under specific prefix, recursive:

```java
Access access = ...;
Uplink uplink = new Uplink();
try (Project project = uplink.openProject(access);
        ObjectIterator objects = project.listObjects("my-bucket",
                ObjectListOption.prefix("some/key/prefix"), ObjectListOption.recursive())) {
    for (ObjectInfo object : objects) {
        // Do something for each object.
    }
}
```

#### Deleting an object

```java
Access access = ...;
Uplink uplink = new Uplink();
try (Project project = uplink.openProject(access)) {
    project.deleteObject("my-bucket", "key/to/my/object"));
}
```

### Sharing content

Sharing content on the Storj network is achieved by sharing the following pieces of information to the receiving party:

1. A serialized shared `Access` Grant to access the shared content.
1. The bucket name containing the shared content.
1. The object key or prefix to the share content.

Below is an example for uploading a file and preparing the restricted `Access` Grant:

```java
Access access = ...;
String tempDir = mActivity.getCacheDir().getPath();
Uplink uplink = new Uplink(UplinkOption.tempDir(tempDir));
try (Project project = uplink.openProject(access);
        OutputStream out = project.uploadObject("my-bucket", "key/to/my/object");
        InputStream in = new FileInputStream("path/to/local/file")) {
    byte[] buffer = new byte[8 * 1024];
    int bytesRead;
    while ((bytesRead = in.read(buffer)) != -1) {
        out.write(buffer, 0, bytesRead);
    }
}

// Share a read-only access grant to "my-bucket/key/to/my" prefix.
// It is possible to share the access grant only to "my-bucket/key/to/my/file" object too.
Scope sharedAccess = access.share(
        new Permission.Builder().allowDownload().allowList().build(),
        new SharePrefix("my-bucket", "key/to/my"));

// Serialize the access grant to, so it can be easily sent to the receiving party.
String serializedShare = sharedAccess.serialize();
```

The receiving party can download the shared file using the following example:

```java
// Info received by the other party
String serializedAccess = "13GRuHAW...";
String bucketName = "my-bucket";
String objectKey = "key/to/my/object";

Access access = Access.parse(serializedAccess);
try (Project project = uplink.openProject(access);
        InputStream in = project.downloadObject(bucketName, objectKey);
        OutputStream out = new FileOutputStream("path/to/local/file")) {
    byte[] buffer = new byte[8 * 1024];
    int bytesRead;
    while ((bytesRead = in.read(buffer)) != -1) {
        out.write(buffer, 0, bytesRead);
    }
}
```
