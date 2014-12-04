package utils;

import com.amazonaws.AmazonClientException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.PutObjectRequest;

import java.io.File;

public class TCAmazonS3Client {
    private static final String THUMBNAIL_BUCKET = "tc-sample-thumbnails";
    private static final String RESIZED_BUCKET = "tc-sample-resized";

    public TCAmazonS3Client() {
        AmazonS3 s3 = getS3();
        try {
            s3.createBucket(THUMBNAIL_BUCKET);
            s3.createBucket(RESIZED_BUCKET);
        } catch (AmazonS3Exception e) {
            //TODO I know that this try/catch is really bad practice on many levels, but in the interest of time,
            // I'm not going to get and iterate through the list of existing buckets yet.
        }
    }

    public void writeThumbToS3(File thumb, String key) {
        AmazonS3 s3 = getS3();

        s3.putObject(new PutObjectRequest(THUMBNAIL_BUCKET, key, thumb));
    }

    public void writeResizedToS3(File resized, String key) {
        AmazonS3 s3 = getS3();

        s3.putObject(new PutObjectRequest(RESIZED_BUCKET, key, resized));
    }

    private AmazonS3 getS3() {
        //Get credentials
        AWSCredentials credentials = null;
        try {
            credentials = new ProfileCredentialsProvider().getCredentials();
        } catch (Exception e) {
            throw new AmazonClientException("Cannot load the credentials from the credential profiles file. ", e);
        }

        AmazonS3 s3 = new AmazonS3Client(credentials);
        Region usWest2 = Region.getRegion(Regions.US_WEST_2);
        s3.setRegion(usWest2);

        return s3;
    }
}
