package test.jpa.more.business;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.iglooproject.jpa.more.business.file.model.path.HashTableFileStorePathGeneratorImpl;
import org.junit.jupiter.api.Test;

class TestHashTableFilePathGenerator {

  @Test
  void testHashTableFilePathGenerator() {
    HashTableFileStorePathGeneratorImpl pathGenerator1 = new HashTableFileStorePathGeneratorImpl(1);

    String path1 = pathGenerator1.getFilePath("143205", "pdf");
    assertEquals("cd/143205.pdf", path1);

    String path2 = pathGenerator1.getFilePath("117519", "docx");
    assertEquals("36/117519.docx", path2);
  }
}
