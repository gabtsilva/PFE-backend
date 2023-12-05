package vinci.be.backend;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

/** The Spring Cicd Demo Application Tests. */
@SpringBootTest(classes = HelloWorldService.class)
class TestJava {

    @Mock
    private HelloWorldControler priceProxy;

    @InjectMocks
    private HelloWorldService matchingService;

    @Test
    void contextLoads() {
        assertEquals(3, 1+2);
    }
}