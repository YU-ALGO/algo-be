package com.stock.yu.downbitbe.board.application;

import com.stock.yu.downbitbe.board.domain.board.Board;
import com.stock.yu.downbitbe.board.domain.board.BoardRepository;
import com.stock.yu.downbitbe.board.domain.post.*;
import com.stock.yu.downbitbe.user.entity.LoginType;
import com.stock.yu.downbitbe.user.entity.User;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PostServiceTest {
    @InjectMocks
    PostService postService;

    @Mock
    BoardRepository boardRepository;

    @Mock
    PostRepository postRepository;

    User mockUser = null;
    Board mockBoard = null;
    Post mockPost = null;

    @BeforeEach
    public void setUp() {
        mockUser = User.builder()
                .username("test@test")
                .password("1234")
                .loginType(LoginType.LOCAL)
                .nickname("test")
                .build();

        mockBoard = Board.builder()
                .name("test board")
                .build();

        ReflectionTestUtils.setField(mockUser, "id", 1L);
        ReflectionTestUtils.setField(mockBoard, "id", 1L);
    }

    @Nested
    @DisplayName("게시글 검색")
    class FindPostById {
        Long mockPostId = null;
        Long mockBoardId = null;

        @BeforeEach
        public void setId() {
            mockBoardId = 1L;
            mockPostId = 2L;
        }

        @Nested
        @DisplayName("성공 케이스")
        class SuccessCase {
            @Test
            @DisplayName("검색 성공")
            void Success() {
                //given
                String mockTitle = "title test";
                String mockContent = "content test";
                mockPost = Post.builder()
                        .title(mockTitle)
                        .content(mockContent)
                        .user(mockUser)
                        .board(mockBoard)
                        .build();
                ReflectionTestUtils.setField(mockPost, "id", mockPostId);
                when(boardRepository.findById(mockBoardId)).thenReturn(Optional.ofNullable(mockBoard));
                when(postRepository.findById(mockPostId)).thenReturn(Optional.ofNullable(mockPost));
                PostResponseDto mockDto = new PostResponseDto(mockPost);

                //when
                PostResponseDto expectedMockDto = postService.findPostByPostId(mockBoardId, mockPostId);

                //then
                assertThat(expectedMockDto).usingRecursiveComparison().isEqualTo(mockDto);
            }
        }

        @Nested
        @DisplayName("실패 케이스")
        class FailCase {
            @Test
            @DisplayName("게시판이 존재하지 않는 경우")
            void BoardNotExist() {
                //given
                when(boardRepository.findById(any())).thenReturn(Optional.empty());

                //when
                IllegalArgumentException exception = Assertions.assertThrows(IllegalArgumentException.class, () -> postService.findPostByPostId(mockBoardId, mockPostId));

                //then
                assertThat(exception.getMessage()).isEqualTo("게시판이 존재하지 않습니다.");
            }

            @Test
            @DisplayName("게시글이 존재하지 않는 경우")
            void PostNotExist() {
                //given
                when(boardRepository.findById(mockBoardId)).thenReturn(Optional.ofNullable(mockBoard));
                when(postRepository.findById(any())).thenReturn(Optional.empty());

                //when
                IllegalArgumentException exception = Assertions.assertThrows(IllegalArgumentException.class, () -> postService.findPostByPostId(mockBoardId, mockPostId));

                //then
                assertThat(exception.getMessage()).isEqualTo("게시글이 존재하지 않습니다.");
            }
        }

    }


    @Test
    void findAllPostsById() {
    }

    @Nested
    @DisplayName("게시글 생성")
    class CreatePost {
        PostCreateRequestDto mockPostCreateDto = null;

        @Nested
        @DisplayName("성공 케이스")
        class SuccessCase {
            @Test
            @DisplayName("게시물 생성 성공")
            void Success() {
                //given
                Long expectedId = 1L;
                mockPostCreateDto = new PostCreateRequestDto("title test", "content test");
                mockPost = mockPostCreateDto.toEntity(mockBoard, mockUser);
                ReflectionTestUtils.setField(mockPost, "id", 1L);
                when(boardRepository.findById(mockBoard.getId())).thenReturn(Optional.ofNullable(mockBoard));
                when(postRepository.save(any(Post.class))).thenReturn(mockPost);

                //when
                Long mockPostId = postService.createPost(mockPostCreateDto, mockBoard.getId(), mockUser);

                //then
                assertThat(expectedId).isEqualTo(mockPostId);
            }
        }

        @Nested
        @DisplayName("실패 케이스")
        class FailCase {
            @Test
            @DisplayName("게시판이 존재하지 않는 경우")
            void BoardNotExist() {
                //given
                when(boardRepository.findById(any())).thenReturn(Optional.empty());

                //when
                IllegalArgumentException exception = Assertions.assertThrows(IllegalArgumentException.class, () -> postService.createPost(mockPostCreateDto, mockBoard.getId(), mockUser));

                //then
                assertThat(exception.getMessage()).isEqualTo("게시판이 존재하지 않습니다.");
            }
        }
    }

    @Nested
    @DisplayName("게시글 수정")
    class UpdatePost {
        Long mockPostId = null;
        Long mockBoardId = null;

        PostUpdateRequestDto mockPostUpdateDto = null;

        @BeforeEach
        public void setId() {
            String mockTitle = "title test";
            String mockContent = "content test";
            mockBoardId = 1L;
            mockPostId = 2L;
            mockPost = Post.builder()
                    .title(mockTitle)
                    .content(mockContent)
                    .user(mockUser)
                    .board(mockBoard)
                    .build();
            ReflectionTestUtils.setField(mockPost, "id", mockPostId);

            mockPostUpdateDto = new PostUpdateRequestDto(mockBoardId, mockPostId, mockTitle + " update", mockContent + " update");
        }

        @Nested
        @DisplayName("성공 케이스")
        class SuccessCase {
            @Test
            @DisplayName("게시물 수정 성공")
            void Success() {
                //given

                when(boardRepository.findById(mockBoardId)).thenReturn(Optional.ofNullable(mockBoard));
                when(postRepository.findById(mockPostId)).thenReturn(Optional.ofNullable(mockPost));
                when(postRepository.save(any(Post.class))).thenAnswer(post -> {
                    Post p = post.getArgument(0);
                    ReflectionTestUtils.setField(p, "id", mockPostId);
                    return p;
                });


                //when
                Long expectedPostId = postService.updatePost(mockPostUpdateDto, mockBoardId, mockPostId, mockUser);


                //then
                assertThat(expectedPostId).isEqualTo(mockPostId);

            }
        }

        @Nested
        @DisplayName("실패 케이스")
        class FailCase {
            @Test
            @DisplayName("게시판이 존재하지 않는 경우")
            void BoardNotExist() {
                //given
                when(boardRepository.findById(mockBoardId)).thenReturn(Optional.empty());

                //when
                IllegalArgumentException exception = Assertions.assertThrows(IllegalArgumentException.class, () -> postService.updatePost(mockPostUpdateDto, mockBoardId, mockPostId, mockUser));

                //then
                assertThat(exception.getMessage()).isEqualTo("게시판이 존재하지 않습니다.");

            }

            @Test
            @DisplayName("게시글이 존재하지 않는 경우")
            void PostNotExist() {
                //given
                when(boardRepository.findById(mockBoardId)).thenReturn(Optional.ofNullable(mockBoard));
                when(postRepository.findById(mockPostId)).thenReturn(Optional.empty());

                //when
                IllegalArgumentException exception = Assertions.assertThrows(IllegalArgumentException.class, () -> postService.updatePost(mockPostUpdateDto, mockBoardId, mockPostId, mockUser));

                //then
                assertThat(exception.getMessage()).isEqualTo("게시글이 존재하지 않습니다.");
            }

            @Test
            @DisplayName("작성자와 사용자가 일치하지 않는 경우")
            void CreatorNotEqual() {
                //given
                when(boardRepository.findById(mockBoardId)).thenReturn(Optional.ofNullable(mockBoard));
                when(postRepository.findById(mockPostId)).thenReturn(Optional.ofNullable(mockPost));
                User otherUser = User.builder()
                        .username("other@test")
                        .password("1234")
                        .loginType(LoginType.LOCAL)
                        .nickname("test")
                        .build();
                ReflectionTestUtils.setField(otherUser, "id", 2L);

                //when
                RuntimeException exception = Assertions.assertThrows(RuntimeException.class, () -> postService.updatePost(mockPostUpdateDto, mockBoardId, mockPostId, otherUser));

                //then
                assertThat(exception.getMessage()).isEqualTo("작성자와 일치하지 않습니다.");
            }
        }
    }


    @Nested
    @DisplayName("게시글 삭제")
    class DeletePost{
        @Nested
        @DisplayName("성공 케이스")
        class Success{
            @Test
            void deletePost() {
            }
        }
    }

}