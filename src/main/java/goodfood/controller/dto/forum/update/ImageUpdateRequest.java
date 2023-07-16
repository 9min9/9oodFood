package goodfood.controller.dto.forum.update;

import goodfood.service.dto.file.ImageServiceDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import static lombok.AccessLevel.PUBLIC;

@Getter @Setter
@NoArgsConstructor(access = PUBLIC)
@AllArgsConstructor(access = PUBLIC)
public class ImageUpdateRequest {
    private Long id;

    private String originalName;

    public ImageServiceDto toServiceDto(MultipartFile multipartFile) {
        ImageServiceDto serviceDto = new ImageServiceDto();
        serviceDto.setId(id);
        serviceDto.setOriginalName(originalName);
        serviceDto.setMultipartFile(multipartFile);

        return serviceDto;
    }



}
