package com.demo;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

class AppTest {

    @Test
    void homeReturnsGreeting() {
        App app = new App();
        assertEquals(true, app.home().contains("Jenkins"));
    }

    @Test
    void healthReturnsOk() {
        App app = new App();
        assertEquals("OK", app.health());
    }
}
