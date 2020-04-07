package io.storj.test;

import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import io.storj.*;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
public class LibuplinkInstrumentedTest {

    private static final String VALID_SCOPE = InstrumentationRegistry.getArguments().getString("scope", "13GRuHAWnYKcVBgHKbpg6yNT7p4mfjAPjzWKNu771WT2sgLmyMSanBpFFhoNubNN4Gr7m55LQrR4JR8dfC86MGsWGe11poahaRGs6bRgvJj3cBTZyP2NCE21SsaV3qAcBvzcuvZGBocdw4A6mZiUZVi14JWkhk3Kd5iXyhoBCU69845CvU2My3Qv");
    private static Scope SCOPE;

    private UplinkOption[] uplinkOptions;

    @Before
    public void setUp() throws StorjException {
        SCOPE = Scope.parse(VALID_SCOPE);
        String filesDir = InstrumentationRegistry.getTargetContext().getFilesDir().getAbsolutePath();
        uplinkOptions = new UplinkOption[]{
                UplinkOption.tempDir(filesDir),
                UplinkOption.skipPeerCAWhitelist(true)
        };
    }

    @Test
    public void testBasicBucket() throws Exception {
        try (Uplink uplink = new Uplink(uplinkOptions); Project project = uplink.openProject(SCOPE)) {

            String expectedBucket = "test-bucket";
            RedundancyScheme rs = new RedundancyScheme.Builder().
                    setRequiredShares((short) 4).
                    setRepairShares((short) 6).
                    setSuccessShares((short) 8).
                    setTotalShares((short) 10).
                    build();

            try {
                BucketInfo createBucketInfo = project.createBucket(expectedBucket, BucketCreateOption.redundancyScheme(rs));

                BucketInfo getBucketInfo = project.getBucketInfo(expectedBucket);
                Assert.assertEquals(expectedBucket, getBucketInfo.getName());
                Assert.assertEquals(createBucketInfo, getBucketInfo);
            } finally {
                project.deleteBucket(expectedBucket);

                try {
                    project.getBucketInfo(expectedBucket);
                } catch (StorjException e) {
                    Assert.assertTrue("Unexpected error: " + e.getMessage(), e.getMessage().contains("bucket not found"));
                }
            }
        }
    }

    @Test
    public void testListBuckets() throws Exception {
        try (Uplink uplink = new Uplink(uplinkOptions); Project project = uplink.openProject(SCOPE)) {

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
                    project.createBucket(bucket, BucketCreateOption.redundancyScheme(rs));
                    expectedBuckets.add(bucket);
                }

                Iterable<BucketInfo> buckets = project.listBuckets(
                        BucketListOption.cursor(""), BucketListOption.pageSize(2));
                StringBuilder allBuckets = new StringBuilder();
                int numOfBuckets = 0;
                for (BucketInfo bucket : buckets) {
                    allBuckets.append(bucket.getName()).append("|");
                    numOfBuckets++;
                }

                assertEquals(allBuckets.toString(), expectedBuckets.size(), numOfBuckets);
            } finally {
                for (int i = 0; i < 10; i++) {
                    String bucket = "test-bucket" + i;
                    project.deleteBucket(bucket);
                }

                Iterable<BucketInfo> buckets = project.listBuckets(BucketListOption.pageSize(1));
                assertFalse(buckets.iterator().hasNext());
            }
        }
    }

    @Test
    public void testUploadDownloadInline() throws Exception {
        try (Uplink uplink = new Uplink(uplinkOptions); Project project = uplink.openProject(SCOPE)) {

            String expectedBucket = "test-bucket";
            RedundancyScheme rs = new RedundancyScheme.Builder().
                    setRequiredShares((short) 4).
                    setRepairShares((short) 6).
                    setSuccessShares((short) 8).
                    setTotalShares((short) 10).
                    build();

            project.createBucket(expectedBucket, BucketCreateOption.redundancyScheme(rs));

            try (Bucket bucket = project.openBucket(expectedBucket, SCOPE)) {
                byte[] expectedData = new byte[2048];
                Random random = new Random();
                random.nextBytes(expectedData);

                String objectPath = "object/path";
                bucket.uploadObject(objectPath, expectedData);

                // full download
                ByteArrayOutputStream writer = new ByteArrayOutputStream();
                bucket.downloadObject(objectPath, writer);
                assertArrayEquals(expectedData, writer.toByteArray());

                // range download
                writer.reset();
                bucket.downloadObject(objectPath, writer, 10, 20);
                assertArrayEquals(Arrays.copyOfRange(expectedData, 10, 30), writer.toByteArray());

                bucket.deleteObject(objectPath);
            }

            project.deleteBucket(expectedBucket);
        }
    }


    @Test
    public void testUploadDownloadDeleteRemote() throws Exception {
        try (Uplink uplink = new Uplink(uplinkOptions); Project project = uplink.openProject(SCOPE)) {

            String expectedBucket = "test-bucket";
            RedundancyScheme rs = new RedundancyScheme.Builder().
                    setRequiredShares((short) 4).
                    setRepairShares((short) 6).
                    setSuccessShares((short) 8).
                    setTotalShares((short) 10).
                    build();

            project.createBucket(expectedBucket, BucketCreateOption.redundancyScheme(rs));

            try (Bucket bucket = project.openBucket(expectedBucket, SCOPE)) {
                byte[] expectedData = new byte[2 * 1024 * 1024];
                Random random = new Random();
                random.nextBytes(expectedData);

                String objectPath = "object/path";
                try {
                    bucket.uploadObject(objectPath, expectedData);

                    ByteArrayOutputStream writer = new ByteArrayOutputStream();
                    bucket.downloadObject(objectPath, writer);
                    assertArrayEquals(expectedData, writer.toByteArray());
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

    @Test
    public void testListObjects() throws Exception {
        try (Uplink uplink = new Uplink(uplinkOptions); Project project = uplink.openProject(SCOPE)) {

            String expectedBucket = "test-bucket";
            RedundancyScheme rs = new RedundancyScheme.Builder().
                    setRequiredShares((short) 4).
                    setRepairShares((short) 6).
                    setSuccessShares((short) 8).
                    setTotalShares((short) 10).
                    build();

            project.createBucket(expectedBucket,
                    BucketCreateOption.redundancyScheme(rs), BucketCreateOption.pathCipher(CipherSuite.NONE));

            try (Bucket bucket = project.openBucket(expectedBucket, SCOPE)) {
                long before = System.currentTimeMillis();

                // TODO should 13 to see listing bug
                int expectedObjects = 10;

                for (int i = 0; i < expectedObjects; i++) {
                    String path = String.format("path%d", i);
                    bucket.uploadObject(path, new byte[0]);
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

    @Test
    public void testFileSharing() throws Exception {
        String expectedBucket = "test-bucket";

        RedundancyScheme rs = new RedundancyScheme.Builder().
                setRequiredShares((short) 4).
                setRepairShares((short) 6).
                setSuccessShares((short) 8).
                setTotalShares((short) 10).
                build();

        try (Uplink uplink = new Uplink(uplinkOptions); Project project = uplink.openProject(SCOPE)) {

            project.createBucket(expectedBucket, BucketCreateOption.redundancyScheme(rs));

            try (Bucket bucket = project.openBucket(expectedBucket, SCOPE)) {
                bucket.uploadObject("first-file", "First file content".getBytes(UTF_8));
                bucket.uploadObject("subfolder/second-file", "Second file content".getBytes(UTF_8));
            }
        }

        // Share a read-only scope with access only to "subfolder"
        Scope shared = SCOPE.restrict(
                new Caveat.Builder().disallowDeletes(true).disallowWrites(true).build(),
                new EncryptionRestriction(expectedBucket, "subfolder"));

        // Serialize the scope to, so it can be easily sent to the other party
        String serializedShare = shared.serialize();

        // The other party should parse the serialized shared scope and use it for accessing the data
        Scope parsed = Scope.parse(serializedShare);

        try (Uplink uplink = new Uplink(uplinkOptions);
             Project project = uplink.openProject(parsed)) {

            try (Bucket bucket = project.openBucket(expectedBucket, parsed.getEncryptionAccess())) {
                // Try to download first-file - should fail
                String errorMessage = "";
                try {
                    ByteArrayOutputStream writer = new ByteArrayOutputStream();
                    bucket.downloadObject("first-file", writer);
                } catch (StorjException e) {
                    errorMessage = e.getMessage();
                }
                assertTrue(errorMessage, errorMessage.contains("unable to find encryption base"));

                // Try to download first-file - should succeed
                ByteArrayOutputStream writer = new ByteArrayOutputStream();
                bucket.downloadObject("subfolder/second-file", writer);
                assertArrayEquals("Second file content".getBytes(UTF_8), writer.toByteArray());

                // Try to upload new file in bucket root - should fail
                errorMessage = "";
                try {
                    bucket.uploadObject("third-file", "Third file content".getBytes(UTF_8));
                } catch (StorjException e) {
                    errorMessage = e.getMessage();
                }
                assertTrue(errorMessage, errorMessage.contains("unable to find encryption base"));

                // Try to upload new file in subfolder - should fail
                errorMessage = "";
                try {
                    bucket.uploadObject("subfolder/forth-file", "Forth file content".getBytes(UTF_8));
                } catch (StorjException e) {
                    errorMessage = e.getMessage();
                }
                assertTrue(errorMessage, errorMessage.contains("Unauthorized API credentials"));

                // Try to delete second-file - should fail
                errorMessage = "";
                try {
                    bucket.deleteObject("subfolder/second-file");
                } catch (StorjException e) {
                    errorMessage = e.getMessage();
                }
                assertTrue(errorMessage, errorMessage.contains("Unauthorized API credentials"));
            }
        } finally {
            // cleanup
            try (Uplink uplink = new Uplink(uplinkOptions);
                 Project project = uplink.openProject(SCOPE);
                 Bucket bucket = project.openBucket(expectedBucket, SCOPE)) {

                bucket.deleteObject("first-file");
                bucket.deleteObject("subfolder/second-file");

                project.deleteBucket(expectedBucket);
            }
        }
    }


    @Test
    public void testEncryptionAccessFromPassphrase() throws Exception {
        try (Uplink uplink = new Uplink(uplinkOptions); Project project = uplink.openProject(SCOPE)) {

            Key saltedKey = Key.getSaltedKeyFromPassphrase(project, "some-passphrase");
            EncryptionAccess ea = new EncryptionAccess(saltedKey);
            String serialized = ea.serialize();
            assertNotEquals("", serialized);
        }
    }

    @Test
    public void testApiKey() throws Exception {
        String apiKeyData = "13YqeKQiA3ANSuDu4rqX6eGs3YWox9GRi9rEUKy1HidXiNNm6a5SiE49Hk9gomHZVcQhq4eFQh8yhDgfGKg268j6vqWKEhnJjFPLqAP";
        ApiKey apiKey = ApiKey.parse(apiKeyData);
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
                .build();

        ApiKey newAPIKey = apiKey.restrict(caveat);
        assertNotEquals(apiKeyData, newAPIKey.serialize());
    }

    @Test
    public void testMultiTenant() throws Exception {
        String userID = "user@example.com";
        String passphrase = "abc123";
        String fileName = "test.dat";
        String appBucket = "appbucket";
        String fileContent = "This is file content";

        try (Uplink uplink = new Uplink(uplinkOptions); Project project = uplink.openProject(SCOPE)) {
            RedundancyScheme rs = new RedundancyScheme.Builder().
                    setRequiredShares((short) 4).
                    setRepairShares((short) 6).
                    setSuccessShares((short) 8).
                    setTotalShares((short) 10).
                    build();

            project.createBucket(appBucket, BucketCreateOption.redundancyScheme(rs));
        }


        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] salt = digest.digest(userID.getBytes(StandardCharsets.UTF_8));

        Scope shared = SCOPE.restrict(
                new Caveat.Builder().build(),
                new EncryptionRestriction(appBucket, userID + "/"));
        Key key = Key.getSaltedKeyFromPassphrase(salt, passphrase);
        shared.getEncryptionAccess().overrideEncryptionKey(appBucket, userID, key);

        try (Uplink uplink = new Uplink(uplinkOptions); Project project = uplink.openProject(shared)) {
            try (Bucket bucket = project.openBucket(appBucket, shared)) {
                bucket.uploadObject(userID+"/"+fileName, fileContent.getBytes(UTF_8));
            }
        }

        shared = SCOPE.restrict(
                new Caveat.Builder().build(),
                new EncryptionRestriction(appBucket, userID + "/"));
        key = Key.getSaltedKeyFromPassphrase(salt, passphrase);
        shared.getEncryptionAccess().overrideEncryptionKey(appBucket, userID, key);

        try (Uplink uplink = new Uplink(uplinkOptions); Project project = uplink.openProject(shared)) {
            try (Bucket bucket = project.openBucket(appBucket, shared)) {
                ByteArrayOutputStream writer = new ByteArrayOutputStream();
                bucket.downloadObject(userID + "/"+fileName, writer);

                assertEquals(fileContent, writer.toString());

                bucket.deleteObject(userID + "/"+fileName);
            }
        }

        try (Uplink uplink = new Uplink(uplinkOptions); Project project = uplink.openProject(SCOPE)) {
            project.deleteBucket(appBucket);
        }
    }

}
