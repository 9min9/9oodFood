package goodfood.controller.dto.forum.update;

import goodfood.entity.file.StoreImage;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;

@Getter
@Setter
@NoArgsConstructor
public class FileUpdateResponse {
    private Long imageId;
    private String originalFileName;
    private String name;
    private String imagePath;
    private String gcsPath;
    private byte[] notEnbytes;
    private String contentType;
    private long size;

    private String bytes;

    public FileUpdateResponse toDto(StoreImage storeImage, MultipartFile multipartFile) throws IOException {
        this.imageId = storeImage.getId();
        this.name = storeImage.getImageName();
        this.originalFileName = storeImage.getOriginImageName();
        this.imagePath = storeImage.getImagePath();
        this.gcsPath = storeImage.getGcsPath();
        this.notEnbytes = multipartFile.getBytes();
        this.contentType = multipartFile.getContentType();
        this.size = multipartFile.getSize();
        this.bytes = Base64.getEncoder().encodeToString(multipartFile.getBytes());

        return this;
    }


}
