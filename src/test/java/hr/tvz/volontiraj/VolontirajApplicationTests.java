package hr.tvz.volontiraj;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class VolontirajApplicationTests {

    @Autowired
    private ApplicationContext context;

    @Test
    void main_shouldRunWithoutExceptions() {
        VolontirajApplication.main(new String[] {});
    }

    @Test
    void contextLoads() {
        assertNotNull(context);
    }

    @Test
    void restTemplateBeanExists() {
        RestTemplate restTemplate = context.getBean(RestTemplate.class);
        assertNotNull(restTemplate);
    }

}
