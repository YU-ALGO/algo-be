package com.stock.yu.downbitbe.domain.post.service;

import com.stock.yu.downbitbe.domain.post.dto.PostDTO;
import com.stock.yu.downbitbe.domain.post.entity.Post;
import com.stock.yu.downbitbe.domain.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Log4j2
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {
    private final PostRepository postRepository;

    @Override
    public Long register(PostDTO postDTO) {
        Post post = dtoToEntity(postDTO);

        log.info("==================");
        log.info(post);

        postRepository.save(post);

        return post.getId();
    }

    @Override
    public PostDTO get(Long id) {
        Optional<Post> result = postRepository.getWithAuthor(id);
        if (result.isPresent())
            return entityToDTO(result.get());
        return null;
    }

    @Override
    public void modify(PostDTO postDTO) {
        Long id = postDTO.getId();
        Optional<Post> result = postRepository.findById(id);
        if (result.isPresent()) {
            Post post = result.get();

            post.changeTitle(postDTO.getTitle());
            post.changeContent(postDTO.getContent());
            postRepository.save(post);
        }
    }

    @Override
    public void remove(Long id) {
        postRepository.deleteById(id);
    }

    @Override
    public List<PostDTO> getAllWithAuthor(String authorId) {
        List<Post> postList = postRepository.getList(authorId);

        return postList.stream().map(post -> entityToDTO(post)).collect(Collectors.toList());
    }
}
