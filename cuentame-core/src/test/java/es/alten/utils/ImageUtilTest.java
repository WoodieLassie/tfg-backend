package es.alten.utils;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class ImageUtilTest {

  private final byte[] imageData = "image data".getBytes();
  private final byte[] imageData2 = ImageUtil.compressImage("image data".getBytes());

  @Test
  void compressImageTest() {
    byte[] compressedImageData = ImageUtil.compressImage(imageData);
    Assertions.assertArrayEquals(imageData, ImageUtil.decompressImage(compressedImageData));
    Assertions.assertFalse(imageData.length > compressedImageData.length);
  }
  @Test
  void decompressImageTest() {
    byte[] decompressedImageData = ImageUtil.decompressImage(imageData2);
    Assertions.assertArrayEquals(imageData2, ImageUtil.compressImage(decompressedImageData));
    Assertions.assertTrue(imageData2.length > decompressedImageData.length);
  }
}
