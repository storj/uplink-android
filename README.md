# android-libuplink

[![Storj.io](https://storj.io/img/storj-badge.svg)](https://storj.io)
[![Javadocs](https://img.shields.io/badge/javadoc-0.8.0-blue.svg)](https://storj.github.io/android-libuplink/javadoc/0.8.0/)

Android bindings to Storj V3 libuplink.

## Requirements

* Android 5.0 Lollipop or newer

## Setup

Add the Gradle dependency to the `build.gradle` file of the app module:

```Gradle
dependencies {
    implementation 'io.storj:libuplink-android:0.8.0'
}
```

## Usage

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
String serializedScope = "13GRuHAW..."
Scope scope = Scope.parse(serializedScope);
```

---------------

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
