package vinci.be.backend;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import vinci.be.backend.models.TextHelloWorld;

import static org.junit.jupiter.api.Assertions.assertEquals;

/** The Spring Cicd Demo Application Tests. */
@SpringBootTest(classes = TextHelloWorld.class)
class TestJava {
    @Test
    void contextLoads() {
        assertEquals(3, 1+2);
    }
}