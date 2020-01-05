package io.github.behoston.meloooncensor.filter;

import io.github.behoston.meloooncensor.config.Configuration;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Collections;

public class ClassicFilterTest {

    @Test
    public void testMessageViolatesPolicy() {
        Configuration configuration = Mockito.mock(Configuration.class);
        Mockito.when(configuration.getCensor()).thenReturn(Collections.singletonList("fuck"));
        Mockito.when(configuration.getCharString()).thenReturn("*");
        ClassicFilter filter = new ClassicFilter(configuration);

        String message = "Fucking hate this server!";
        Assertions.assertTrue(filter.violatesPolicy(message));
        Assertions.assertEquals("******* hate this server!", filter.censorMessage(message));
    }

}
