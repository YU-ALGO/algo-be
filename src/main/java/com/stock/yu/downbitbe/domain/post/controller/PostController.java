package com.stock.yu.downbitbe.domain.post.controller;

import com.stock.yu.downbitbe.domain.post.dto.PostDTO;
import com.stock.yu.downbitbe.domain.post.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Log4j2
@RequestMapping("/posts/")
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;

    @PostMapping(value="")
    public ResponseEntity<Long> register(@RequestBody PostDTO postDTO) {
        log.info("--------------------register-------------");
        log.info(postDTO);

        Long id = postService.register(postDTO);

        return new ResponseEntity<>(id, HttpStatus.OK);
    }
    
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PostDTO> read(@PathVariable("id") Long id) {
        log.info("----------------read--------------");
        log.info(id);
        
        return new ResponseEntity<>(postService.get(id), HttpStatus.OK);
    }
    
    @GetMapping(value = "/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<PostDTO>> getList(String authorId) {
        log.info("------------getList-------------");
        log.info(authorId);

        return new ResponseEntity<>(postService.getAllWithAuthor(authorId), HttpStatus.OK);
    }
}
