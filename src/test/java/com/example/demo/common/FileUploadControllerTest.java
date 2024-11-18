//package com.example.demo.common;
//
////import com.example.demo.common.controller.FileUploadController;
////import com.example.demo.common.dto.FileUploadDTO;
////import com.example.demo.common.service.FileUploadService;
//import lombok.extern.log4j.Log4j2;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
//import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
//import org.springframework.context.annotation.Import;
//import org.springframework.http.ResponseEntity;
//import org.springframework.test.annotation.Commit;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.util.Collections;
//import java.util.HashSet;
//import java.util.List;
//
//import static org.assertj.core.api.Assertions.assertThat;
//
///**
// * @DataJpaTest와 @Import를 사용하여 필요한 빈만 등록하는 방식으로 테스트를 수행합니다.
// */
//@Log4j2
//@DataJpaTest
//@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
//@Import(FileUploadService.class) // FileUploadService를 테스트 컨텍스트에 수동으로 등록
//public class FileUploadControllerTest {
//
//    @Autowired
//    private FileUploadService fileUploadService; // 실제 FileUploadService 사용
//
//    private FileUploadController fileUploadController; // 컨트롤러 인스턴스
//
//    private FileUploadDTO fileUploadDTO; // 테스트용 FileUploadDTO 객체
//
//    @BeforeEach
//    public void setup() {
//        // 실제 서비스 계층을 사용하는 컨트롤러 생성
//        fileUploadController = new FileUploadController(fileUploadService);
//
//        // 테스트용 FileUploadDTO 객체 생성
//        fileUploadDTO = FileUploadDTO.builder()
//                .uno(1L)
//                .name("Test File Upload")
//                .attachFiles(new HashSet<>(Collections.singleton(
//                        new FileUploadDTO.AttachFileDTO(0, "testfile.txt")
//                )))
//                .build();
//    }
//
//    @Test
//    @Transactional
//    @Commit
//    public void testUploadFile() {
//        // 파일 업로드를 수행하고 결과를 검증
//        ResponseEntity<FileUploadDTO> response = fileUploadController.uploadFile(fileUploadDTO);
//
//        // 응답이 예상대로 200 OK 상태인지 확인
//        assertThat(response.getStatusCodeValue()).isEqualTo(200);
//        // 응답 내용이 예상대로 반환되었는지 확인
//        assertThat(response.getBody()).isNotNull();
//        assertThat(response.getBody().getName()).isEqualTo("Test File Upload");
//        log.info("Uploaded File: " + response.getBody());
//    }
//
//    @Test
//    @Transactional
//    @Commit
//    public void testGetAllFileUploads() {
//        // 먼저 파일 업로드를 수행하여 테스트 데이터를 준비
//        fileUploadController.uploadFile(fileUploadDTO);
//
//        // 모든 파일 업로드 데이터를 조회
//        ResponseEntity<List<FileUploadDTO>> response = fileUploadController.getAllFileUploads();
//
//
//        assertThat(response.getStatusCodeValue()).isEqualTo(200);
//        assertThat(response.getBody()).isNotEmpty();
//        assertThat(response.getBody().get(0).getName()).isEqualTo("Test File Upload");
//        log.info("Retrieved All Files: " + response.getBody());
//    }
//}
