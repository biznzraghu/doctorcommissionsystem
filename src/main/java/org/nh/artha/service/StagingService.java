package org.nh.artha.service;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface StagingService {
    String uploadFile(@RequestBody MultipartFile multipartFile,String type) throws IOException;
}
