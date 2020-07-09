import com.google.common.collect.ImmutableList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ScratchTest {

    Scratch scratch = new Scratch();
    @BeforeEach
    void setup() {
    }

    @Test
    public void singleRequest() throws InterruptedException {
        String url = "foo1=bar1&foo2=bar2";
        List<String> queries = ImmutableList.of(url);
        assertEquals(ImmutableList.of("https://postman-echo.com/get?foo1=bar1&foo2=bar2"), scratch.getResponses(queries));
    }

    @Test
    public void twoRequests() throws InterruptedException {
        String url1 = "foo1=bar1";
        String url2 = "foo2=bar2";
        List<String> queries = ImmutableList.of(url1, url2);
        assertEquals(ImmutableList.of("https://postman-echo.com/get?foo1=bar1",
                "https://postman-echo.com/get?foo2=bar2"), scratch.getResponses(queries));
    }

    @Test
    public void multipleRequests() throws InterruptedException {
        String url1 = "foo1=bar1";
        String url2 = "foo2=bar2";
        String url3 = "foo3=bar3";
        String url4 = "foo4=bar4";
        String url5 = "foo5=bar5";
        List<String> queries = ImmutableList.of(url1, url3, url2, url5, url4);
        String constant = "https://postman-echo.com/get?";
        assertEquals(ImmutableList.of(constant + url1, constant + url3,
                constant + url2, constant + url5, constant + url4), scratch.getResponses(queries));
    }
}