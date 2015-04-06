package com.jimjh.kryo;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Writes and reads a POJO from disk.
 *
 * @author Jim Lim - jim@quixey.com
 */
public class SimpleIO {

  private static final Logger LOGGER = LoggerFactory.getLogger(SimpleIO.class);

  /**
   * @param args command-line args, ignored for now
   */
  public static void main(String[] args) throws IOException {
    POJO object = POJO.create();
    Kryo kryo = new Kryo();

    Path scratch = Files.createTempDirectory("kryo");
    String file = scratch.resolve("file.io").toString();

    try (Output output = new Output(new FileOutputStream(file))) {
      kryo.writeObject(output, object);
      LOGGER.info("Wrote object to {}", file);
    }

    try (Input input = new Input(new FileInputStream(file))) {
      POJO read = kryo.readObject(input, POJO.class);
      LOGGER.info("Read object from {}", file);
    }
  }
}
