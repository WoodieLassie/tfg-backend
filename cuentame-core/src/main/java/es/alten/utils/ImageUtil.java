package es.alten.utils;

import es.alten.exceptions.BadInputException;

import java.io.ByteArrayOutputStream;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

public class ImageUtil {
  public static byte[] compressImage(byte[] data) {

    Deflater deflater = new Deflater();
    deflater.setLevel(Deflater.BEST_COMPRESSION);
    deflater.setInput(data);
    deflater.finish();

    ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
    byte[] tmp = new byte[4 * 1024];
    while (!deflater.finished()) {
      int size = deflater.deflate(tmp);
      outputStream.write(tmp, 0, size);
    }
    try {
      outputStream.close();
    } catch (Exception e) {
      throw new BadInputException();
    }
    return outputStream.toByteArray();
  }

  public static byte[] decompressImage(byte[] data) {
    if (data == null) {
      return new byte[0];
    }
    Inflater inflater = new Inflater();
    inflater.setInput(data);
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
    byte[] tmp = new byte[4 * 1024];
    try {
      while (!inflater.finished()) {
        int count = inflater.inflate(tmp);
        outputStream.write(tmp, 0, count);
      }
      outputStream.close();
    } catch (Exception exception) {
      throw new BadInputException();
    }
    return outputStream.toByteArray();
  }

  private ImageUtil() {
    throw new IllegalStateException("Utility class");
  }
}
