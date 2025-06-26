package hr.tvz.volontiraj.controller;

import hr.tvz.volontiraj.service.SupabaseService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class UploadControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private SupabaseService supabaseService;

    @Test
    @WithMockUser("USER")
    public void testUploadImage() throws Exception {
        MockMultipartFile mockFile = new MockMultipartFile(
                "files",
                "test.jpg",
                "image/jpeg",
                "fake image content".getBytes()
        );
        List<String> urls = List.of("http://url.com/img1.jpg");

        when(supabaseService.uploadImages(anyList())).thenReturn(urls);

        mockMvc.perform(multipart("/api/images/upload")
                        .file(mockFile)
                        .with(request -> { request.setMethod("PUT"); return request; }))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0]").value("http://url.com/img1.jpg"));

        verify(supabaseService).uploadImages(anyList());
    }

    @Test
    @WithMockUser("USER")
    public void testUploadImage_ShouldThrowException() throws Exception {
        MockMultipartFile mockFile = new MockMultipartFile(
                "files",
                "test.jpg",
                "image/jpeg",
                "fake image content".getBytes()
        );

        when(supabaseService.uploadImages(anyList())).thenThrow(new IOException("upload failed"));

        mockMvc.perform(multipart("/api/images/upload")
                        .file(mockFile)
                        .with(request -> { request.setMethod("PUT"); return request; }))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("Failed to upload images: upload failed"));
    }


}
