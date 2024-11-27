package es.alten.dto;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

class SeasonFilDTOTest {
    SeasonFilterDTO seasonFilterDTO;

    @BeforeEach
    void setUp() {
        seasonFilterDTO = new SeasonFilterDTO();
    }
    @Test
    void testObtainFilterSpecification() {
        seasonFilterDTO.obtainFilterSpecification();
        seasonFilterDTO.setSeasonNum(1);
        seasonFilterDTO.setDescription("desc");
        seasonFilterDTO.setEpisodes(new ArrayList<>());
        Assertions.assertEquals(1, seasonFilterDTO.getSeasonNum());
        Assertions.assertEquals("desc", seasonFilterDTO.getDescription());
        Assertions.assertEquals(List.of(), seasonFilterDTO.getEpisodes());
    }
}
