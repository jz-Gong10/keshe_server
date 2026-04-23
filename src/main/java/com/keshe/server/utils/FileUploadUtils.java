package com.keshe.server.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
public class FileUploadUtils {

    private static String uploadDir;

    private static String baseUrl;

    @Value("${file.upload.dir:${user.dir}/uploads}")
    public void setUploadDir(String uploadDir) {
        FileUploadUtils.uploadDir = uploadDir;
    }

    @Value("${file.base-url:http://localhost:8080}")
    public void setBaseUrl(String baseUrl) {
        FileUploadUtils.baseUrl = baseUrl;
    }

    public static String uploadFile(MultipartFile file) throws IOException {
        if (file == null || file.isEmpty()) {
            return null;
        }

        File dir = new File(uploadDir);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        String originalFilename = file.getOriginalFilename();
        String extension = "";
        if (originalFilename != null && originalFilename.contains(".")) {
            extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        }

        String filename = UUID.randomUUID().toString() + extension;
        Path filePath = Paths.get(uploadDir, filename);

        Files.write(filePath, file.getBytes());

        return baseUrl + "/uploads/" + filename;
    }

    public static List<String> uploadFiles(List<MultipartFile> files) throws IOException {
        List<String> urls = new ArrayList<>();
        if (files != null) {
            for (MultipartFile file : files) {
                String url = uploadFile(file);
                if (url != null) {
                    urls.add(url);
                }
            }
        }
        return urls;
    }

    public static boolean deleteFile(String fileUrl) {
        if (fileUrl == null || fileUrl.isEmpty()) {
            return false;
        }

        String filename = fileUrl.substring(fileUrl.lastIndexOf("/") + 1);
        Path filePath = Paths.get(uploadDir, filename);

        try {
            return Files.deleteIfExists(filePath);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}
