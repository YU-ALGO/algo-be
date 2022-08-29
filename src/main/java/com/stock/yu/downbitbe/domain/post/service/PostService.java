package com.stock.yu.downbitbe.domain.post.service;

import com.stock.yu.downbitbe.domain.post.dto.PostDTO;
import com.stock.yu.downbitbe.domain.post.entity.Post;
import com.stock.yu.downbitbe.domain.user.entity.User;

import java.util.List;

public interface PostService {
    Long register(PostDTO postDTO);
    PostDTO get(Long id);
    void modify(PostDTO postDTO);
    void remove(Long id);
    List<PostDTO> getAllWithAuthor(String authorId);
    default Post dtoToEntity(PostDTO postDTO) {
        Post post = Post.builder()
                .id(postDTO.getId())
                .title(postDTO.getTitle())
                .content(postDTO.getContent())
                .author(User.builder().userId(postDTO.getAuthorId()).build())
                .build();

        return post;
    }
    default PostDTO entityToDTO(Post post) {
        PostDTO postDTO = PostDTO.builder()
                .id(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .authorId(post.getAuthor().getUserId())
                .regDate(post.getCreatedAt())
                .modDate(post.getModifiedAt())
            .build();

        return postDTO;
    }
}
