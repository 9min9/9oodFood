package goodfood.service.file;

import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.MetadataException;
import com.drew.metadata.exif.ExifIFD0Directory;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import goodfood.controller.dto.forum.update.FileUpdateResponse;
import goodfood.entity.file.StoreImage;
import goodfood.entity.forum.Forum;
import goodfood.service.dto.file.FileServiceDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.fileupload.disk.DiskFileItem;
import org.apache.commons.io.IOUtils;
import org.imgscalr.Scalr;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Files;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
@Slf4j
public class FileStoreService {
    private final Storage storage;
    @Value("${file.localResourcePath}")
    private String localDirPath;
    @Value("${file.originalImagePath}")
    private String originImagePath;
    @Value("${spring.cloud.gcp.bucket}")
    private String gcsBucketName;

    public FileServiceDto toImageServiceDto(MultipartFile multipartFile, Forum forum) throws IOException, MetadataException {
        String storeFilename = createStoreFilename(multipartFile.getOriginalFilename());
        String viaPath = createViaPath();
        String path = createPath(storeFilename, viaPath);
        String gcsPath = createGCSPath(storeFilename, viaPath);
        String originalImagePath = createOriginalImagePath(storeFilename);

        Metadata metadata = getMetadata(multipartFile.getInputStream());
        int orientation = getOrientation(metadata);

        FileServiceDto serviceDto = new FileServiceDto();
        serviceDto.setOriginImageName(multipartFile.getOriginalFilename());
        serviceDto.setImageName(storeFilename);
        serviceDto.setViaPath(viaPath);
        serviceDto.setImagePath(path);
        serviceDto.setOriginalFilePath(originalImagePath);
        serviceDto.setOrientation(orientation);
        serviceDto.setGcsPath(gcsPath);
        serviceDto.setImageSize(multipartFile.getSize());
        serviceDto.setMultipartFile(multipartFile);
        serviceDto.setForum(forum);

        return serviceDto;
    }

    public Metadata getMetadata(InputStream inputStream) {
        Metadata metadata;
        try {
            metadata = ImageMetadataReader.readMetadata(inputStream);
        } catch (ImageProcessingException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return metadata;
    }

    public Integer getOrientation(Metadata metadata) throws MetadataException {
        int orientation = 1;

        Directory directory = metadata.getFirstDirectoryOfType(ExifIFD0Directory.class);

        // directory는 있는데 그 안에 orientation값이 없을 수 있어 두개 다 체크
        if (directory != null && directory.containsTag(ExifIFD0Directory.TAG_ORIENTATION)) {
            orientation = directory.getInt(ExifIFD0Directory.TAG_ORIENTATION);
        }

        return orientation;
    }

    public BufferedImage rotateImage(BufferedImage bufferedImage, int orientation) {
        BufferedImage rotatedImage;

        if (orientation == 6) {
            rotatedImage = Scalr.rotate(bufferedImage, Scalr.Rotation.CW_90);
        } else if (orientation == 3) {
            rotatedImage = Scalr.rotate(bufferedImage, Scalr.Rotation.CW_180);
        } else if (orientation == 8) {
            rotatedImage = Scalr.rotate(bufferedImage, Scalr.Rotation.CW_270);
        } else {
            rotatedImage = bufferedImage;
        }

        return rotatedImage;
    }

    /**
     * Create
     */

    public String createOriginalImagePath(String imageName) {
        return originImagePath + imageName;
    }

    public String createOriginalImage(FileServiceDto serviceDto) throws IOException {
        String originalImagePath = createOriginalImagePath(serviceDto.getImageName());
        serviceDto.getMultipartFile().transferTo(new File(originalImagePath));

        return originalImagePath;
    }

    public void createResizeImageFile(FileServiceDto serviceDto) throws Exception {
        File originalImageFile = new File(serviceDto.getOriginalFilePath());
        FileInputStream is = new FileInputStream(originalImageFile);

        int w = 800;
        int h =600;
        BufferedImage resize = resize(is, w, h);

        int orientation = serviceDto.getOrientation();
        BufferedImage rotatedImage = rotateImage(resize, orientation);

        ImageIO.write(rotatedImage, "jpg", new File(serviceDto.getImagePath()));

    }

    public BlobInfo uploadGCS(FileServiceDto serviceDto) throws IOException, MetadataException {
        String gcsStorePath = serviceDto.getViaPath() + serviceDto.getImageName();
        BlobInfo blobInfo = storage.create(
                BlobInfo.newBuilder(gcsBucketName, gcsStorePath)
//                        .setAcl(new ArrayList<>(Arrays.asList(Acl.of(Acl.User.ofAllAuthenticatedUsers(), Acl.Role.READER))))
                        .build(),
                new FileInputStream(serviceDto.getImagePath()));
        return blobInfo;
    }


    public static BufferedImage resize(InputStream inputStream, int width, int height) throws Exception {
        BufferedImage inputImage = ImageIO.read(inputStream); //리사이즈 대상 이미지 읽기
        BufferedImage outputImage = new BufferedImage(width, height, inputImage.getType()); //리사이즈될 사이즈

        Graphics2D g2 = outputImage.createGraphics();
        g2.drawImage(inputImage, 0, 0, width, height, null);// 리사이즈되는 이미지 그리기
        g2.dispose(); // 자원해제

        return outputImage;
    }

    public String createStoreFilename(String originalFilename) {
        String uuid = UUID.randomUUID().toString();
        String ext = extractExt(originalFilename);
        String imageName = uuid + ext;
        return imageName;
    }

    private String extractExt(String originalFilename) {
        int idx = originalFilename.lastIndexOf(".");
        String ext = originalFilename.substring(idx);
        return ext;
    }

    public String createViaPath() {
        LocalDate date = LocalDate.now();
        return date + "/";
    }

    public void createDir(String viaPath) {
        File dir = new File(localDirPath + viaPath);
        if (!dir.exists()) {
            dir.mkdir();
        }
    }

    //저장할 파일 경로 지정
    public String createPath(String storeFilename, String viaPath) {
        return localDirPath + viaPath + storeFilename;
    }

    public String createGCSPath(String storeFileName, String viaPath) {
        String gcsUrl = "https://storage.googleapis.com/";
        return gcsUrl + gcsBucketName + "/" + viaPath + storeFileName;
    }

    /**
     * Read
     */

    public MultipartFile getMultipartFile(StoreImage storeImage) throws IOException {
        File file = new File(storeImage.getImagePath());
        DiskFileItem fileItem = new DiskFileItem(storeImage.getImageName(), Files.probeContentType(file.toPath()),
                false, file.getName(), (int) file.length(), file.getParentFile());
        InputStream input = new FileInputStream(file);
        OutputStream os = fileItem.getOutputStream();
        IOUtils.copy(input, os);

        MultipartFile multipartFile = new CommonsMultipartFile(fileItem);
        return multipartFile;
    }

    public List<MultipartFile> getMultipartList(List<StoreImage> storeImageList) throws IOException {
        List<MultipartFile> result = new ArrayList<>();

        for (StoreImage storeImage : storeImageList) {
            File file = new File(storeImage.getImagePath());
            DiskFileItem fileItem = new DiskFileItem(storeImage.getImageName(), Files.probeContentType(file.toPath()), false, file.getName(), (int) file.length(), file.getParentFile());

            InputStream input = new FileInputStream(file);
            OutputStream os = fileItem.getOutputStream();
            IOUtils.copy(input, os);

            MultipartFile multipartFile = new CommonsMultipartFile(fileItem);
            result.add(multipartFile);
        }
        return result;
    }

    public List<FileUpdateResponse> getUpdateFileDto(List<StoreImage> findImageList) throws IOException {
        List<FileUpdateResponse> result = new ArrayList<>();
        for (StoreImage storeImage : findImageList) {
            MultipartFile multipartFile = getMultipartFile(storeImage);
            FileUpdateResponse response = new FileUpdateResponse().toDto(storeImage, multipartFile);
            result.add(response);
        }
        return result;
    }

    /**
     * Delete
     */
    public void deleteFile(String imagePath) {
        File file = new File(imagePath);
        if (file.isFile()) {
            file.delete();
        } else {
            log.info(imagePath + " | 파일 삭제에 실패했습니다.");
        }
    }

    public void deleteGCSFile(StoreImage image) {
        LocalDate createDate = image.getCreateDate().toLocalDate();
        BlobId blobId = BlobId.of(gcsBucketName, createDate + "/" + image.getImageName());
        storage.delete(blobId);
    }
}
