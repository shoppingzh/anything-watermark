/**
 * 
 */
package com.xpzheng.watermark.controller;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.FilenameUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.xpzheng.watermark.util.WebUtils;
import com.xpzheng.watermark.web.AjaxResult;

/**
 * @author xpzheng
 *
 */
@RestController
@RequestMapping("file")
public class FileController {

    public static final String UPLOAD_FILE_PATH = "upload";

    @PostMapping("upload")
    public AjaxResult upload(MultipartFile file, HttpServletRequest req) throws IllegalStateException, IOException {
        File path = WebUtils.getWebPath(req, UPLOAD_FILE_PATH);
        String filename = UUID.randomUUID().toString() + "." + FilenameUtils.getExtension(file.getOriginalFilename());
        file.transferTo(new File(path, filename));
        return AjaxResult.success(filename);
    }

}
