package com.algo.yu.algobe.board.ui;

import com.algo.yu.algobe.board.domain.post.*;
import com.algo.yu.algobe.board.application.PostService;
import com.algo.yu.algobe.security.config.Config;
import com.algo.yu.algobe.user.domain.user.UserAuthDto;
import com.algo.yu.algobe.user.domain.user.User;
import com.algo.yu.algobe.user.application.UserService;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/boards")
@Log4j2
@ApiResponses({
        @ApiResponse(responseCode = "200", description = "OK"),
        @ApiResponse(responseCode = "400", description = "BAD REQUEST"),
        @ApiResponse(responseCode = "404", description = "NOT FOUND"),
        @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR")
})
public class PostController {
    private final PostService postService;
    private final UserService userService;

    @GetMapping("/{board_id}/posts")
    public ResponseEntity<List<PostListResponseDto>> getPostList(@PathVariable("board_id") Long boardId, @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable,
                                                                 @RequestParam(value = "keyword", required = false) String keyword, @RequestParam(value = "searchType", required = false) PostSearchType searchType) {
        Page<PostListResponseDto> postListResponse = postService.findAllPostsByBoardId(boardId, pageable, keyword, searchType);
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set("X-Page-Count", String.valueOf(postListResponse.getTotalPages()));
        return ResponseEntity.status(HttpStatus.OK).headers(responseHeaders).body(postListResponse.stream().collect(Collectors.toList()));
    }

    @GetMapping("/posts/top")
    public ResponseEntity<List<PostListResponseDto>> getTopPostList(@RequestParam(value = "size") int size){
        List<PostListResponseDto> responseDtoList = postService.findTopPosts(size);
        return ResponseEntity.status(HttpStatus.OK).body(responseDtoList);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{board_id}/posts/{post_id}")
    public ResponseEntity<PostResponseDto> getPost(@PathVariable("board_id") Long boardId, @PathVariable("post_id") Long postId,
                                                   @CookieValue("viewList") String viewList,
                                                   @CurrentSecurityContext(expression = "authentication.principal") UserAuthDto auth) {
        User user = userService.findByUsername(auth.getUsername());

        PostResponseDto responseDto = postService.findPostByPostId(boardId, postId, user.getUserId());

        Set<Long> viewSet = new HashSet<>();

        if(!(viewList.equals("")||viewList.equals("[]"))) {
            log.info("viewList : ");

            for (String s : viewList.replaceAll("[\\[\\]]", "").split("/")) {
                log.info(s);
                viewSet.add(Long.parseLong(s));
            }
        }

        long viewCount = 0;
        if (!viewSet.contains(postId))
            viewCount = postService.updateView(postId, user);
        else
            viewCount = responseDto.getViewCount();
        viewSet.add(postId);

        log.info("viewSet : "+viewSet.toString().replaceAll(",","/"));

        ResponseCookie viewListCookie = ResponseCookie.from("viewList",viewSet.toString().replaceAll(" ", "").replaceAll(",","/"))
                .httpOnly(true)
                .path("/")
                .domain(Config.DOMAIN)
                .build();

        return ResponseEntity.status(HttpStatus.OK)
                .header(HttpHeaders.SET_COOKIE, viewListCookie.toString())
                .body(responseDto);
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/{board_id}/posts")
    public ResponseEntity<Long> createPost(final @RequestBody @Valid PostCreateRequestDto postCreateRequestDto, @PathVariable("board_id") Long boardId,
                                           @CurrentSecurityContext(expression = "authentication") Authentication authentication) {
        if(boardId == 1 && !authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        log.info("title" + postCreateRequestDto.getTitle());
        log.info("content" + postCreateRequestDto.getContent());
        UserAuthDto auth = (UserAuthDto) authentication.getPrincipal();
        User user = userService.findByUsername(auth.getUsername());
        Long ret = postService.createPost(postCreateRequestDto, boardId, user);
        return ResponseEntity.status(HttpStatus.OK).body(ret);
    }

    @PreAuthorize("isAuthenticated()")
    @PatchMapping("/{board_id}/posts/{post_id}")
    public ResponseEntity<Long> updatePost(final @RequestBody @Valid PostUpdateRequestDto postUpdateRequestDto, @PathVariable("board_id") Long boardId,
                                           @PathVariable("post_id") Long postId, @CurrentSecurityContext(expression = "authentication") Authentication authentication) {
        if(boardId == 1 && !authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        UserAuthDto auth = (UserAuthDto) authentication.getPrincipal();
        User user = userService.findByUsername(auth.getUsername());
        Long ret = postService.updatePost(postUpdateRequestDto, boardId, postId, user);
        return ResponseEntity.status(HttpStatus.OK).body(ret);
    }

    @PreAuthorize("isAuthenticated()")
    @DeleteMapping("/{board_id}/posts/{post_id}")
    public ResponseEntity<Long> deletePost(@PathVariable("board_id") Long boardId, @PathVariable("post_id") Long postId,
                                           @CurrentSecurityContext(expression = "authentication") Authentication authentication) {
        if(boardId == 1 && !authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        UserAuthDto auth = (UserAuthDto) authentication.getPrincipal();
        User user = userService.findByUsername(auth.getUsername());
        Long ret = postService.deletePost(boardId, postId, user);
        return ResponseEntity.status(HttpStatus.OK).body(ret);
    }
}