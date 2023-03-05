package unsw.archaic_fs;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import unsw.archaic_fs.exceptions.UNSWFileAlreadyExistsException;
import unsw.archaic_fs.exceptions.UNSWFileNotFoundException;
import unsw.archaic_fs.exceptions.UNSWNoSuchFileException;
import java.util.EnumSet;

public class ArchaicFsTest {
  @Test
  public void testCdInvalidDirectory() {
    ArchaicFileSystem fs = new ArchaicFileSystem();

    // Try to change directory to an invalid one
    assertThrows(UNSWNoSuchFileException.class, () -> {
      fs.cd("/usr/bin/cool-stuff");
    });
  }

  @Test
  public void testCdValidDirectory() {
    ArchaicFileSystem fs = new ArchaicFileSystem();

    assertDoesNotThrow(() -> {
      fs.mkdir("/usr/bin/cool-stuff", true, false);
      fs.cd("/usr/bin/cool-stuff");
    });
  }

  @Test
  public void testCdAroundPaths() {
    ArchaicFileSystem fs = new ArchaicFileSystem();

    assertDoesNotThrow(() -> {
      fs.mkdir("/usr/bin/cool-stuff", true, false);
      fs.cd("/usr/bin/cool-stuff");
      assertEquals("/usr/bin/cool-stuff", fs.cwd());
      fs.cd("..");
      assertEquals("/usr/bin", fs.cwd());
      fs.cd("../bin/..");
      assertEquals("/usr", fs.cwd());
    });
  }

  @Test
  public void testCreateFile() {
    ArchaicFileSystem fs = new ArchaicFileSystem();

    assertDoesNotThrow(() -> {
      fs.writeToFile("test.txt", "My Content", EnumSet.of(FileWriteOptions.CREATE, FileWriteOptions.TRUNCATE));
      assertEquals("My Content", fs.readFromFile("test.txt"));
      fs.writeToFile("test.txt", "New Content", EnumSet.of(FileWriteOptions.TRUNCATE));
      assertEquals("New Content", fs.readFromFile("test.txt"));
    });
  }

  // Now write some more!
  // Some ideas to spark inspiration;
  // - File Writing/Reading with various options (appending for example)
  // - Cd'ing .. on the root most directory (shouldn't error should just remain on
  // root directory)
  // - many others...

@Test
public void testCdAroundPaths2() {
  ArchaicFileSystem fileSystem = new ArchaicFileSystem();

  try {
    fileSystem.mkdir("/usr/a/b/c/d", true, false);
    fileSystem.cd("/usr/a/b/c/d");
    assertEquals("/usr/a/b/c/d", fileSystem.cwd());

    fileSystem.cd("..");
    assertEquals("/usr/a/b/c", fileSystem.cwd());

    fileSystem.cd("../../..");
    assertEquals("/usr", fileSystem.cwd());

    fileSystem.cd("../../../../..");
    assertEquals("", fileSystem.cwd());
  } catch (Exception e) {
    fail("Unexpected exception: " + e.getMessage());
  }
}


  @Test
  public void testMkdir() {
    ArchaicFileSystem fs = new ArchaicFileSystem();
    assertThrows(UNSWFileNotFoundException.class, () -> {
      fs.mkdir("/usr/bin/car", false, false);
    });
    assertThrows(UNSWFileAlreadyExistsException.class, () -> {
      fs.mkdir("/usr/bin/car", true, false);
      fs.mkdir("/usr/bin/car", true, false);
    });
  }

  @Test
  public void testWriteToFileFileWrongArgs() {
    ArchaicFileSystem fs = new ArchaicFileSystem();
    assertThrows(IllegalArgumentException.class, () -> {
      fs.writeToFile("test.txt", "Content", EnumSet.of(FileWriteOptions.CREATE));
    });
    assertThrows(IllegalArgumentException.class, () -> {
      fs.writeToFile("test.txt", "Content", EnumSet.of(FileWriteOptions.TRUNCATE, FileWriteOptions.APPEND));
    });
  }

  @Test
  public void testWriteToFileFileWithNewFile() {
    ArchaicFileSystem fs = new ArchaicFileSystem();
    assertDoesNotThrow(() -> {
      fs.writeToFile("test.txt", "Content", EnumSet.of(FileWriteOptions.CREATE_IF_NOT_EXISTS, FileWriteOptions.APPEND));
    });
  }

  @Test
  public void testWriteToFileFileNotExists() {
    ArchaicFileSystem fs = new ArchaicFileSystem();
    assertThrows(UNSWFileNotFoundException.class, () -> {
      fs.writeToFile("test.txt", "Content", EnumSet.of(FileWriteOptions.APPEND));
    });
  }

  @Test
  public void testWriteToFileFileExists() {
    ArchaicFileSystem fs = new ArchaicFileSystem();
    assertThrows(UNSWFileAlreadyExistsException.class, () -> {
      fs.writeToFile("test.txt", "Content", EnumSet.of(FileWriteOptions.CREATE, FileWriteOptions.TRUNCATE));
      fs.writeToFile("test.txt", "Content", EnumSet.of(FileWriteOptions.CREATE, FileWriteOptions.TRUNCATE));
    });
  }

  @Test
  public void testWriteToFileFileAppend() {
    ArchaicFileSystem fs = new ArchaicFileSystem();
    assertDoesNotThrow(() -> {
      fs.writeToFile("test.txt", "Content", EnumSet.of(FileWriteOptions.CREATE, FileWriteOptions.TRUNCATE));
      fs.writeToFile("test.txt", "Content", EnumSet.of(FileWriteOptions.APPEND));
    });
  }

  @Test
  public void testReadFromFileNotExists() {
    ArchaicFileSystem fs = new ArchaicFileSystem();
    assertThrows(UNSWFileNotFoundException.class, () -> {
      fs.readFromFile("/user/bin/abc");
    });

  }
}
