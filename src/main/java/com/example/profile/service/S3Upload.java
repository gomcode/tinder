package com.example.profile.service;


import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.internal.Mimetypes;
import com.amazonaws.services.s3.model.ObjectMetadata;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class S3Upload {

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Value("${cloud.aws.s3.dir}")
    private String dir;

    private final AmazonS3Client s3Client;

    public String upload(InputStream inputStream, String originFileName, String fileSize) {
        // 파일의 이름이 중복되지 않기 위해서 UUID로 생성한 랜덤 값과 파일 이름을 연결하여 S3에 업로드
        String s3FileName = UUID.randomUUID() + "-" + originFileName;

        //파일의 사이즈를 ContentLength로 S3에 알려주기 위해서 ObjectMetadata를 사용
        ObjectMetadata objMeta = new ObjectMetadata();




        // objMeta.setContentType("png");

        //
        var mimetype = Mimetypes.getInstance().getMimetype(s3FileName);

        objMeta.setContentType(mimetype);
        objMeta.setContentLength(Long.parseLong(fileSize));

        //S3 API 메소드인 putObject를 이용하여 파일 Stream을 열어서 S3에 파일을 업로드
        s3Client.putObject(bucket, s3FileName, inputStream, objMeta);
        //  357125ea-84f5-4021-8cd5-fbed6a15e34f-ERD_1.JPG

        //  getUrl 메소드를 통해서 S3에 업로드된 사진 URL을 가져오는 방식입니다.

        return s3Client.getUrl(bucket, s3FileName).toString();
    }
}
