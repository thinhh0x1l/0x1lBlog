package top.blogapi.service.upload;

public interface UploadService {
    String uploadImage(byte[] data, String fileName, String contentType);
    void deleteImage(String fileUrl);
}
