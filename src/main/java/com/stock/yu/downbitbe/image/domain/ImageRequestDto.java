package com.stock.yu.downbitbe.image.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ImageRequestDto {
    @NotBlank
    @JsonProperty("file_name")
    private String fileName;

    @NotNull
    @JsonProperty("image_request_type")
    private ImageRequestType imageRequestType;
}
