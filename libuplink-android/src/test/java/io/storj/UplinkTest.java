package io.storj;

import com.google.common.io.ByteStreams;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.function.Consumer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class UplinkTest {

//    static {
//        try {
//            System.load("/home/wywrzal/git/storj/uplink-c/.build/libuplink.so");
//        } catch (UnsatisfiedLinkError e) {
//            System.err.println("Native code library failed to load.\n" + e);
//            System.exit(1);
//        }
//    }

    private static final String VALID_ACCESS = System.getenv("GATEWAY_0_ACCESS");// InstrumentationRegistry.getArguments().getString("access", "13GRuHAWnYKcVBgHKbpg6yNT7p4mfjAPjzWKNu771WT2sgLmyMSanBpFFhoNubNN4Gr7m55LQrR4JR8dfC86MGsWGe11poahaRGs6bRgvJj3cBTZyP2NCE21SsaV3qAcBvzcuvZGBocdw4A6mZiUZVi14JWkhk3Kd5iXyhoBCU69845CvU2My3Qv");
    private static Access ACCESS;

    private UplinkOption[] uplinkOptions;

    @Before
    public void setUp() throws StorjException {
        ACCESS = Access.parse(VALID_ACCESS);
        String filesDir = System.getProperty("java.io.tmpdir");
        uplinkOptions = new UplinkOption[]{
                UplinkOption.tempDir(filesDir),
        };
    }

    @Test
    public void testBuckets() throws Exception {
        Uplink uplink = new Uplink(uplinkOptions);
        try (Project project = uplink.openProject(ACCESS)) {

            String[] expectedBuckets = new String[]{"test-bucket", "another", "foo", "bar"};

            for (String name : expectedBuckets) {
                BucketInfo createBucketInfo = project.createBucket(name);
                Assert.assertEquals(name, createBucketInfo.getName());

                BucketInfo statBucket = project.statBucket(name);
                Assert.assertEquals(name, statBucket.getName());
                Assert.assertEquals(createBucketInfo, statBucket);

                BucketInfo ensureBucketInfo = project.ensureBucket(name);
                Assert.assertEquals(name, ensureBucketInfo.getName());

                statBucket = project.statBucket(name);
                Assert.assertEquals(name, statBucket.getName());
                Assert.assertEquals(ensureBucketInfo, statBucket);
            }

            String[] toEnsure = new String[]{"gogo", "for", "itit"};
            for (String name : toEnsure) {
                BucketInfo ensureBucketInfo = project.ensureBucket(name);
                Assert.assertEquals(name, ensureBucketInfo.getName());

                BucketInfo statBucket = project.statBucket(name);
                Assert.assertEquals(name, statBucket.getName());
                Assert.assertEquals(ensureBucketInfo, statBucket);
            }

            List<String> combine = new ArrayList<>();
            combine.addAll(Arrays.asList(expectedBuckets));
            combine.addAll(Arrays.asList(toEnsure));
            expectedBuckets = combine.toArray(new String[combine.size()]);

            final List<BucketInfo> buckets = new ArrayList<>();
            project.listBuckets().iterate(new Consumer<BucketInfo>() {
                @Override
                public void accept(BucketInfo bucketInfo) {
                    buckets.add(bucketInfo);
                }
            });
            Assert.assertEquals(expectedBuckets.length, buckets.size());

            for (BucketInfo bucket : buckets) {
                BucketInfo deleteBucketInfo = project.deleteBucket(bucket.getName());
                Assert.assertEquals(bucket, deleteBucketInfo);
            }

            for (BucketInfo bucket : buckets) {
                try {
                    project.statBucket(bucket.getName());
                } catch (StorjException e) {
                    Assert.assertTrue("Unexpected error: " + e.getMessage(), e.getMessage().contains("bucket not found"));
                }
            }
        }
    }

    @Test
    public void testObjects() throws Exception {
        Uplink uplink = new Uplink(uplinkOptions);
        try (Project project = uplink.openProject(ACCESS)) {
            BucketInfo createBucketInfo = project.createBucket("test-objects-test-bucket");

            byte[] expectedData = new byte[2 * 1024 * 1024];
            Random random = new Random();
            random.nextBytes(expectedData);

            Map<String, String> metadata = new HashMap<>();
            metadata.put("custom-meta-1", "foo");
            metadata.put("custom-meta-2", "boo");
            try (ObjectOutputStream os = project.uploadObject(createBucketInfo.getName(), "test-file")) {
                os.setCustom(metadata);
                os.write(expectedData);
                os.commit();
            }

            ObjectInfo objectInfo = project.statObject(createBucketInfo.getName(), "test-file");
            Assert.assertEquals("test-file", objectInfo.getKey());
            Assert.assertEquals(expectedData.length, objectInfo.getSystem().getContentLength());
            Assert.assertEquals(metadata, objectInfo.getCustom());

            try (ObjectOutputStream os = project.uploadObject(createBucketInfo.getName(), "test-file-2")) {
                os.write(expectedData);
                os.abort();
            }

            try {
                project.statObject(createBucketInfo.getName(), "test-file-2");
                fail("Exception not thrown");
            } catch (StorjException e) {
            }

            byte[] data;
            try (InputStream is = project.downloadObject(createBucketInfo.getName(), "test-file")) {
                data = ByteStreams.toByteArray(is);
            }

            Assert.assertArrayEquals(expectedData, data);
            ObjectInfo deleteObjectInfo = project.deleteObject(createBucketInfo.getName(), "test-file");
            Assert.assertEquals(objectInfo, deleteObjectInfo);

            project.deleteBucket(createBucketInfo.getName());
        }
    }

    @Test
    public void testAccess() throws Exception {
        String serializedAccess = VALID_ACCESS;
        Access access = Access.parse(serializedAccess);
        String newSerializedAccess = access.serialize();
        assertEquals(serializedAccess, newSerializedAccess);

        Permission permission = new Permission.Builder().allowDownload().allowList().build();
        Access sharedAccess = access.share(permission);
        sharedAccess.serialize();

        permission = new Permission.Builder().allowDownload().allowList().allowUpload().allowDelete().build();
        sharedAccess = access.share(permission,
                new SharePrefix("bucket1"), new SharePrefix("bucket2", "my-prefix"));
        sharedAccess.serialize();
    }
}
