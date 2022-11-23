package com.algo.yu.algobe.board.application;

import com.algo.yu.algobe.board.domain.post.PostImageTemp;
import com.algo.yu.algobe.board.domain.post.PostImageTempRepository;
import com.algo.yu.algobe.board.infra.PostImageHandler;
import com.algo.yu.algobe.board.domain.board.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RequiredArgsConstructor
@Service
public class PostImageService {
    private final BoardRepository boardRepository;
    private final PostImageTempRepository imageTempRepository;
    private final PostImageHandler postImageHandler;

    @Transactional
    public String uploadPostImage(Long boardId, String username, MultipartFile image) throws IOException {
        boardRepository.findById(boardId).orElseThrow(() -> new IllegalArgumentException("게시판이 존재하지 않습니다."));
        String filename = postImageHandler.parseImageInfo(image, username);
        return imageTempRepository.save(new PostImageTemp(filename)).getFilename();
    }
}
