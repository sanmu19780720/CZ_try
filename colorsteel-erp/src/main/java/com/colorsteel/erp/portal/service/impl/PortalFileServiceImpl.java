package com.colorsteel.erp.portal.service.impl;

import com.colorsteel.erp.common.exception.BusinessException;
import com.colorsteel.erp.common.result.ResultCode;
import com.colorsteel.erp.portal.service.PortalFileService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.UUID;

@Service
public class PortalFileServiceImpl implements PortalFileService {

    private final String uploadRoot;

    public PortalFileServiceImpl(@Value("${portal.upload-root:./uploads}") String uploadRoot) {
        this.uploadRoot = uploadRoot;
    }

    @Override
    public String uploadImage(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new BusinessException(ResultCode.BAD_REQUEST.getCode(), "请选择图片文件");
        }
        String contentType = file.getContentType();
        if (!StringUtils.hasText(contentType) || !contentType.startsWith("image/")) {
            throw new BusinessException(ResultCode.BAD_REQUEST.getCode(), "仅支持图片文件");
        }
        if (file.getSize() > 5 * 1024 * 1024L) {
            throw new BusinessException(ResultCode.BAD_REQUEST.getCode(), "图片大小不能超过 5MB");
        }

        String ext = detectExt(file.getOriginalFilename());
        String dateFolder = LocalDate.now().toString().replace("-", "");
        String relative = "portal/" + dateFolder + "/" + UUID.randomUUID().toString().replace("-", "") + ext;
        Path target = Paths.get(uploadRoot).toAbsolutePath().resolve(relative).normalize();

        try {
            Files.createDirectories(target.getParent());
            file.transferTo(target);
        } catch (IOException e) {
            throw new BusinessException(ResultCode.INTERNAL_ERROR.getCode(), "图片上传失败");
        }
        return "/uploads/" + relative.replace("\\", "/");
    }

    private String detectExt(String originalFilename) {
        if (!StringUtils.hasText(originalFilename)) {
            return ".jpg";
        }
        int idx = originalFilename.lastIndexOf('.');
        if (idx < 0 || idx == originalFilename.length() - 1) {
            return ".jpg";
        }
        String ext = originalFilename.substring(idx).toLowerCase();
        return ext.length() > 10 ? ".jpg" : ext;
    }
}
