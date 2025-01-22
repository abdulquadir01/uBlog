package com.aq.blogapp.services.impl;

import com.aq.blogapp.services.FileService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class FileServiceImpl implements FileService {

  private final Logger logger = LoggerFactory.getLogger(FileServiceImpl.class);

  @Override
  public String uploadImg(String imagePath, MultipartFile imageFile) throws IOException {
//    file name
    Optional<String> originalImageName = Optional.ofNullable(imageFile.getOriginalFilename());

//    random name generator
    String randomId = UUID.randomUUID().toString();
    String imageNameWithoutExtension = originalImageName.map(str -> str.lastIndexOf(".")).toString();
    String newImageName = randomId.concat(imageNameWithoutExtension);

    /**
     if(originalImageName.isPresent()) {
     String imageNameWithoutExtension = originalImageName.get()
     .substring( originalImageName.get().lastIndexOf(".") );
     newImageName = randomId.concat(imageNameWithoutExtension);
     }
     */

//    full path
    String filePath = imagePath + File.separator + newImageName;

//    Create folder if not created
    File file = new File(imagePath);
    if (!file.exists()) {
      file.mkdir();
    }

//    file copy from input-stream to a file
    Files.copy(imageFile.getInputStream(), Paths.get(filePath));

    return newImageName;
  }


  @Override
  public InputStream getBlogImage(String imagePath, String fileName) throws FileNotFoundException {

    String fullPath = imagePath + File.separator + fileName;

    return new FileInputStream(fullPath);
  }
}
