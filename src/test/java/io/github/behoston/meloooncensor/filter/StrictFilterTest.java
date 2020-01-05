package io.github.behoston.meloooncensor.filter;

import io.github.behoston.meloooncensor.config.Configuration;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Collections;

public class StrictFilterTest {

    @Test
    public void testMessageNotSendWhenOneWordContainsBannedString() {
        Configuration configuration = Mockito.mock(Configuration.class);
        Mockito.when(configuration.getCensor()).thenReturn(Collections.singletonList("fuck"));
        StrictFilter filter = new StrictFilter(configuration);
        String message = "Fucking server!";

        Assertions.assertTrue(filter.violatesPolicy(message));
        Assertions.assertNull(filter.censorMessage(message));
    }
}
