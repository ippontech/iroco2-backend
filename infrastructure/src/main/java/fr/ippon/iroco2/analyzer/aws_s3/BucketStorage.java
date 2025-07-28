package fr.ippon.iroco2.analyzer.aws_s3;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;

import java.time.Duration;

@Component
@RequiredArgsConstructor
public class BucketStorage {

    private final S3Presigner s3Presigner;

    @Value("${aws.s3.bucketName}")
    private String bucketName;

    public String createPresignedUrl(String key) {
        try (S3Presigner presigner = s3Presigner) {
            PutObjectPresignRequest presignRequest = buildPresignRequest(bucketName, key);

            var presignedRequest = presigner.presignPutObject(presignRequest);

            return presignedRequest.url().toExternalForm();
        }
    }

    private PutObjectPresignRequest buildPresignRequest(String bucketName, String key) {
        return PutObjectPresignRequest.builder().signatureDuration(Duration.ofMinutes(10)).putObjectRequest(putObjectBuilder -> putObjectBuilder.bucket(bucketName).key(key)).build();
    }
}
