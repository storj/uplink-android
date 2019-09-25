package io.storj;

import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
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

    public static final String VALID_SATELLITE_ADDRESS = InstrumentationRegistry.getArguments().getString("storj.sim.host", "192.168.8.134:10000");
    public static final String VALID_API_KEY = InstrumentationRegistry.getArguments().getString("api.key", "GBK6TEMIPJQUOVVN99C2QO9USKTU26QB6C4VNM0=");

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

                Iterable<BucketInfo> buckets = project.listBuckets("", 2);
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

                buckets = project.listBuckets("", 1);
                Iterator<BucketInfo> iterator = buckets.iterator();
                assertEquals(false, iterator.hasNext());
            }
        }
    }

    @Test
    public void testEquals() throws Exception {
        Config c1 = new Config.Builder().build();
        Config c2 = new Config.Builder().build();
        assertEquals(c1, c2);

        c1 = new Config.Builder().setMaxInlineSize(1).
                setMaxMemory(2).setTempDir("temp").build();
        c2 = new Config.Builder().setMaxInlineSize(1).
                setMaxMemory(2).setTempDir("temp").build();
        assertEquals(c1, c2);

//        c1 = new Config.Builder().setMaxInlineSize(1).
//                setMaxMemory(2).setTempDir("temp").build();
//        c2 = new Config.Builder().setMaxInlineSize(1).
//                setMaxMemory(3).setTempDir("temp").build();
//        assertNotEquals(c1, c2);
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
                        UploadOptions options = new UploadOptions.Builder().build();
                        try (OutputStream writer = bucket.newWriter(objectPath, options)) {
                            writer.write(expectedData);
                            writer.flush();

                        }
                    }

                    {
                        ReaderOptions options = new ReaderOptions.Builder().build();
                        try (InputStream is = bucket.newReader(objectPath, options)) {
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
                        WriterOptions options = new WriterOptions.Builder().build();
                        try (OutputStream writer = bucket.newWriter(objectPath, options)) {
                            writer.write(expectedData);
                            writer.flush();

                        }
                    }

                    {
                        ReaderOptions options = new ReaderOptions.Builder().build();
                        try (InputStream is = bucket.newReader(objectPath, options)) {
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
                        assertTrue(e.getMessage().startsWith("object not found"));
                    }
                }

                project.deleteBucket(expectedBucket);
            }
        }
    }

//    @Test
//    public void testListObjects() throws Exception {
//        Config config = new Config.Builder().setTempDir(filesDir).build();
//
//        try (io.storj.Uplink uplink = new io.storj.Uplink(config)) {
//            io.storj.ApiKey apiKey = new io.storj.ApiKey(VALID_API_KEY);
//
//            try (io.storj.Project project = uplink.openProject(VALID_SATELLITE_ADDRESS, apiKey)) {
//                String expectedBucket = "test-bucket";
//                io.storj.RedundancyScheme rs = new RedundancyScheme.Builder().
//                        setRequiredShares((short) 4).
//                        setRepairShares((short) 6).
//                        setSuccessShares((short) 8).
//                        setTotalShares((short) 10).
//                        build();
//
//                io.storj.BucketConfig bucketConfig = new BucketConfig.Builder()
//                        .setRedundancyScheme(rs).build();
//
//                project.createBucket(expectedBucket, bucketConfig);
//
//                EncryptionAccess access = new EncryptionAccess();
//                access.setDefaultKey("TestEncryptionKey".getBytes());
//
//                try (Bucket bucket = project.openBucket(expectedBucket, access)) {
//                    long before = System.currentTimeMillis();
//
//                    for (int i = 0; i < 13; i++) {
//                        WriterOptions options = new WriterOptions.Builder().build();
//                        try (OutputStream writer = bucket.newWriter("object/path" + i, options)) {
//                            writer.write(new byte[0]);
//                            writer.flush();
//                        }
//
//                        ReaderOptions roptions = new ReaderOptions.Builder().build();
//                        try (InputStream is = bucket.newReader("object/path" + i, roptions)) {
//                        }
//                    }
//
//                    ListOptions listOptions = new ListOptions.Builder().setPrefix("").setCursor("").
//                            setDirection(ListDirection.AFTER).setPageSize(20).build();
//
//
//                    Iterable<ObjectInfo> list = bucket.listObjects(listOptions);
//                    int index = 0;
//                    for (ObjectInfo info : list) {
//                        assertEquals(expectedBucket, info.getBucket());
//                        assertTrue(info.getCreated().getTime() >= before);
//
//                        // cleanup
//                        bucket.deleteObject("object/path" + index);
//                        index++;
//                    }
//                    assertEquals(13, index);
//
//                    project.deleteBucket(expectedBucket);
//                }
//            }
//        }
//    }
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
//    @Test
//    public void testApiKey() throws Exception {
//        String apiKeyData = "13YqeKQiA3ANSuDu4rqX6eGs3YWox9GRi9rEUKy1HidXiNNm6a5SiE49Hk9gomHZVcQhq4eFQh8yhDgfGKg268j6vqWKEhnJjFPLqAP";
//        APIKey apiKey = Mobile.parseAPIKey(apiKeyData);
//
//        String serialized = apiKey.serialize();
//        assertEquals(serialized, apiKeyData);
//
//        Caveat caveat = new Caveat();
//        caveat.setDisallowDeletes(true);
//        caveat.setDisallowWrites(true);
//        caveat.setDisallowReads(true);
//        caveat.setDisallowLists(true);
//        caveat.setNotAfter(100);
//        caveat.setNotBefore(50);
//
//        CaveatPath path = new CaveatPath();
//        path.setBucket("bucket".getBytes());
//        path.setEncryptedPathPrefix("123456".getBytes());
//        caveat.addCaveatPath(path);
//
//        APIKey newAPIKey = apiKey.restrict(caveat);
//        assertNotEquals("", newAPIKey.serialize());
//    }
//

}
