package com.example.testfilereader.controller;


import com.example.testfilereader.model.Node;
import com.example.testfilereader.service.FileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@Slf4j
@RequestMapping("/file")
public class FileController {

    private final FileService service;

    public FileController(FileService service) {
        this.service = service;
    }

    @PostMapping
    public Node get(@RequestParam("file") MultipartFile file) {
        log.info("Received request to get Node file");
        return service.getNodesFromFile(file);
    }
}
