package com.stock.yu.downbitbe.image.ui;

import com.stock.yu.downbitbe.image.application.PreSignedURLService;
import com.stock.yu.downbitbe.image.domain.ImageRequestDto;
import com.stock.yu.downbitbe.image.domain.ImageRequestType;
import com.stock.yu.downbitbe.user.application.UserService;
import com.stock.yu.downbitbe.user.domain.user.UserAuthDto;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/posts/images")
@ApiResponses({
        @ApiResponse(responseCode = "200", description = "OK"),
        @ApiResponse(responseCode = "400", description = "BAD REQUEST"),
        @ApiResponse(responseCode = "404", description = "NOT FOUND"),
        @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR")
})
public class PreSignedURLController {
    private final PreSignedURLService preSignedURLService;

    @PostMapping("")
    public ResponseEntity<String> getImageURL(@RequestBody ImageRequestDto imageRequestDto,
                                              @CurrentSecurityContext(expression = "authentication.principal") UserAuthDto auth){
        if(imageRequestDto.getImageRequestType() == ImageRequestType.GET)
            return ResponseEntity.status(HttpStatus.OK).body(preSignedURLService.getImageByName(imageRequestDto.getFileName()));
        else if(imageRequestDto.getImageRequestType() == ImageRequestType.POST) {
            int index = imageRequestDto.getFileName().lastIndexOf('.');
            String extension = imageRequestDto.getFileName().substring(index).toLowerCase();
            return ResponseEntity.status(HttpStatus.OK).body(preSignedURLService.postImage(extension, auth.getUsername()));
        }
        else if(imageRequestDto.getImageRequestType() == ImageRequestType.DELETE)
            return ResponseEntity.status(HttpStatus.OK).body(preSignedURLService.deleteImage(imageRequestDto.getFileName(), auth.getUsername()));
        else return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("잘못된 접근입니다.");
    }
}
