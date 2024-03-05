package com.example.testfilereader;

import com.example.testfilereader.model.Node;
import com.example.testfilereader.service.FileServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileServiceTest {

    private FileServiceImpl fileService = new FileServiceImpl();

    @Test
    void getFileAndConvertToNodeTest() {
        Path path = Paths.get("example.txt");
        String name = "example.txt";
        String originalFileName = "example.txt";
        String contentType = "text/plain";
        byte[] content = null;
        try {
            content = Files.readAllBytes(path);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        MultipartFile result = new MockMultipartFile(name, originalFileName, contentType, content);
        Node nodeForCheck = fileService.getNodesFromFile(result);
        System.out.println(nodeForCheck.getNameOfChapter());
        System.out.println(nodeForCheck.getSubChapters().size());
        System.out.println(nodeForCheck.getSubChapters().get(0).getNameOfChapter());
        System.out.println(nodeForCheck.getSubChapters().get(0).getTextInChapter());
        System.out.println(nodeForCheck.getSubChapters().get(0).getSubChapters().size());
        System.out.println(nodeForCheck.getSubChapters().get(0).getSubChapters().get(0).getNameOfChapter());
    }
}
