package top.blogapi.service.upload;

/**
 * Giao diện service cho thao tác tải tệp lên, xử lý lưu trữ và xóa hình ảnh.
 */
public interface UploadService {
    String uploadImage(byte[] data, String fileName, String contentType);
    void deleteImage(String fileUrl);
}
