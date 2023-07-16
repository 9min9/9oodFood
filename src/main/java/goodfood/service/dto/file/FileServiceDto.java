package goodfood.service.dto.file;

import goodfood.entity.file.StoreImage;
import goodfood.entity.forum.Forum;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class FileServiceDto {
    private String originImageName;
    private String imageName;
    private String imagePath;
    private String originalFilePath;

    private String viaPath;
    private String gcsPath;
    private Long imageSize;
    private int orientation;
    private Forum forum;

    private MultipartFile multipartFile;

    public StoreImage toEntity() {
        return StoreImage.builder().originImageName(originImageName).imageName(imageName).imagePath(imagePath).gcsPath(gcsPath).imageSize(imageSize).forum(forum).build();
    }


}
