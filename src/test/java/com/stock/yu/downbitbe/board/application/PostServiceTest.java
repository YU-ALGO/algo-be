package com.stock.yu.downbitbe.board.application;

import com.stock.yu.downbitbe.board.domain.board.Board;
import com.stock.yu.downbitbe.board.domain.board.BoardRepository;
import com.stock.yu.downbitbe.board.domain.post.Post;
import com.stock.yu.downbitbe.board.domain.post.PostCreateRequestDto;
import com.stock.yu.downbitbe.board.domain.post.PostRepository;
import com.stock.yu.downbitbe.user.entity.LoginType;
import com.stock.yu.downbitbe.user.entity.User;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
class PostServiceTest {
    @InjectMocks
    PostService postService;

    @Mock
    BoardRepository boardRepository;

    @Mock
    PostRepository postRepository;

    @Test
    void findPostByPostId() {
    }

    @Test
    void findAllPostsById() {
    }

    @Nested
    @DisplayName("게시물 생성")
    class CreatePost {

        @BeforeAll
        public void variableSetting(){
            mockUser = User.builder()
                    .userId("test@test")
                    .password("1234")
                    .type(LoginType.LOCAL)
                    .nickname("test")
                    .build();

            mockBoard = Board.builder()
                    .name("free board")
                    .build();

            ReflectionTestUtils.setField(mockUser, "id", 1L);
            ReflectionTestUtils.setField(mockBoard,"id", 1L);
            mockPostCreateDto = new PostCreateRequestDto("title test", "content test");
            mockPost = mockPostCreateDto.toEntity(mockBoard, mockUser);
            ReflectionTestUtils.setField(mockPost, "id", 1L);
        }

        User mockUser;
        Board mockBoard;
        Post mockPost;
        PostCreateRequestDto mockPostCreateDto;

        @Nested
        @DisplayName("성공 케이스")
        class SuccessCase{
            @Test
            @DisplayName("새로운 게시물 생성")
            void createPostSuccess(){
                Long testPostId = postService.createPost(mockPostCreateDto, mockBoard.getId(), mockUser);

            }
        }

        @Nested
        @DisplayName("실패 케이스")
        class FailCase{
            @Test
            @DisplayName("")
            void createPostFail(){

            }
        }
    }

    @Test
    void updatePost() {
    }

    @Test
    void deletePost() {
    }
}