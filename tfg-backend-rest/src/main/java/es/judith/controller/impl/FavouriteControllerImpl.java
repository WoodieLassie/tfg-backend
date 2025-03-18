package es.judith.controller.impl;

import es.judith.bo.FavouriteBO;
import es.judith.bo.ShowBO;
import es.judith.bo.UserBO;
import es.judith.controller.FavouriteController;
import es.judith.domain.Favourite;
import es.judith.domain.Role;
import es.judith.domain.Show;
import es.judith.dto.FavouriteDTO;
import es.judith.dto.FavouriteInputDTO;
import es.judith.dto.SeasonDTO;
import es.judith.exceptions.BadInputException;
import es.judith.exceptions.NotExistingIdException;
import es.judith.exceptions.NotFoundException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api/favourites")
@Tag(name = "favourites")
public class FavouriteControllerImpl extends GenericControllerImpl implements FavouriteController {

  private static final Logger LOG = LoggerFactory.getLogger(FavouriteControllerImpl.class);
  private final FavouriteBO bo;
  private final UserBO userBO;
  private final ShowBO showBO;

  public FavouriteControllerImpl(FavouriteBO bo, UserBO userBO, ShowBO showBO) {
      super(userBO);
      this.bo = bo;
      this.userBO = userBO;
      this.showBO = showBO;
  }

  @Override
  @Operation(method = "GET", summary = "Get all favourites by user id")
  @ApiResponse(
      responseCode = "200",
      description = "OK",
      content = {
        @Content(
            mediaType = "application/json",
            array = @ArraySchema(schema = @Schema(implementation = FavouriteDTO.class)))
      })
  @GetMapping("/{userId}")
  public ResponseEntity<List<FavouriteDTO>> findAllByUser(@PathVariable Long userId) {
    LOG.debug("Fetching results with user id {}", userId);
    List<Favourite> favouriteList = bo.findAllByUser(userId);
    List<FavouriteDTO> convertedFavouriteList = new ArrayList<>();
    for (Favourite favourite : favouriteList) {
      FavouriteDTO favouriteDTO = new FavouriteDTO();
      favouriteDTO.loadFromDomain(favourite);
      convertedFavouriteList.add(favouriteDTO);
    }
    return ResponseEntity.ok(convertedFavouriteList);
  }

  @Override
  @Operation(method = "POST", summary = "Save a favourite")
  @ApiResponse(
      responseCode = "201",
      description = "Created",
      content = {@Content(schema = @Schema(hidden = true))})
  @SecurityRequirement(name = "Authorization")
  @PostMapping
  public ResponseEntity<Favourite> add(@RequestBody FavouriteInputDTO favouriteDTO) {
    if (!favouriteDTO.allFieldsArePresent()) {
      throw new BadInputException("All fields must be present in request body");
    }
    Show show = showBO.findOne(favouriteDTO.getShowId());
    if (show == null) {
      throw new NotExistingIdException(
              "Show with id " + favouriteDTO.getShowId() + " does not exist");
    }
    Favourite favourite = favouriteDTO.obtainDomainObject();
    favourite.setShow(show);
    LOG.debug("FavouriteControllerImpl: Saving data");
    bo.save(favourite);
    return ResponseEntity.status(HttpStatus.CREATED).body(null);
  }

  @Override
  @Operation(method = "DELETE", summary = "Delete a favourite", parameters = @Parameter(ref = "id"))
  @ApiResponse(
      responseCode = "204",
      description = "No content",
      content = {@Content(schema = @Schema(hidden = true))})
  @ApiResponse(
      responseCode = "404",
      description = "Not found",
      content = @Content(schema = @Schema(hidden = true)))
  @SecurityRequirement(name = "Authorization")
  @DeleteMapping("/{id}")
  public ResponseEntity<FavouriteDTO> delete(@PathVariable Long id) {
    if (!bo.exists(id)) {
      throw new NotFoundException("Favourite with id " + id + " does not exist");
    }
    if (!Objects.equals(bo.findOne(id).getCreatedBy(), this.getCurrentUser().getId()) && this.getCurrentUser().getRole() != Role.ADMIN) {
      return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
    }
    LOG.debug("FavouriteControllerImpl: Deleting data with id {}", id);
    bo.delete(id);
    return ResponseEntity.noContent().build();
  }
}