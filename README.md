# android-libuplink

[![Storj.io](https://storj.io/img/storj-badge.svg)](https://storj.io)
[![Javadocs](https://img.shields.io/badge/javadoc-0.12.0-blue.svg)](https://storj.github.io/android-libuplink/javadoc/0.12.0/)

Android bindings to Storj V3 libuplink.

## Requirements

* Android 5.0 Lollipop or newer

## Setup

Add the Gradle dependency to the `build.gradle` file of the app module:

```Gradle
dependencies {
    implementation 'io.storj:libuplink-android:0.12.0'
}
```

## Usage

+ [API Key](#api-key)
  - [Parsing serialized API key from string](#parsing-serialized-api-key-from-string)
+ [Scope](#scope)
  - [Creating new scope from passphrase](#creating-new-scope-from-passphrase)
  - [Resticting access of scope](#resticting-access-of-scope)
  - [Serializing scope to string](#serializing-scope-to-string)
  - [Parsing serialized scope from string](#parsing-serialized-scope-from-string)
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

### API Key

The `ApiKey` contains the access key to a project on the satellite.

#### Parsing serialized API key from string

API keys are obtained from the satellite Web interface as a serialized string. They must be parsed to `ApiKey` object.

```java
String serializedApiKey = "13Yqft7v...";
ApiKey apiKey = ApiKey.parse(serializedApiKey);
```

### Scope

The `Scope` contains all information required to access resources on the Storj network:
- Satellite Address where metadata is stored
- API Key for accessing a project on the satellite
- Encryption Access for decrypting the content

#### Creating new scope from passphrase

New `Scope` can be created from Satellite Address, API Key and passphrase.

```java
String satelliteAddress = "us-central-1.tardigrade.io:7777";
String serializedApiKey = "13Yqft7v...";
String passphrase = "super secret passphrase";

ApiKey apiKey = ApiKey.parse(serializedApiKey);
try (Uplink uplink = new Uplink();
     Project project = uplink.openProject(satelliteAddress, apiKey)) {
    Key saltedKey = Key.getSaltedKeyFromPassphrase(project, passphrase);
    EncryptionAccess access = new EncryptionAccess(saltedKey);
    Scope scope = new Scope(satelliteAddress, apiKey, access);
}
```

#### Resticting access of scope

Scopes can be restricted in terms of allowed operations and access to specific buckets and objects.

```java
Scope sharedScope = SCOPE.restrict(
        new Caveat.Builder().disallowDeletes(true).disallowWrites(true).build(),
        new EncryptionRestriction("my-bucket", "pictures/birthday/"));
```

#### Serializing scope to string

Scopes can be serialized to string for easy sharing.

```java
Scope scope = ...;
String serializedScope = scope.serialize(); 
```

#### Parsing serialized scope from string

If received a serialized Scope as a string, it can be parsed to `Scope` object.

```java
String serializedScope = "13GRuHAW...";
Scope scope = Scope.parse(serializedScope);
```

### Buckets

#### Creating new bucket

```java
Scope scope = ...;
try (Uplink uplink = new Uplink();
     Project project = uplink.openProject(scope)) {
    project.createBucket("my-bucket"));
}
```

#### Getting info about a bucket

```java
Scope scope = ...;
try (Uplink uplink = new Uplink();
     Project project = uplink.openProject(scope)) {
    BucketInfo info = project.getBucketInfo("my-bucket"));
}
```

#### Listing buckets

```java
Scope scope = ...;
try (Uplink uplink = new Uplink();
     Project project = uplink.openProject(scope)) {
    Iterable<BucketInfo> buckets = project.listBucket();
    for (BucketInfo bucket : buckets) {
        // Do something for each bucket.
    }
}
```

#### Deleting a bucket

```java
Scope scope = ...;
try (Uplink uplink = new Uplink();
     Project project = uplink.openProject(scope)) {
    project.deleteBucket("my-bucket"));
}
```

### Objects

#### Downloading an object

Below is the easiest way for downloading a complete object to a local file. `bucket.downloadObject` is a blocking operation. It will return when the download is complete.

```java
Scope scope = ...;
try (Uplink uplink = new Uplink();
     Project project = uplink.openProject(scope);
     Bucket bucket = project.openBucket("my-bucket", scope);
     OutputStream out = new FileOutputStream("path/to/local/file")) {
    bucket.downloadObject("path/to/my/object", out);
}
```

#### Downloading a range of an object

If only a portion of the object should be downloaded, this can be specified with the `off` and `len` parameters. The example below will download only 4 KiB from the object, starting at 1 KiB offset.

```java
Scope scope = ...;
long off = 1024;
long len = 4096;
try (Uplink uplink = new Uplink();
     Project project = uplink.openProject(scope);
     Bucket bucket = project.openBucket("my-bucket", scope);
     OutputStream out = new FileOutputStream("path/to/local/file")) {
    bucket.downloadObject("path/to/my/object", out, off, len);
}
```

#### Downloading an object with progress monitoring and cancellation

If progress monitoring and/or cancellation is important, the client can take advantage of the `ObjectInputStream` class.

As all operations in the Storj Java API are blocking, the client should use some means for asynchronous processing - like the `AsyncTask` from the Android platform.

The example below shows how to download with progress monitoring and cancellation using the `AsyncTask`:

```java
public class DownloadTask extends AsyncTask<Void, Long, Throwable> {

    private Scope mScope;
    private ObjectInfo mFile;
    private File mLocalFile;

    private int mNotificationId;
    private long mDownloadedBytes;
    private long mLastNotifiedTime;

    private ObjectInputStream mInputStream;

    DownloadTask(Scope scope, ObjectInfo file, File localFile) {
        mScope scope;
        mFile = file;
        mLocalFile = localFile;
    }

    @Override
    protected Exception doInBackground(Void... params) {
        try (Uplink uplink = new Uplink();
             Project project = uplink.openProject(mScope);
             Bucket bucket = project.openBucket(mFile.getBucket(), mScope);
             ObjectInputStream in = new ObjectInputStream(bucket, mFile.getPath());
             OutputStream out = new FileOutputStream(mLocalFile)) {
            mInputStream = in;
            byte[] buffer = new byte[128 * 1024];
            int len;
            while ((len = in.read(buffer)) != -1) {
                if (isCancelled()) {
                    in.cancel();
                    return null;
                }
                out.write(buffer, 0, len);
                if (isCancelled()) {
                    in.cancel();
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
        mInputStream.cancel();
    }
}
```

#### Uploading new object

Below is the easiest way for uploading new object. `bucket.uploadObject` is a blocking operation. It will return when the upload is complete.

Note the importance of specifying the location of the temp directory using the `UplinkOption.tempDir()` option. This is where the file being uploaded will be first encrypted before actually uploaded into pieces to the Storj network. For Android, it is recommended to set the temp directory to the application's cache directory.

```java
Scope scope = ...;
String tempDir = mActivity.getCacheDir().getPath();
try (Uplink uplink = new Uplink(UplinkOption.tempDir(tempDir));
     Project project = uplink.openProject(scope);
     Bucket bucket = project.openBucket("my-bucket", scope);
     InputStream in = new FileInputStream("path/to/local/file")) {
    bucket.uploadObject("path/to/my/object", in);
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
Scope scope = ...;
try (Uplink uplink = new Uplink();
     Project project = uplink.openProject(scope);
     Bucket bucket = project.openBucket("my-bucket", scope)) {
    Iterable<ObjectInfo> objects = bucket.listObjects();
    for (ObjectInfo object : objects) {
        // Do something for each object.
    }
}
```

Listing all content under specific prefix, recursive:

```java
Scope scope = ...;
try (Uplink uplink = new Uplink();
     Project project = uplink.openProject(scope);
     Bucket bucket = project.openBucket("my-bucket", scope)) {
    Iterable<ObjectInfo> objects = bucket.listObjects(
        ObjectListOption.prefix("some/path/prefix"), ObjectListOption.recursive(true));
    for (ObjectInfo object : objects) {
        // Do something for each object.
    }
}
```

#### Deleting an object

```java
Scope scope = ...;
try (Uplink uplink = new Uplink();
     Project project = uplink.openProject(scope);
     Bucket bucket = project.openBucket("my-bucket", scope)) {
    bucket.deleteObject("path/to/my/object"));
}
```

### Sharing content

Sharing content on the Storj network is achieved by sharing the following pieces of information to the receiving party:

1. A serialized restricted `Scope` to access the shared content.
1. The bucket name containing the shared content.
1. The object path or prefix to the share content.

Below is an example for uploading a file and preparing the restricted `Scope`:

```java
Scope scope = ...;
String tempDir = mActivity.getCacheDir().getPath();
try (Uplink uplink = new Uplink(UplinkOption.tempDir(tempDir));
     Project project = uplink.openProject(scope);
     Bucket bucket = project.openBucket("my-bucket", scope);
     InputStream in = new FileInputStream("path/to/local/file")) {
    bucket.uploadObject("path/to/my/object", in);
}

// Share a read-only scope to "my-bucket/path/to/my" folder.
// It is possible to restrict the scope only to "my-bucket/path/to/my/file" object too.
Scope sharedScope = scope.restrict(
        new Caveat.Builder().disallowDeletes(true).disallowWrites(true).build(),
        new EncryptionRestriction("my-bucket", "path/to/my"));

// Serialize the scope to, so it can be easily sent to the receiving party.
String serializedShare = sharedScope.serialize();
```

The receiving party can download the shared file using the following example:

```java
// Info received by the other party
String serializedScope = "13GRuHAW...";
String bucketName = "my-bucket";
String objectPath = "path/to/my/object";

Scope scope = Scope.parse(serializedScope);
try (Uplink uplink = new Uplink();
     Project project = uplink.openProject(scope);
     Bucket bucket = project.openBucket(bucketName, scope);
     OutputStream out = new FileOutputStream("path/to/local/file")) {
    bucket.downloadObject(objectPath, out);
}
```
