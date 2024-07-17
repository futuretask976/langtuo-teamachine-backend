package com.langtuo.teamachine.dao.oss;

import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.common.auth.CredentialsProvider;
import com.aliyun.oss.common.auth.DefaultCredentialProvider;
import com.aliyun.oss.model.ObjectMetadata;
import com.aliyun.oss.model.PutObjectResult;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.net.URL;
import java.security.SecureRandom;
import java.util.Date;

public class OSSUtils {
    public static String uploadFile(File file) throws OSSException, ClientException, FileNotFoundException {
        if (!file.exists()) {
            throw new IllegalArgumentException(file.getName());
        }

        // 获取文件名
        String fileName = file.getName();
        // 获取文件后缀名
        String suffixName = fileName.substring(fileName.lastIndexOf("."));
        // 最后上传生成的文件名
        String finalFileName = System.currentTimeMillis() + "" + new SecureRandom().nextInt(0x0400)
                + suffixName;
        // OSS中的文件夹名
        String objectName = OSSConfig.OSS_STORAGE_PATH_PREFIX + finalFileName;

        // 创建OSSClient实例
        CredentialsProvider credentialsProvider = new DefaultCredentialProvider(
                OSSConfig.OSS_ACCESS_KEY_ID, OSSConfig.OSS_ACCESS_KEY_SECRET);
        OSS ossClient = new OSSClientBuilder().build(OSSConfig.OSS_END_POINT, credentialsProvider);
        // 创建file输入流
        FileInputStream fileInputStream = null;
        try {
            fileInputStream = new FileInputStream(file);
            PutObjectResult result = ossClient.putObject(OSSConfig.OSS_BUCKET_NAME, objectName,
                    fileInputStream);
            System.out.println(result);
        } finally {
            if (ossClient != null) {
                ossClient.shutdown();
            }
            if (fileInputStream != null) {
                try {
                    fileInputStream.close();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
        return objectName;
    }

    public static String uploadFile(MultipartFile multipartFile)
            throws OSSException, ClientException, IOException {
        if (multipartFile == null) {
            throw new IllegalArgumentException(multipartFile.getName());
        }

        // 获取文件名
        String fileName = multipartFile.getOriginalFilename();
        // 获取文件后缀名
        String suffixName = fileName.substring(fileName.lastIndexOf("."));
        // 最后上传生成的文件名
        String finalFileName = System.currentTimeMillis() + "" + new SecureRandom().nextInt(0x0400)
                + suffixName;
        // OSS中的文件夹名
        String objectName = OSSConfig.OSS_STORAGE_PATH_PREFIX + finalFileName;

        // 创建OSSClient实例
        CredentialsProvider credentialsProvider = new DefaultCredentialProvider(
                OSSConfig.OSS_ACCESS_KEY_ID, OSSConfig.OSS_ACCESS_KEY_SECRET);
        OSS ossClient = new OSSClientBuilder().build(OSSConfig.OSS_END_POINT, credentialsProvider);
        // 创建上传文件的元信息，可以通过文件元信息设置HTTP header(设置了才能通过返回的链接直接访问)。
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentType("image/jpg");

        // 创建file输入流
        ByteArrayInputStream byteArrayInputStream = null;
        try {
            byteArrayInputStream = new ByteArrayInputStream(multipartFile.getBytes());
            PutObjectResult result = ossClient.putObject(OSSConfig.OSS_BUCKET_NAME, objectName,
                    byteArrayInputStream, objectMetadata);
            System.out.println(result);
        } finally {
            if (ossClient != null) {
                ossClient.shutdown();
            }
            if (byteArrayInputStream != null) {
                try {
                    byteArrayInputStream.close();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
        return objectName;
    }

    public static String genAccessObjectURL(String objectName) throws OSSException, ClientException {
        // 创建OSSClient实例
        CredentialsProvider credentialsProvider = new DefaultCredentialProvider(
                OSSConfig.OSS_ACCESS_KEY_ID, OSSConfig.OSS_ACCESS_KEY_SECRET);
        OSS ossClient = new OSSClientBuilder().build(OSSConfig.OSS_END_POINT, credentialsProvider);

        try {
            Date expiration = new Date(System.currentTimeMillis() + OSSConfig.OSS_ACCESS_EXPIRATION_TIME);
            URL result = ossClient.generatePresignedUrl(OSSConfig.OSS_BUCKET_NAME, objectName, expiration);
            return result.toString();
        } finally {
            if (ossClient != null) {
                ossClient.shutdown();
            }
        }
    }
}
