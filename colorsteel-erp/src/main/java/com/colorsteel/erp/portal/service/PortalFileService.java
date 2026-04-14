package com.colorsteel.erp.portal.service;

import org.springframework.web.multipart.MultipartFile;

public interface PortalFileService {
    String uploadImage(MultipartFile file);
}
