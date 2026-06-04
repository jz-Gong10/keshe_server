package com.keshe.server.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import jakarta.annotation.PostConstruct;
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
    private String uploadDirConfig;

    @Value("${file.base-url:http://localhost:8080}")
    private String baseUrlConfig;

    @PostConstruct
    public void init() {
        uploadDir = uploadDirConfig;
        baseUrl = baseUrlConfig;
        // 确保上传目录存在
        File dir = new File(uploadDir);
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }

    public static String uploadFile(MultipartFile file) throws IOException {
        if (file == null || file.isEmpty()) {
            return null;
        }

        // 确保 uploadDir 不为空
        if (uploadDir == null) {
            uploadDir = System.getProperty("user.dir") + "/uploads";
            File dir = new File(uploadDir);
            if (!dir.exists()) {
                dir.mkdirs();
            }
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

        // 确保 baseUrl 不为空
        if (baseUrl == null) {
            baseUrl = "http://localhost:8080";
        }
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

        // 确保 uploadDir 不为空
        if (uploadDir == null) {
            uploadDir = System.getProperty("user.dir") + "/uploads";
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

    // 上传文件（返回相对路径）
    public static String uploadFileRelative(MultipartFile file) throws IOException {
        if (file == null || file.isEmpty()) {
            return null;
        }

        // 确保 uploadDir 不为空
        if (uploadDir == null) {
            uploadDir = System.getProperty("user.dir") + "/uploads";
            File dir = new File(uploadDir);
            if (!dir.exists()) {
                dir.mkdirs();
            }
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

        return "/uploads/" + filename;  // 返回相对路径
    }

    // 上传多个文件（返回相对路径）
    public static List<String> uploadFilesRelative(List<MultipartFile> files) throws IOException {
        List<String> urls = new ArrayList<>();
        if (files != null) {
            for (MultipartFile file : files) {
                String url = uploadFileRelative(file);
                if (url != null) {
                    urls.add(url);
                }
            }
        }
        return urls;
    }

    // 将相对路径转换为完整URL
    public static String getFullUrl(String relativePath) {
        if (relativePath == null || relativePath.isEmpty()) {
            return null;
        }
        // 确保 baseUrl 不为空
        if (baseUrl == null) {
            baseUrl = "http://localhost:8080";
        }
        return baseUrl + relativePath;
    }

    // 将相对路径列表转换为完整URL列表
    public static List<String> getFullUrls(List<String> relativePaths) {
        List<String> urls = new ArrayList<>();
        if (relativePaths != null) {
            for (String path : relativePaths) {
                urls.add(getFullUrl(path));
            }
        }
        return urls;
    }
}
