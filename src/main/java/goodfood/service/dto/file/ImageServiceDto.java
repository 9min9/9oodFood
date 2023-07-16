package goodfood.service.dto.file;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter @Setter
public class ImageServiceDto {
    private Long id;
    private String originalName;
    private MultipartFile multipartFile;

}
