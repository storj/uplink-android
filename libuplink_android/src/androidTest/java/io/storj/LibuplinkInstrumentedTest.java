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
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Random;
import java.util.Set;


import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)
public class LibuplinkInstrumentedTest {

    public static final String VALID_SATELLITE_ADDRESS = InstrumentationRegistry.getArguments().getString("storj.sim.host", "172.19.48.151:10000");
    public static final String VALID_API_KEY = InstrumentationRegistry.getArguments().getString("api.key", "13Yqed8J5EKXUkJV8qbaxcoWbkXsqBREXEMv48fFMjZs5GY5gmjynfDsjs9YhwsSBLc9eWfd7riYcsAvimTpLKqp2npGX7NpFUj4wiH");

    String filesDir;

    @Before
    public void setUp() {
        filesDir = InstrumentationRegistry.getTargetContext().getFilesDir().getAbsolutePath();
    }

    @Test
    public void testBasicBucket() throws Exception {
        Config config = new Config.Builder().setTempDir(filesDir).build();

        try (io.storj.Uplink uplink = new io.storj.Uplink(config)) {
            io.storj.ApiKey apiKey = new io.storj.ApiKey(VALID_API_KEY);

            try (io.storj.Project project = uplink.openProject(VALID_SATELLITE_ADDRESS, apiKey)) {
                String expectedBucket = "test-bucket";
                io.storj.RedundancyScheme rs = new RedundancyScheme.Builder().
                        setRequiredShares((short) 4).
                        setRepairShares((short) 6).
                        setSuccessShares((short) 8).
                        setTotalShares((short) 10).
                        build();

                io.storj.BucketConfig bucketConfig = new BucketConfig.Builder()
                        .setRedundancyScheme(rs).build();

                project.createBucket(expectedBucket, bucketConfig);

                io.storj.BucketInfo bucketInfo = project.getBucketInfo(expectedBucket);
                Assert.assertEquals(expectedBucket, bucketInfo.getName());

                project.deleteBucket(expectedBucket);

                try {
                    project.getBucketInfo(expectedBucket);
                } catch (StorjException e) {
                    Assert.assertTrue(e.getMessage().contains("bucket not found"));
                }
            }
        }
    }

    @Test
    public void testListBuckets() throws Exception {
        Config config = new Config.Builder().setTempDir(filesDir).build();

        try (Uplink uplink = new Uplink(config)) {
            ApiKey apiKey = new ApiKey(VALID_API_KEY);

            try (io.storj.Project project = uplink.openProject(VALID_SATELLITE_ADDRESS, apiKey)) {
                io.storj.RedundancyScheme rs = new RedundancyScheme.Builder().
                        setRequiredShares((short) 4).
                        setRepairShares((short) 6).
                        setSuccessShares((short) 8).
                        setTotalShares((short) 10).
                        build();

                io.storj.BucketConfig bucketConfig = new BucketConfig.Builder()
                        .setRedundancyScheme(rs).build();

                Set<String> expectedBuckets = new HashSet<>();
                for (int i = 0; i < 10; i++) {
                    String expectedBucket = "test-bucket" + i;
                    project.createBucket(expectedBucket, bucketConfig);
                    expectedBuckets.add(expectedBucket);
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

                for (String bucket : expectedBuckets) {
                    project.deleteBucket(bucket);
                }

                buckets = project.listBuckets(BucketListOption.pageSize(1));
                Iterator<BucketInfo> iterator = buckets.iterator();
                assertEquals(false, iterator.hasNext());
            }
        }
    }

    @Test
    public void testEquals() {
        Config c1 = new Config.Builder().build();
        Config c2 = new Config.Builder().build();
        assertEquals(c1, c2);

        c1 = new Config.Builder().setMaxInlineSize(1).
                setMaxMemory(2).setTempDir("temp").build();
        c2 = new Config.Builder().setMaxInlineSize(1).
                setMaxMemory(2).setTempDir("temp").build();
        assertEquals(c1, c2);

        c1 = new Config.Builder().setMaxInlineSize(1).
                setMaxMemory(2).setTempDir("temp").build();
        c2 = new Config.Builder().setMaxInlineSize(1).
                setMaxMemory(2).setTempDir("temp3").build();
        assertNotEquals(c1, c2);
    }


    @Test
    public void testUploadDownloadInline() throws Exception {
        Config config = new Config.Builder().setTempDir(filesDir).build();

        try (io.storj.Uplink uplink = new io.storj.Uplink(config)) {
            io.storj.ApiKey apiKey = new io.storj.ApiKey(VALID_API_KEY);

            try (io.storj.Project project = uplink.openProject(VALID_SATELLITE_ADDRESS, apiKey)) {
                String expectedBucket = "test-bucket";
                io.storj.RedundancyScheme rs = new RedundancyScheme.Builder().
                        setRequiredShares((short) 4).
                        setRepairShares((short) 6).
                        setSuccessShares((short) 8).
                        setTotalShares((short) 10).
                        build();

                io.storj.BucketConfig bucketConfig = new BucketConfig.Builder()
                        .setRedundancyScheme(rs).build();

                project.createBucket(expectedBucket, bucketConfig);

                EncryptionAccess access = new EncryptionAccess();
                access.setDefaultKey("TestEncryptionKey".getBytes());

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
        Config config = new Config.Builder().setTempDir(filesDir).build();

        try (io.storj.Uplink uplink = new io.storj.Uplink(config)) {
            io.storj.ApiKey apiKey = new io.storj.ApiKey(VALID_API_KEY);

            try (io.storj.Project project = uplink.openProject(VALID_SATELLITE_ADDRESS, apiKey)) {
                String expectedBucket = "test-bucket";
                io.storj.RedundancyScheme rs = new RedundancyScheme.Builder().
                        setRequiredShares((short) 4).
                        setRepairShares((short) 6).
                        setSuccessShares((short) 8).
                        setTotalShares((short) 10).
                        build();

                io.storj.BucketConfig bucketConfig = new BucketConfig.Builder()
                        .setRedundancyScheme(rs).build();

                project.createBucket(expectedBucket, bucketConfig);

                EncryptionAccess access = new EncryptionAccess();
                access.setDefaultKey("TestEncryptionKey".getBytes());

                try (Bucket bucket = project.openBucket(expectedBucket, access)) {
                    byte[] expectedData = new byte[2 * 1024 * 1024];
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
                            byte[] buf = new byte[512];
                            int read;
                            while ((read = bis.read(buf)) != -1) {
                                writer.write(buf, 0, read);
                            }
                            assertArrayEquals(expectedData, writer.toByteArray());
                        }
                    }

                    bucket.deleteObject(objectPath);

                    try {
                        bucket.deleteObject(objectPath);
                    } catch (StorjException e) {
                        assertTrue(e.getMessage(), e.getMessage().contains("not found"));
                    }
                }

                project.deleteBucket(expectedBucket);
            }
        }
    }

    @Test
    public void testListObjects() throws Exception {
        Config config = new Config.Builder().setTempDir(filesDir).build();

        try (io.storj.Uplink uplink = new io.storj.Uplink(config)) {
            io.storj.ApiKey apiKey = new io.storj.ApiKey(VALID_API_KEY);

            try (io.storj.Project project = uplink.openProject(VALID_SATELLITE_ADDRESS, apiKey)) {
                String expectedBucket = "test-bucket-" + new Date().getTime();
                io.storj.RedundancyScheme rs = new RedundancyScheme.Builder().
                        setRequiredShares((short) 4).
                        setRepairShares((short) 6).
                        setSuccessShares((short) 8).
                        setTotalShares((short) 10).
                        build();

                io.storj.BucketConfig bucketConfig = new BucketConfig.Builder()
                        .setPathCipher(CipherSuite.NONE)
                        .setRedundancyScheme(rs).build();

                project.createBucket(expectedBucket, bucketConfig);

                EncryptionAccess access = new EncryptionAccess();
                access.setDefaultKey("TestEncryptionKey".getBytes());

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

                        // cleanup
                        bucket.deleteObject(String.format("path%d", index));
                        index++;
                    }
                    assertEquals(expectedObjects, index);

                    project.deleteBucket(expectedBucket);
                }
            }
        }
    }

    @Test
    public void testFileSharing() throws Exception {
        String expectedBucket = "test-bucket-" + new Date().getTime();

        Config config = new Config.Builder().setTempDir(filesDir).build();
        io.storj.ApiKey apiKey = new io.storj.ApiKey(VALID_API_KEY);

        io.storj.RedundancyScheme rs = new RedundancyScheme.Builder().
                setRequiredShares((short) 4).
                setRepairShares((short) 6).
                setSuccessShares((short) 8).
                setTotalShares((short) 10).
                build();

        io.storj.BucketConfig bucketConfig = new BucketConfig.Builder()
                .setRedundancyScheme(rs).build();

        try (io.storj.Uplink uplink = new io.storj.Uplink(config)) {
            try (io.storj.Project project = uplink.openProject(VALID_SATELLITE_ADDRESS, apiKey)) {
                project.createBucket(expectedBucket, bucketConfig);

                EncryptionAccess access = new EncryptionAccess();
                access.setDefaultKey("TestEncryptionKey".getBytes());

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

        try (io.storj.Uplink uplink = new io.storj.Uplink(config)) {
            try (io.storj.Project project = uplink.openProject(VALID_SATELLITE_ADDRESS, restrictedKey)) {

                EncryptionAccess access = new EncryptionAccess();
                access.setDefaultKey("TestEncryptionKey".getBytes());

                try (Bucket bucket = project.openBucket(expectedBucket, access)) {
                    String errorMessage = "";
                    try (OutputStream oos = new ObjectOutputStream(bucket, "third-file")) {
                        oos.write("Third file content".getBytes());
                    }catch (IOException e){
                       errorMessage = e.getMessage();
                    }
                    assertTrue(errorMessage, errorMessage.contains("Unauthorized API credentials"));
                }
            }
        }

        // cleanup
        try (io.storj.Uplink uplink = new io.storj.Uplink(config)) {
            try (io.storj.Project project = uplink.openProject(VALID_SATELLITE_ADDRESS, apiKey)) {
                project.deleteBucket(expectedBucket);
            }
        }
    }

    //
//    @Test
//    public void testEncryptionAccessFromPassphrase() throws Exception {
//        Config config = new Config();
//
//        Uplink uplink = new Uplink(config, filesDir);
//        try {
//            Project project = uplink.openProject(VALID_SATELLITE_ADDRESS, VALID_API_KEY);
//            try {
//                byte[] saltedKey = project.saltedKeyFromPassphrase("some-passphrase");
//                EncryptionAccess ea = new EncryptionAccess(saltedKey);
//                String serialized = ea.serialize();
//                assertNotEquals("", serialized);
//            } finally {
//                project.close();
//            }
//        } finally {
//            uplink.close();
//        }
//    }
//
//    @Test
//    public void testEncryptionAccessWithRoot() throws Exception {
//        Config config = new Config();
//
//        Uplink uplink = new Uplink(config, filesDir);
//        try {
//            Project project = uplink.openProject(VALID_SATELLITE_ADDRESS, VALID_API_KEY);
//            try {
//                byte[] saltedKey = project.saltedKeyFromPassphrase("some-passphrase");
//                EncryptionAccess ea = Mobile.newEncryptionAccessWithRoot("bucket", "unencryptedPath", "encryptedPath", saltedKey);
//                String serialized = ea.serialize();
//                assertNotEquals("", serialized);
//            } finally {
//                project.close();
//            }
//        } finally {
//            uplink.close();
//        }
//    }
//
    @Test
    public void testApiKey() throws Exception {
        String apiKeyData = "13YqeKQiA3ANSuDu4rqX6eGs3YWox9GRi9rEUKy1HidXiNNm6a5SiE49Hk9gomHZVcQhq4eFQh8yhDgfGKg268j6vqWKEhnJjFPLqAP";
        ApiKey apiKey = new ApiKey(apiKeyData);

        String serialized = apiKey.serialize();
        assertEquals(serialized, apiKeyData);
        Caveat caveat = new Caveat.Builder()
                .disallowDeletes(true)
                .disallowLists(true)
                .disallowWrites(true)
                .disallowReads(false)
                .notAfter(50)
                .notBefore(100)
//                .addCaveatPath(new CaveatPath("bucket".getBytes(), "123456".getBytes()))
                .build();

        ApiKey newAPIKey = apiKey.restrict(caveat);
        assertNotEquals(apiKeyData, newAPIKey.serialize());
    }

}
