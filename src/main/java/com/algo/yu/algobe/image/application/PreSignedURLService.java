package com.algo.yu.algobe.image.application;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

@Slf4j
@RequiredArgsConstructor
@Service
public class PreSignedURLService {
    private final AmazonS3 amazonS3;

    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;

    private String generateUrl(String fileName, HttpMethod httpMethod) {
        Date expiration = new Date();
        long expTimeMillis = expiration.getTime();
        expTimeMillis += 1000 * 60 * 2;
        expiration.setTime(expTimeMillis);
        return amazonS3.generatePresignedUrl(bucketName, fileName, expiration, httpMethod).toString();
    }

    @Async
    public String getImageByName(String fileName, String path){
        String filePath = path + fileName;
        if (!amazonS3.doesObjectExist(bucketName, filePath))
            return "File does not exist";
        return generateUrl(filePath, HttpMethod.GET);
    }

    @Async
    public String postImage(String extension, String username, String path) {
        int index = username.lastIndexOf('@');
        String fileName = path + username.substring(0, index).toLowerCase() + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS")) + extension;
        String url = generateUrl(fileName, HttpMethod.PUT);
        log.info("URL = " + url);
        return url;
    }

    @Async
    public String deleteImage(String fileName, String username, String path) {
        String filePath = path + fileName;
        if (!amazonS3.doesObjectExist(bucketName, filePath))
            return "File does not exist";
        return generateUrl(fileName, HttpMethod.DELETE);
    }
}
