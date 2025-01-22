package com.aq.blogapp.services;


import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;


public interface FileService {

    String uploadImg(String path, MultipartFile file) throws IOException;
    InputStream getBlogImage(String path, String fileName) throws FileNotFoundException;

}
