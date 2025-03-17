package com.kosta.big.board.service;

import com.kosta.big.board.domain.BoardEntity;
import com.kosta.big.board.repository.BoardRepository;
import com.kosta.big.replication.config.RoutingDataSource;
import com.zaxxer.hikari.HikariDataSource;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Fail.fail;
import static org.junit.jupiter.api.Assertions.assertNotNull;


@SpringBootTest
public class BoardServiceTest {


    @Autowired
    private BoardService boardService;

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private Environment environment;

    @Test
    @DisplayName("Slave_강제연결_테스트")
    void testDatabaseConnection(@Qualifier("slaveDataSource") DataSource dataSource) {
        try (Connection connection = dataSource.getConnection()) {
            System.out.println("Database connection successful!");
        } catch (SQLException e) {
            e.printStackTrace();
            fail("Database connection failed!");
        }
    }
    @Test
    @DisplayName("Master_DataSource_테스트")
    void masterDataSourceTest(@Qualifier("masterDataSource") final DataSource masterDataSource) {
        // given
        String url = environment.getProperty("spring.datasource.master.hikari.jdbc-url");
        String username = environment.getProperty("spring.datasource.master.hikari.username");
        String driverClassName = environment.getProperty("spring.datasource.master.hikari.driver-class-name");

        // when
        HikariDataSource hikariDataSource = (HikariDataSource) masterDataSource;
        assertNotNull(masterDataSource, "Master DataSource should not be null");

        System.out.println("Master HikariDataSource Check : " + hikariDataSource);
        System.out.println("Master Url Check : " + hikariDataSource.getJdbcUrl());
        System.out.println("Master UserName Check : " + hikariDataSource.getUsername());
        System.out.println("Master DriverClass Check : " + hikariDataSource.getDriverClassName());
        // then
        verifyOf(url, username, driverClassName, hikariDataSource);

    }

    @Test
    @DisplayName("Slave_DataSource_테스트")
    void slaveDataSourceTest(@Qualifier("slaveDataSource") final DataSource slaveDataSource) {
        // given
        String url = environment.getProperty("spring.datasource.slave.hikari.jdbc-url");
        String username = environment.getProperty("spring.datasource.slave.hikari.username");
        String driverClassName = environment.getProperty("spring.datasource.slave.hikari.driver-class-name");

        // when
        HikariDataSource hikariDataSource = (HikariDataSource) slaveDataSource;
        assertNotNull(slaveDataSource, "Slave DataSource should not be null");

        System.out.println("Slave HikariDataSource Check : " + hikariDataSource);
        System.out.println("Slave Url Check : " + hikariDataSource.getJdbcUrl());
        System.out.println("Slave UserName Check : " + hikariDataSource.getUsername());
        System.out.println("Slave DriverClass Check : " + hikariDataSource.getDriverClassName());


        // then
        verifyOf( url, username, driverClassName, hikariDataSource);

    }

    private void verifyOf(String url, String username, String driverClassName, HikariDataSource hikariDataSource) {
        assertThat(hikariDataSource.getJdbcUrl()).isEqualTo(url);
        assertThat(hikariDataSource.getUsername()).isEqualTo(username);
        assertThat(hikariDataSource.getDriverClassName()).isEqualTo(driverClassName);
    }

//    @BeforeEach
//    void setUp() {
//        // 테스트 데이터 삽입
//        BoardEntity boardEntity = new BoardEntity();
//        boardEntity.setTitle("Test Title SetUp");
//        boardEntity.setContents("Test Content SetUp");
//        boardRepository.save(boardEntity);
//    }

//    @Test
//    @DisplayName("게시글 쓰기 테스트")
//    void Create(){
//        BoardEntity entity= new BoardEntity();
//        entity.setTitle("Test Title Junit");
//        entity.setContents("Test Contents Junit");
//
//        BoardEntity read = boardService.svcBoardCreateOne(entity);
//        Assertions.assertNotNull(read, "create is null");
//        System.out.println("create : " + read);
//    }
//
//    @Test
//    @DisplayName("목록 테스트")
//    void Read(){
//        //given
//        String title = "Test Title Junit";
//        //when
//        List<BoardEntity> read = boardService.svcBoardRead(title);
//        //then
//        Assertions.assertNotNull(read,"리스트가 null입니다.");
//        Assertions.assertFalse(read.isEmpty(), "리스트가 비어 있습니다.");
//        Assertions.assertEquals(title, read.get(0).getTitle(), "title 이름이 다릅니다.");
//        System.out.println("read -> " + read);
//    }

//    @Test
//    @DisplayName("수정 테스트")
//    void Update(){
//        Optional<BoardEntity> updateBoard = boardRepository.findById(1L);
//        updateBoard.ifPresent( boardEntity -> {
//            boardEntity.setTitle("수정_게시글제목_1번");
//            boardEntity.setContents("수정_게시글내용_1번");
//            boardEntity.setRegdate(new Date());
//            boardRepository.save(boardEntity);
//        });
//    }
//
//    @Test
//    @DisplayName("삭제 테스트")
//    void Delete(){
//        Optional<BoardEntity> deleteBoard = boardRepository.findById(2542609762118434816L);
//        deleteBoard.ifPresent( boardEntity -> {
//            boardRepository.delete(boardEntity);
//        });
//
//        Optional<BoardEntity> failedDeleteBoard = boardRepository.findById(2542609762118434816L);
//        Assert.assertFalse(failedDeleteBoard.isPresent());
//
//    }

    //        List<BoardDto> createBoard = Arrays.asList(
//                BoardDto.builder().title("게시글제목_1번").contents("게시글내용_1번").build(),
//                BoardDto.builder().title("게시글제목_2번").contents("게시글내용_2번").build(),
//                BoardDto.builder().title("게시글제목_3번").contents("게시글내용_3번").build()
//        );
//        createBoard.forEach( it->{
//            BoardDto dto = boardService.svcBoardCreateOne(it);
//             dto.setTitle("게시글1");
//             dto.setContents("내용1");
//        });
}
