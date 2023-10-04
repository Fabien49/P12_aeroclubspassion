package com.fabienit.aeroclubspassion.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.fabienit.aeroclubspassion.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class RevisionTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Revision.class);
        Revision revision1 = new Revision();
        revision1.setId(1L);
        Revision revision2 = new Revision();
        revision2.setId(revision1.getId());
        assertThat(revision1).isEqualTo(revision2);
        revision2.setId(2L);
        assertThat(revision1).isNotEqualTo(revision2);
        revision1.setId(null);
        assertThat(revision1).isNotEqualTo(revision2);
    }
}
