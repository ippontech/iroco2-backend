package fr.ippon.iroco2.domain.commons.model;

import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class PayloadTest {

    @Test
    void should_return_null_when_configuredValues_is_null() {
        // given
        var payload = new Payload(null, null, null, null, 0, null);

        //when
        var actual = payload.getValue(PayloadConfiguration.S3_STORAGE);

        // then
        assertNull(actual);
    }

    @Test
    void should_return_value_when_configuredValues_is_not_null() {
        // given
        var payload = new Payload(null, null, null, null, 0, Map.of(PayloadConfiguration.S3_STORAGE, "test"));

        //when
        var actual = payload.getValue(PayloadConfiguration.S3_STORAGE);

        // then
        assertEquals("test", actual);
    }
}