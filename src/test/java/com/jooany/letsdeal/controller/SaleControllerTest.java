package com.jooany.letsdeal.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jooany.letsdeal.service.SaleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
public class SaleControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private SaleService saleService;

//    @Test
//    @WithMockUser
//    void 판매글작성_성공() throws Exception {
//
//        MockMultipartFile imageFile = new MockMultipartFile("image", "test.jpg", "image/jpeg", "test image".getBytes());
//        SaleSaveReq saleCreateReq = new SaleSaveReq(1L, "Test Title", "Test Contents", 10000);
//        String requestJson = objectMapper.writeValueAsString(saleCreateReq);
//
//        mockMvc.perform(MockMvcRequestBuilders.multipart("/api/v1/sales")
//                        .file(imageFile)
//                        .param("saleCreateReq", requestJson)
//                        .contentType("multipart/form-data")
//                ).andDo(print())
//                .andExpect(status().isOk());
//    }

//    @Test
//    @WithAnonymousUser
//    void 판매글작성_실패_존재하지않는사용자() throws Exception {
//
//    }
//    @Test
//    @WithMockUser
//    void 판매글작성_실패_() throws Exception {
//
//    }
//    @Test
//    @WithMockUser
//    void 판매글작성_실패_() throws Exception {
//
//    }
//    @Test
//    @WithMockUser
//    void 판매글작성_실패_() throws Exception {
//
//    }
//
//
//    @Test
//    @WithMockUser
//    void 판매글목록조회_성공() throws Exception {
//        mockMvc.perform(get("/api/v1/sales"))
//                .andDo(print())
//                .andExpect(status().isOk());
//    }
//
//    @Test
//    @DisplayName("판매글 상세 정보 조회 - 성공")
//    @WithMockUser
//    void 판매글상세조회_성공() throws Exception {
//        mockMvc.perform(get("/api/v1/sales/1"))
//                .andDo(print())
//                .andExpect(status().isOk());
//    }


}
