# android-libuplink
Android bindings to Storj V3 libuplink.

## Requirements

* Android 5.0 Lollipop or newer

## Setup

Add the Gradle dependency to the `build.gradle` file of the app module:

```Gradle
dependencies {
    compile 'io.storj:libuplink-android:0.8'
}
```

## Usage

```java
    Scope scope = Scope.parse("13GRuHAWnY...");

    try (Uplink uplink = new Uplink(); Project project = uplink.openProject(scope)) {
        project.createBucket("my-bucket");

        try (Bucket bucket = project.openBucket("my-bucket", scope)) {
            String objectPath = "object/path";
            byte[] data = "File content".getBytes();
            bucket.uploadObject(objectPath, data);

            ByteArrayOutputStream writer = new ByteArrayOutputStream();
            bucket.downloadObject(objectPath, writer);

            bucket.deleteObject(objectPath);
        }

        project.deleteBucket("my-bucket");
    }
```
Bindings Javdoc can be found [here](https://storj.github.io/android-libuplink/javadoc/0.8/).