package com.example.walkingmate_back.board.repository;

import com.example.walkingmate_back.board.entity.Board;
import com.example.walkingmate_back.user.entity.UserEntity;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class BoardRepositoryTest {

    @Autowired
    EntityManager em;

    @Autowired
    BoardRepository boardRepository;

    @BeforeAll
    static void beforeAll() {
        System.out.println("## BeforeAll Annotation 호출 ##");
        System.out.println();
    }

    @AfterAll
    static void afterAll() {
        System.out.println("## afterAll Annotation 호출 ##");
        System.out.println();
    }

    @BeforeEach
    void beforeEach() {
        System.out.println("## beforeEach Annotation 호출 ##");
        System.out.println();
    }

    @AfterEach
    void afterEach() {
        System.out.println("## afterEach Annotation 호출 ##");
        System.out.println();
    }

    @Test
    @DisplayName("게시글 저장 테스트")
    public void createBoardTest() {
        System.out.println("## createBoardTest 시작 ##");
        System.out.println();

        Board board = new Board();
        UserEntity user = new UserEntity();
        user.setId("bbb");
        board.setTitle("테스트 제목");
        board.setContent("테스트 내용");
        board.setUser(user);

        Board savedBoard = boardRepository.save(board);
        assertEquals(savedBoard.getTitle(), "테스트 제목");
    }

    @Test
    @DisplayName("게시글을 반환하는지 확인")
    public void boardListTest() {
        System.out.println("## boardListTest 시작 ##");
        System.out.println();

        List<Board> result = boardRepository.findAllBoard(PageRequest.of(0, 10, Sort.by("id").descending()));

        assertEquals(result.size(), 2);
    }

}
