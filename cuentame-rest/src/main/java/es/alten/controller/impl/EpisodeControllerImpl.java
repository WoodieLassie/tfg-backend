package es.alten.controller.impl;

import com.fasterxml.jackson.annotation.JsonView;
import es.alten.bo.EpisodeBO;
import es.alten.controller.EpisodeController;
import es.alten.domain.Episode;
import es.alten.dto.EpisodeDTO;
import es.alten.rest.impl.RestControllerImpl;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;


//TODO: Ajustar el tamaño de pagina para que sea adecuado
@RestController
@RequestMapping("/api/episodes")
@Tag(name = "episodes")
public class EpisodeControllerImpl extends RestControllerImpl<Episode, EpisodeDTO, Long, EpisodeBO>
    implements EpisodeController {

//    @Override
//    @GetMapping
//    public ResponseEntity<List<EpisodeDTO>> findAll() {
//        List<Episode> episodeList = bo.findAll();
//        List<EpisodeDTO> convertedEpisodeList = new ArrayList<>();
//        for (Episode episode : episodeList) {
//            EpisodeDTO episodeDTO = new EpisodeDTO();
//            episodeDTO.loadFromDomain(episode);
//            convertedEpisodeList.add(episodeDTO);
//        }
//        return ResponseEntity.ok(convertedEpisodeList);
//    }
//
    @GetMapping(value = "/sorted/{seasonNum}", params = {"title", "episodeNum"})
    public ResponseEntity<Page<EpisodeDTO>> findAllSortedAndPaged(
            @PathVariable Long seasonNum,
            @RequestParam String title,
            @RequestParam Integer episodeNum,
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "3") Integer size,
            Pageable pageable) {
    List<Episode> episodeList = bo.findAllSortedAndPaged(seasonNum, title, episodeNum);
    List<EpisodeDTO> convertedEpisodeList = new ArrayList<>();
    for (Episode episode : episodeList) {
        EpisodeDTO episodeDTO = new EpisodeDTO();
        episodeDTO.loadFromDomain(episode);
        convertedEpisodeList.add(episodeDTO);
    }
    int pageEnd = Math.min((page + size), convertedEpisodeList.size());
    Page<EpisodeDTO> episodes = new PageImpl<>(convertedEpisodeList.subList(page,pageEnd), pageable, convertedEpisodeList.size());
    return ResponseEntity.ok(episodes);
  }
//
//  @Override
//  @GetMapping("/{id}")
//  public ResponseEntity<EpisodeDTO> findOne(@PathVariable Long id) {
//      Episode episode = bo.findOne(id);
//      EpisodeDTO convertedEpisode = new EpisodeDTO();
//      convertedEpisode.loadFromDomain(episode);
//      return ResponseEntity.ok(convertedEpisode);
//  }
}
