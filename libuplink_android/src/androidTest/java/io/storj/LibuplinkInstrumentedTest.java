package io.storj;

import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
public class LibuplinkInstrumentedTest {

    public static final String VALID_SATELLITE_ADDRESS = InstrumentationRegistry.getArguments().getString("storj.sim.host", "172.19.48.151:10000");
    public static final String VALID_API_KEY = InstrumentationRegistry.getArguments().getString("api.key", "13Yqed8J5EKXUkJV8qbaxcoWbkXsqBREXEMv48fFMjZs5GY5gmjynfDsjs9YhwsSBLc9eWfd7riYcsAvimTpLKqp2npGX7NpFUj4wiH");

    String filesDir;
    UplinkOption[] uplinkOptions;

    @Before
    public void setUp() {
        filesDir = InstrumentationRegistry.getTargetContext().getFilesDir().getAbsolutePath();
        uplinkOptions = new UplinkOption[]{
                UplinkOption.tempDir(filesDir),
                UplinkOption.skipPeerCAWhitelist(true)
        };
    }

    @Test
    public void testBasicBucket() throws Exception {
        try (Uplink uplink = new Uplink(uplinkOptions)) {
            ApiKey apiKey = new ApiKey(VALID_API_KEY);

            try (Project project = uplink.openProject(VALID_SATELLITE_ADDRESS, apiKey)) {
                String expectedBucket = "test-bucket";
                RedundancyScheme rs = new RedundancyScheme.Builder().
                        setRequiredShares((short) 4).
                        setRepairShares((short) 6).
                        setSuccessShares((short) 8).
                        setTotalShares((short) 10).
                        build();

                try {
                    project.createBucket(expectedBucket, BucketOption.redundancyScheme(rs));

                    BucketInfo bucketInfo = project.getBucketInfo(expectedBucket);
                    Assert.assertEquals(expectedBucket, bucketInfo.getName());
                } finally {
                    project.deleteBucket(expectedBucket);

                    try {
                        project.getBucketInfo(expectedBucket);
                    } catch (StorjException e) {
                        Assert.assertTrue(e.getMessage().contains("bucket not found"));
                    }
                }
            }
        }
    }

    @Test
    public void testListBuckets() throws Exception {
        try (Uplink uplink = new Uplink(uplinkOptions)) {
            ApiKey apiKey = new ApiKey(VALID_API_KEY);

            try (Project project = uplink.openProject(VALID_SATELLITE_ADDRESS, apiKey)) {
                RedundancyScheme rs = new RedundancyScheme.Builder().
                        setRequiredShares((short) 4).
                        setRepairShares((short) 6).
                        setSuccessShares((short) 8).
                        setTotalShares((short) 10).
                        build();

                Set<String> expectedBuckets = new HashSet<>();

                try {
                    for (int i = 0; i < 10; i++) {
                        String bucket = "test-bucket" + i;
                        project.createBucket(bucket, BucketOption.redundancyScheme(rs));
                        expectedBuckets.add(bucket);
                    }

                    Iterable<BucketInfo> buckets = project.listBuckets(
                            BucketListOption.cursor(""), BucketListOption.pageSize(2));
                    String allBuckets = "";
                    int numOfBuckets = 0;
                    for (BucketInfo bucket : buckets) {
                        allBuckets += bucket.getName() + "|";
                        numOfBuckets++;
                    }

                    assertEquals(allBuckets, expectedBuckets.size(), numOfBuckets);
                } finally {
                    for (int i = 0; i < 10; i++) {
                        String bucket = "test-bucket" + i;
                        project.deleteBucket(bucket);
                    }

                    Iterable<BucketInfo> buckets = project.listBuckets(BucketListOption.pageSize(1));
                    assertEquals(false, buckets.iterator().hasNext());
                }
            }
        }
    }

    @Test
    public void testUploadDownloadInline() throws Exception {
        try (Uplink uplink = new Uplink(uplinkOptions)) {
            ApiKey apiKey = new ApiKey(VALID_API_KEY);

            try (Project project = uplink.openProject(VALID_SATELLITE_ADDRESS, apiKey)) {
                String expectedBucket = "test-bucket";
                RedundancyScheme rs = new RedundancyScheme.Builder().
                        setRequiredShares((short) 4).
                        setRepairShares((short) 6).
                        setSuccessShares((short) 8).
                        setTotalShares((short) 10).
                        build();

                project.createBucket(expectedBucket, BucketOption.redundancyScheme(rs));

                EncryptionAccess access = new EncryptionAccess();
                access.setDefaultKey(new Key("TestEncryptionKey"));

                try (Bucket bucket = project.openBucket(expectedBucket, access)) {
                    byte[] expectedData = new byte[2048];
                    Random random = new Random();
                    random.nextBytes(expectedData);

                    String objectPath = "object/path";
                    {
                        try (OutputStream oos = new ObjectOutputStream(bucket, objectPath)) {
                            oos.write(expectedData);
                        }
                    }

                    {
                        try (InputStream is = new ObjectInputStream(bucket, objectPath)) {
                            BufferedInputStream bis = new BufferedInputStream(is);

                            ByteArrayOutputStream writer = new ByteArrayOutputStream();
                            byte[] buf = new byte[256];
                            int read;
                            while ((read = bis.read(buf)) != -1) {
                                writer.write(buf, 0, read);
                            }
                            assertArrayEquals(expectedData, writer.toByteArray());
                        }
                    }

                    bucket.deleteObject(objectPath);
                }

                project.deleteBucket(expectedBucket);
            }
        }
    }


    @Test
    public void testUploadDownloadDeleteRemote() throws Exception {
        try (Uplink uplink = new Uplink(uplinkOptions)) {
            ApiKey apiKey = new ApiKey(VALID_API_KEY);

            try (Project project = uplink.openProject(VALID_SATELLITE_ADDRESS, apiKey)) {
                String expectedBucket = "test-bucket";
                RedundancyScheme rs = new RedundancyScheme.Builder().
                        setRequiredShares((short) 4).
                        setRepairShares((short) 6).
                        setSuccessShares((short) 8).
                        setTotalShares((short) 10).
                        build();

                project.createBucket(expectedBucket, BucketOption.redundancyScheme(rs));

                EncryptionAccess access = new EncryptionAccess();
                access.setDefaultKey(new Key("TestEncryptionKey"));

                try (Bucket bucket = project.openBucket(expectedBucket, access)) {
                    byte[] expectedData = new byte[2 * 1024 * 1024];
                    Random random = new Random();
                    random.nextBytes(expectedData);

                    String objectPath = "object/path";
                    try {
                        try (OutputStream oos = new ObjectOutputStream(bucket, objectPath)) {
                            oos.write(expectedData);
                        }

                        try (InputStream is = new ObjectInputStream(bucket, objectPath)) {
                            BufferedInputStream bis = new BufferedInputStream(is);

                            ByteArrayOutputStream writer = new ByteArrayOutputStream();
                            byte[] buf = new byte[512];
                            int read;
                            while ((read = bis.read(buf)) != -1) {
                                writer.write(buf, 0, read);
                            }
                            assertArrayEquals(expectedData, writer.toByteArray());
                        }
                    } finally {
                        bucket.deleteObject(objectPath);

                        try {
                            bucket.deleteObject(objectPath);
                        } catch (StorjException e) {
                            assertTrue(e.getMessage(), e.getMessage().contains("not found"));
                        }
                    }
                } finally {
                    project.deleteBucket(expectedBucket);
                }
            }
        }
    }

    @Test
    public void testListObjects() throws Exception {
        try (Uplink uplink = new Uplink(uplinkOptions)) {
            ApiKey apiKey = new ApiKey(VALID_API_KEY);

            try (Project project = uplink.openProject(VALID_SATELLITE_ADDRESS, apiKey)) {
                String expectedBucket = "test-bucket";
                RedundancyScheme rs = new RedundancyScheme.Builder().
                        setRequiredShares((short) 4).
                        setRepairShares((short) 6).
                        setSuccessShares((short) 8).
                        setTotalShares((short) 10).
                        build();

                project.createBucket(expectedBucket,
                        BucketOption.redundancyScheme(rs), BucketOption.pathCipher(CipherSuite.NONE));

                EncryptionAccess access = new EncryptionAccess();
                access.setDefaultKey(new Key("TestEncryptionKey"));

                try (Bucket bucket = project.openBucket(expectedBucket, access)) {
                    long before = System.currentTimeMillis();

                    // TODO should 13 to see listing bug
                    int expectedObjects = 10;

                    for (int i = 0; i < expectedObjects; i++) {
                        String path = String.format("path%d", i);
                        try (OutputStream oos = new ObjectOutputStream(bucket, path)) {
                            oos.write(new byte[0]);
                        }
                    }

                    Iterable<ObjectInfo> list = bucket.listObjects(
                            ObjectListOption.recursive(true), ObjectListOption.pageSize(20));
                    int index = 0;
                    for (ObjectInfo info : list) {
                        assertEquals(expectedBucket, info.getBucket());
                        assertTrue(info.getCreated().getTime() >= before);

                        index++;
                    }
                    assertEquals(expectedObjects, index);

                    // cleanup
                    for (int i = 0; i < expectedObjects; i++) {
                        String path = String.format("path%d", i);
                        bucket.deleteObject(path);
                    }
                } finally {
                    project.deleteBucket(expectedBucket);
                }
            }
        }
    }

    @Test
    public void testFileSharing() throws Exception {
        String expectedBucket = "test-bucket";

        ApiKey apiKey = new ApiKey(VALID_API_KEY);

        RedundancyScheme rs = new RedundancyScheme.Builder().
                setRequiredShares((short) 4).
                setRepairShares((short) 6).
                setSuccessShares((short) 8).
                setTotalShares((short) 10).
                build();

        try (Uplink uplink = new Uplink(uplinkOptions)) {
            try (Project project = uplink.openProject(VALID_SATELLITE_ADDRESS, apiKey)) {
                project.createBucket(expectedBucket, BucketOption.redundancyScheme(rs));

                EncryptionAccess access = new EncryptionAccess();
                access.setDefaultKey(new Key("TestEncryptionKey"));

                try (Bucket bucket = project.openBucket(expectedBucket, access)) {
                    try (OutputStream oos = new ObjectOutputStream(bucket, "first-file")) {
                        oos.write("First file content".getBytes());
                    }
                }
            }
        }

        Caveat caveat = new Caveat.Builder()
                .disallowDeletes(true)
                .disallowWrites(true).build();

        ApiKey restrictedKey = apiKey.restrict(caveat);

        try (Uplink uplink = new Uplink(uplinkOptions)) {
            try (Project project = uplink.openProject(VALID_SATELLITE_ADDRESS, restrictedKey)) {

                EncryptionAccess access = new EncryptionAccess();
                access.setDefaultKey(new Key("TestEncryptionKey"));

                try (Bucket bucket = project.openBucket(expectedBucket, access)) {
                    // Try to download first-file - should succeed
                    try (InputStream is = new ObjectInputStream(bucket, "first-file")) {
                        BufferedInputStream bis = new BufferedInputStream(is);

                        ByteArrayOutputStream writer = new ByteArrayOutputStream();
                        byte[] buf = new byte[512];
                        int read;
                        while ((read = bis.read(buf)) != -1) {
                            writer.write(buf, 0, read);
                        }
                        assertArrayEquals("First file content".getBytes(), writer.toByteArray());
                    }

                    // Try to upload new file - should fail
                    String errorMessage = "";
                    try (OutputStream oos = new ObjectOutputStream(bucket, "third-file")) {
                        oos.write("Third file content".getBytes());
                    } catch (IOException e) {
                        errorMessage = e.getMessage();
                    }
                    assertTrue(errorMessage, errorMessage.contains("Unauthorized API credentials"));

                    // Try to delete first-file - should fail
                    errorMessage = "";
                    try {
                        bucket.deleteObject("first-file");
                    } catch (StorjException e) {
                        errorMessage = e.getMessage();
                    }
                    assertTrue(errorMessage, errorMessage.contains("Unauthorized API credentials"));
                }
            }
        } finally {
            // cleanup
            try (Uplink uplink = new Uplink(uplinkOptions)) {
                try (Project project = uplink.openProject(VALID_SATELLITE_ADDRESS, apiKey)) {
                    project.deleteBucket(expectedBucket);
                }
            }
        }
    }


    @Test
    public void testEncryptionAccessFromPassphrase() throws Exception {
        try (Uplink uplink = new Uplink(uplinkOptions)) {
            try (Project project = uplink.openProject(VALID_SATELLITE_ADDRESS, new ApiKey(VALID_API_KEY))){
                Key saltedKey = project.saltedKeyFromPassphrase("some-passphrase");
                EncryptionAccess ea = new EncryptionAccess();
                ea.setDefaultKey(saltedKey);
                String serialized = ea.serialize();
                assertNotEquals("", serialized);
            }
        }
    }

    @Test
    public void testEncryptionAccessWithRoot() throws Exception {
        try (Uplink uplink = new Uplink(uplinkOptions)) {
            try (Project project = uplink.openProject(VALID_SATELLITE_ADDRESS, new ApiKey(VALID_API_KEY))){
                Key saltedKey = project.saltedKeyFromPassphrase("some-passphrase");
                EncryptionAccess ea = EncryptionAccess.withRoot("bucket", "unencryptedPath", "encryptedPath", saltedKey);
                String serialized = ea.serialize();
                assertNotEquals("", serialized);
            }
        }
    }

    @Test
    public void testApiKey() throws Exception {
        String apiKeyData = "13YqeKQiA3ANSuDu4rqX6eGs3YWox9GRi9rEUKy1HidXiNNm6a5SiE49Hk9gomHZVcQhq4eFQh8yhDgfGKg268j6vqWKEhnJjFPLqAP";
        ApiKey apiKey = new ApiKey(apiKeyData);
        String serialized = apiKey.serialize();
        assertEquals(serialized, apiKeyData);

        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MONTH, 1);

        Caveat caveat = new Caveat.Builder()
                .disallowDeletes(true)
                .disallowLists(true)
                .disallowWrites(true)
                .disallowReads(false)
                .notAfter(cal.getTime())
                .notBefore(new Date())
//                .addCaveatPath(new CaveatPath("bucket".getBytes(), "123456".getBytes()))
                .build();

        ApiKey newAPIKey = apiKey.restrict(caveat);
        assertNotEquals(apiKeyData, newAPIKey.serialize());
    }

}
