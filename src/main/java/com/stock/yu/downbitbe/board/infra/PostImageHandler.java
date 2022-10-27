package com.stock.yu.downbitbe.board.infra;

import com.stock.yu.downbitbe.security.config.Config;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class PostImageHandler {
    public String parseImageInfo(MultipartFile multipartFile, String username) throws IOException {
        String extension = StringUtils.getFilenameExtension(multipartFile.getOriginalFilename());
        String filename = username + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS")) + '.' + extension;
        String path = Config.UPLOAD_FILE_PATH + "uploads/post/images/" + filename;
        File file = new File(path);
        multipartFile.transferTo(file);
        return filename;
    }
}
