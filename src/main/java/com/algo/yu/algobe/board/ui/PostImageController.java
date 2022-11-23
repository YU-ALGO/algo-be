package com.algo.yu.algobe.board.ui;

import com.algo.yu.algobe.board.application.PostImageService;
import com.algo.yu.algobe.security.config.Config;
import com.algo.yu.algobe.user.domain.user.UserAuthDto;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/boards")
public class PostImageController {
    private final PostImageService postImageService;

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/{board_id}/posts/images")
    public ResponseEntity<String> uploadPostImage(@PathVariable("board_id") Long boardId, @RequestParam("image") MultipartFile image,
                                                @CurrentSecurityContext(expression = "authentication.principal") UserAuthDto auth) throws IOException {
        String uploadFilename = postImageService.uploadPostImage(boardId, auth.getUsername(), image);

        return ResponseEntity.status(HttpStatus.OK).body(uploadFilename);
    }

    //TODO : Resource는 첨부파일이라 url로 주기 + aws s3에 업로드
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{board_id}/posts/images/{filename}")
    public ResponseEntity<Resource> downloadPostImage(@PathVariable("board_id") Long boardId, @PathVariable("filename") String filename) throws IOException {
        File imageFile = new File(Config.UPLOAD_FILE_PATH + "uploads/post/images/" + filename);
        UrlResource urlResource = new UrlResource(imageFile.getAbsolutePath());
//        FileSystemResource resource = new FileSystemResource(filename);
//        HttpHeaders responseHeaders = new HttpHeaders();
//        Path filePath = Paths.get(filename);
//        responseHeaders.add("Content-Type", Files.probeContentType(filePath));
        //return ResponseEntity.status(HttpStatus.OK).headers(responseHeaders).body(resource);
        return ResponseEntity.status(HttpStatus.OK).body(urlResource);
    }
}
