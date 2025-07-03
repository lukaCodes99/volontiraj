package hr.tvz.volontiraj.service.implementation;

import hr.tvz.volontiraj.service.HttpClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.*;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
public class SupabaseServiceImplTest {

    @InjectMocks
    private SupabaseServiceImpl supabaseService;

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private HttpClient  httpClient;

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

        ArgumentCaptor<HttpEntity> requestCaptor = ArgumentCaptor.forClass(HttpEntity.class);

        when(httpClient.put(anyString(), requestCaptor.capture())).thenReturn(response);

        List<String> result = supabaseService.uploadImages(List.of(mockFile));

        assertEquals(1, result.size());
        assertTrue(result.getFirst().contains("https://xyz.supabase.co/storage/v1/object/test-bucket/"));
    }

    @Test
    void uploadImages_whenFileIsEmpty() throws IOException {
        MultipartFile emptyFile = mock(MultipartFile.class);
        when(emptyFile.isEmpty()).thenReturn(true);

        List<String> result = supabaseService.uploadImages(List.of(emptyFile));

        assertTrue(result.isEmpty());

        verify(httpClient, never()).put(anyString(), any());
    }


    @Test
    void uploadImages_shouldThrowRuntimeException() throws IOException {
        MultipartFile mockFile = mock(MultipartFile.class);
        when(mockFile.isEmpty()).thenReturn(false);
        when(mockFile.getOriginalFilename()).thenReturn("test.jpg");
        when(mockFile.getContentType()).thenReturn("image/jpeg");
        when(mockFile.getBytes()).thenReturn("dummyData".getBytes());

     //   ResponseEntity<String> badResponse = new ResponseEntity<>("Error uploading", HttpStatus.BAD_REQUEST);

        when(httpClient.put(anyString(), any()))
                .thenThrow(new RuntimeException("No files found"));

        IOException exception = assertThrows(IOException.class, () -> supabaseService.uploadImages(List.of(mockFile)));

        assertEquals("Upload failed: No files found", exception.getMessage());
    }

}
