package es.alten.utils;

import es.alten.dto.ErrorDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.ArrayList;
import java.util.List;

class PageWrapperTest {

  private static final String DESC = "desc";
  private static final String URL = "url";

  @BeforeEach
  void setUp() throws Exception {}

  @Test
  <T> void testPageWrapper() {
    List<T> listT = new ArrayList<>();
    Page<T> results = new PageImpl<T>(listT);
    PageWrapper<T> p = new PageWrapper<T>(results, URL, null);
    Assertions.assertNotNull(p);
  }

  @Test
  <T> void testPageWrapper1() {

    List<ErrorDTO> listT = new ArrayList<>();
    listT.add(new ErrorDTO());
    listT.add(new ErrorDTO());
    listT.add(new ErrorDTO());
    listT.add(new ErrorDTO());
    listT.add(new ErrorDTO());
    listT.add(new ErrorDTO());

    PageRequest firstPageWithTwoElements = PageRequest.of(0, 2);

    Page<ErrorDTO> results = new PageImpl<ErrorDTO>(listT, firstPageWithTwoElements, 12);
    PageWrapper<ErrorDTO> p = new PageWrapper<ErrorDTO>(results, URL, DESC);
    Assertions.assertNotNull(p);
  }

  @Test
  <T> void testPageWrapper2() {

    List<ErrorDTO> listT = new ArrayList<>();
    listT.add(new ErrorDTO());
    listT.add(new ErrorDTO());
    listT.add(new ErrorDTO());
    listT.add(new ErrorDTO());
    listT.add(new ErrorDTO());
    listT.add(new ErrorDTO());

    PageRequest secondPageWithTwoElements = PageRequest.of(5, 5);

    Page<ErrorDTO> results = new PageImpl<ErrorDTO>(listT, secondPageWithTwoElements, 12);
    PageWrapper<ErrorDTO> p = new PageWrapper<ErrorDTO>(results, URL, DESC);
    Assertions.assertNotNull(p);
  }

  @Test
  <T> void testPageWrapper3() {

    List<ErrorDTO> listT = new ArrayList<>();
    for (int i = 0; i < 65; i++) {
      listT.add(new ErrorDTO());
    }

    PageRequest secondPageWithTwoElements = PageRequest.of(10, 20); // 10 -> page.getNumber

    Page<ErrorDTO> results = new PageImpl<ErrorDTO>(listT, secondPageWithTwoElements, 100);
    PageWrapper<ErrorDTO> p = new PageWrapper<ErrorDTO>(results, URL, DESC);
    Assertions.assertNotNull(p);
  }
}
