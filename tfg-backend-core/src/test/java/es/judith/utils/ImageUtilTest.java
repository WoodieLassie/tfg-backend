package es.judith.utils;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;

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
  @Test
  void isConstructorPrivateTest() throws NoSuchMethodException {
    Constructor<ImageUtil> constructor = ImageUtil.class.getDeclaredConstructor();
    constructor.setAccessible(true);
    Assertions.assertTrue(Modifier.isPrivate(constructor.getModifiers()));
    Assertions.assertThrows(InvocationTargetException.class, constructor::newInstance);
  }
}
