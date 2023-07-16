package goodfood.service.file;

import goodfood.entity.file.StoreImage;
import goodfood.entity.forum.Forum;
import goodfood.repository.infra.StoreImageRepository;
import goodfood.service.dto.file.FileServiceDto;
import goodfood.service.dto.file.ImageServiceDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class StoreImageService {
    private final StoreImageRepository imageRepository;
    private final FileStoreService fileStoreService;

    /** Create */
    public void saveImage(MultipartFile multipartFile, Forum forum) throws Exception {
        FileServiceDto serviceDto = fileStoreService.toImageServiceDto(multipartFile, forum);
        String originalImagePath = fileStoreService.createOriginalImage(serviceDto);
        fileStoreService.createResizeImageFile(serviceDto);
        fileStoreService.uploadGCS(serviceDto);
        fileStoreService.deleteFile(originalImagePath);

        StoreImage storeImage = serviceDto.toEntity();
        imageRepository.save(storeImage);
    }

    /** Update */
    @Transactional
    public void updateImage(List<ImageServiceDto> imageServiceDto, Forum forum) throws Exception {
        List<StoreImage> findImageList = imageRepository.findImageByForumId(forum.getId());
        // Delete
        List<Long> deleteImageId = findDeleteImageId(findImageList, imageServiceDto);
        deleteImage(deleteImageId);

        List<ImageServiceDto> createImageList = findCreateImage(imageServiceDto);

        if(!createImageList.isEmpty()) {
            for (ImageServiceDto serviceDto : createImageList) {
                FileServiceDto storeImgServiceDto = fileStoreService.toImageServiceDto(serviceDto.getMultipartFile(), forum);
                saveImage(storeImgServiceDto.getMultipartFile(), storeImgServiceDto.getForum());
            }

        } else {
            log.info("forum id :" +forum.getId() + "의 모든 이미지 삭제됨");
        }
    }

    private List<Long> findDeleteImageId(List<StoreImage> findImageList, List<ImageServiceDto> imageServiceDto) {
        List<Long> findImageIdList = findImageList.stream().map(e -> e.getId()).toList();
        List<Long> serviceDtoIdList = imageServiceDto.stream().map(e -> e.getId()).toList();
        List<Long> result = findImageIdList.stream().filter(num -> !serviceDtoIdList.contains(num)).collect(Collectors.toList());

        return result;
    }

    private List<ImageServiceDto> findCreateImage(List<ImageServiceDto> imageServiceDto) {
        List<ImageServiceDto> newImageList = imageServiceDto.stream().filter(e -> e.getId() == null).toList();
        ArrayList<ImageServiceDto> result = new ArrayList<>(newImageList);
        result.removeIf(e -> e.getOriginalName().isEmpty());

        return result;
    }

    /** Delete */
    public void deleteImage(List<Long> imageIdList) {
        for (Long imageId : imageIdList) {
            Optional<StoreImage> findImage = imageRepository.findById(imageId);
            if(!findImage.isEmpty()) {
                findImage.get().removeForum();
                fileStoreService.deleteFile(findImage.get().getImagePath());
                fileStoreService.deleteGCSFile(findImage.get());
                imageRepository.deleteById(imageId);

            } else {
                log.info(findImage.get().getId()+ "번 이미지데이터 삭제에 실패했습니다.");
            }
        }
    }



}