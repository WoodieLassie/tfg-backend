package es.alten.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import es.alten.bo.ImageBO;
import es.alten.controller.impl.ImageControllerImpl;
import es.alten.domain.Image;
import es.alten.dto.ImageDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMultipartHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ComponentScan(basePackages = "es.alten.cuentame.*")
@Import(ImageControllerImpl.class)
@AutoConfigureMockMvc(addFilters = false)
@ContextConfiguration(classes = ImageControllerImpl.class)
@WebMvcTest(ImageControllerImpl.class)
class ImageControllerImplTest {
  @Autowired private MockMvc mockMvc;
  @MockBean private ImageBO imageBO;
  @Autowired private ObjectMapper objectMapper;

  private MockMultipartFile mockImageData;
  private ImageDTO mockImage;

  @BeforeEach
  void setUp() {
    mockImageData =
        new MockMultipartFile("image", "filename.png", "image/png", "some png".getBytes());
    mockImage = new ImageDTO();
    mockImage.setId(1L);
    mockImage.setName("test");
    mockImage.setType("type");
    mockImage.setImageData("some data".getBytes());
  }

  @Test
  void findAllTest() throws Exception {
    mockImage.setImageUrl("http://localhost/api/images/1");
    List<Image> mockImageList = new ArrayList<>(List.of(mockImage.obtainDomainObject()));
    List<ImageDTO> mockImageDTOList = new ArrayList<>(List.of(mockImage));
    given(imageBO.findAll()).willReturn(mockImageList);
    ResultActions response = mockMvc.perform(get("/api/images"));
    response
        .andExpect(status().isOk())
        .andDo(print())
        .andExpect(content().json(objectMapper.writeValueAsString(mockImageDTOList)));
  }

  @Test
  void findByIdTest() throws Exception {
    mockImage.setImageUrl("http://localhost/api/images/1");
    Image mockImageEntity = mockImage.obtainDomainObject();
    given(imageBO.findById(mockImageEntity.getId())).willReturn(mockImage.getImageData());
    ResultActions response = mockMvc.perform(get("/api/images/{id}", mockImage.getId()));
    response
        .andExpect(status().isOk())
        .andDo(print())
        .andExpect(content().bytes(mockImage.getImageData()));
  }

  @Test
  void findByNameTest() throws Exception {
    mockImage.setImageUrl("http://localhost/api/images/1");
    List<Image> mockImageList = new ArrayList<>(List.of(mockImage.obtainDomainObject()));
    List<ImageDTO> mockImageDTOList = new ArrayList<>(List.of(mockImage));
    given(imageBO.findByName(mockImage.getName())).willReturn(mockImageList);
    ResultActions response =
        mockMvc.perform(get("/api/images/sorted").param("name", mockImage.getName()));
    response
        .andExpect(status().isOk())
        .andDo(print())
        .andExpect(content().json(objectMapper.writeValueAsString(mockImageDTOList)));
  }

  @Test
  void addTest() throws Exception {
    given(imageBO.save(any(Image.class))).willAnswer(invocation -> invocation.getArgument(0));
    ResultActions response =
        mockMvc.perform(MockMvcRequestBuilders.multipart("/api/images").file(mockImageData));
    response.andDo(print()).andExpect(status().isCreated());
  }

  @Test
  void updateTest() throws Exception {
    Image dbImage = new Image();
    dbImage.setId(1L);
    MockMultipartFile newImage =
        new MockMultipartFile("image", "filename2.png", "image/png", "some png2".getBytes());
    MockMultipartHttpServletRequestBuilder builder =
        MockMvcRequestBuilders.multipart("/api/images/{id}", dbImage.getId()).file(newImage);
    builder.with(
        request -> {
          request.setMethod("PATCH");
          return request;
        });
    given(imageBO.findOne(1L)).willReturn(dbImage);
    given(imageBO.save(any(Image.class))).willAnswer(invocation -> invocation.getArgument(0));
    ResultActions response = mockMvc.perform(builder.file(newImage));
    response.andDo(print()).andExpect(status().isNoContent());
  }

  @Test
  void deleteTest() throws Exception {
    Image mockDbImage = mockImage.obtainDomainObject();
    mockDbImage.setId(1L);
    given(imageBO.exists(mockImage.getId())).willReturn(true);
    willDoNothing().given(imageBO).delete(mockDbImage.getId());
    ResultActions response = mockMvc.perform(delete("/api/images/{id}", mockDbImage.getId()));
    response.andExpect(status().isNoContent()).andDo(print());
  }
}
