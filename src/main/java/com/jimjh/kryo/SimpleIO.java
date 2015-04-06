package com.jimjh.kryo;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.esotericsoftware.kryo.io.UnsafeInput;
import com.esotericsoftware.kryo.io.UnsafeOutput;
import com.google.caliper.AfterExperiment;
import com.google.caliper.BeforeExperiment;
import com.google.caliper.api.Macrobenchmark;
import com.google.caliper.runner.CaliperMain;
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

  public static void main(String[] args) {
    CaliperMain.main(SimpleIO.class, args);
  }

  private Path scratch;

  @BeforeExperiment
  void setUp() throws IOException {
    scratch = Files.createTempDirectory("kryo");
  }

  @AfterExperiment
  void tearDown() {
    scratch.toFile().deleteOnExit();
  }

  @Macrobenchmark
  void ioToFile(int reps) throws IOException {
    POJO object = POJO.create();
    Kryo kryo = new Kryo();

    for (int i=0; i<reps; i++) {
      String file = scratch.resolve("ioToFile.io." + i).toString();

      try (Output output = new Output(new FileOutputStream(file))) {
        kryo.writeObject(output, object);
      }

      try (Input input = new Input(new FileInputStream(file))) {
        POJO read = kryo.readObject(input, POJO.class);
        assert object.equals(read);
      }
    }
  }

  @Macrobenchmark
  void ioToArray(int reps) throws IOException {
    POJO object = POJO.create();
    Kryo kryo = new Kryo();

    byte[] buffer;

    for (int i=0; i<reps; i++) {
      try (Output output = new Output(1024 * 1024)) {
        kryo.writeObject(output, object);
        buffer = output.toBytes();
      }

      try (Input input = new Input(buffer)) {
        POJO read = kryo.readObject(input, POJO.class);
        assert object.equals(read);
      }
    }
  }

  @Macrobenchmark
  void unsafeIoToFile(int reps) throws IOException {
    POJO object = POJO.create();
    Kryo kryo = new Kryo();

    for (int i=0; i<reps; i++) {
      String file = scratch.resolve("unsafeIoToFile.io." + i).toString();

      try (Output output = new UnsafeOutput(new FileOutputStream(file))) {
        kryo.writeObject(output, object);
      }

      try (Input input = new UnsafeInput(new FileInputStream(file))) {
        POJO read = kryo.readObject(input, POJO.class);
        assert object.equals(read);
      }
    }
  }

  @Macrobenchmark
  void ioToFileWithComposition(int reps) throws IOException {
    POJO child = POJO.create();
    POJO object = new POJO("s1", "s2", 1, 2L, child);
    Kryo kryo = new Kryo();

    for (int i=0; i<reps; i++) {
      String file = scratch.resolve("ioToFileWithComposition.io." + i).toString();

      try (Output output = new UnsafeOutput(new FileOutputStream(file))) {
        kryo.writeObject(output, object);
      }

      try (Input input = new UnsafeInput(new FileInputStream(file))) {
        POJO read = kryo.readObject(input, POJO.class);
        assert object.equals(read);
      }
    }
  }
}
