package hr.tvz.volontiraj.service.implementation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
public class SupabaseServiceImplTest {

    @InjectMocks
    private SupabaseServiceImpl supabaseService;

    @Mock
    private RestTemplate restTemplate;

    @BeforeEach
    void setup() {
        ReflectionTestUtils.setField(supabaseService, "supabaseUrl", "https://xyz.supabase.co");
        ReflectionTestUtils.setField(supabaseService, "supabaseKey", "fakeKey");
        ReflectionTestUtils.setField(supabaseService, "supabaseBucket", "test-bucket");
    }

    @Test
    void uploadImages_shouldUploadSuccessfully() throws IOException {
        MultipartFile mockFile = mock(MultipartFile.class);
        when(mockFile.isEmpty()).thenReturn(false);
        when(mockFile.getOriginalFilename()).thenReturn("test.jpg");
        when(mockFile.getContentType()).thenReturn("image/jpeg");
        when(mockFile.getBytes()).thenReturn("dummyData".getBytes());

        ResponseEntity<String> response = new ResponseEntity<>("OK", HttpStatus.OK);

        when(restTemplate.exchange(anyString(), eq(HttpMethod.PUT), any(HttpEntity.class), eq(String.class)))
                .thenReturn(response);

        List<String> result = supabaseService.uploadImages(List.of(mockFile));

        assertEquals(1, result.size());
        assertTrue(result.getFirst().contains("https://xyz.supabase.co/storage/v1/object/test-bucket/"));
    }

}
