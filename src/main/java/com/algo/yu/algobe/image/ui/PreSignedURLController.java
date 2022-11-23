package com.algo.yu.algobe.image.ui;

import com.algo.yu.algobe.image.application.PreSignedURLService;
import com.algo.yu.algobe.image.domain.ImageRequestType;
import com.algo.yu.algobe.image.domain.ImageRequestDto;
import com.algo.yu.algobe.user.domain.user.UserAuthDto;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1")
@ApiResponses({
        @ApiResponse(responseCode = "200", description = "OK"),
        @ApiResponse(responseCode = "400", description = "BAD REQUEST"),
        @ApiResponse(responseCode = "404", description = "NOT FOUND"),
        @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR")
})
public class PreSignedURLController {
    private final PreSignedURLService preSignedURLService;

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/posts/images")
    public ResponseEntity<String> getPostImageURL(@RequestBody ImageRequestDto imageRequestDto,
                                              @CurrentSecurityContext(expression = "authentication.principal") UserAuthDto auth){
        String filePath = "post_image/";
        if(imageRequestDto.getImageRequestType() == ImageRequestType.GET)
            return ResponseEntity.status(HttpStatus.OK).body(preSignedURLService.getImageByName(imageRequestDto.getFileName(), filePath));
        else if(imageRequestDto.getImageRequestType() == ImageRequestType.POST) {
            int index = imageRequestDto.getFileName().lastIndexOf('.');
            String extension = imageRequestDto.getFileName().substring(index).toLowerCase();
            return ResponseEntity.status(HttpStatus.OK).body(preSignedURLService.postImage(extension, auth.getUsername(), filePath));
        }
        else if(imageRequestDto.getImageRequestType() == ImageRequestType.DELETE)
            return ResponseEntity.status(HttpStatus.OK).body(preSignedURLService.deleteImage(imageRequestDto.getFileName(), auth.getUsername(), filePath));
        else return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("잘못된 접근입니다.");
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/profiles/images")
    public ResponseEntity<String> getProfileImageURL(@RequestBody ImageRequestDto imageRequestDto,
                                                  @CurrentSecurityContext(expression = "authentication.principal") UserAuthDto auth){
        String filePath = "profile_image/";
        if(imageRequestDto.getImageRequestType() == ImageRequestType.GET)
            return ResponseEntity.status(HttpStatus.OK).body(preSignedURLService.getImageByName(imageRequestDto.getFileName(), filePath));
        else if(imageRequestDto.getImageRequestType() == ImageRequestType.POST) {
            int index = imageRequestDto.getFileName().lastIndexOf('.');
            String extension = imageRequestDto.getFileName().substring(index).toLowerCase();
            return ResponseEntity.status(HttpStatus.OK).body(preSignedURLService.postImage(extension, auth.getUsername(), filePath));
        }
        else if(imageRequestDto.getImageRequestType() == ImageRequestType.DELETE)
            return ResponseEntity.status(HttpStatus.OK).body(preSignedURLService.deleteImage(imageRequestDto.getFileName(), auth.getUsername(), filePath));
        else return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("잘못된 접근입니다.");
    }
}
