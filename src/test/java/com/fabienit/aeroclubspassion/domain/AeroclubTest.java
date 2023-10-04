package com.fabienit.aeroclubspassion.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.fabienit.aeroclubspassion.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AeroclubTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Aeroclub.class);
        Aeroclub aeroclub1 = new Aeroclub();
        aeroclub1.setId(1L);
        Aeroclub aeroclub2 = new Aeroclub();
        aeroclub2.setId(aeroclub1.getId());
        assertThat(aeroclub1).isEqualTo(aeroclub2);
        aeroclub2.setId(2L);
        assertThat(aeroclub1).isNotEqualTo(aeroclub2);
        aeroclub1.setId(null);
        assertThat(aeroclub1).isNotEqualTo(aeroclub2);
    }
}
