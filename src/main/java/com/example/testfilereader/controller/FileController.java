package com.example.testfilereader.controller;


import com.example.testfilereader.model.Node;
import com.example.testfilereader.service.FileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/file")
public class FileController {

    private final FileService service;

    @PostMapping
    public Node get(@RequestParam("file") MultipartFile file) {
        log.info("Received request to get Node file");
        return service.getNodesFromFile(file);
    }

    @PostMapping("/save")
    public Long save(@RequestParam("file") MultipartFile file) {
        log.info("Received request to save file");
        return service.getIdFromFile(file);
    }

    @GetMapping("/{id}")
    public Node get(@PathVariable Long id) {
        log.info("Received request to get Node");
        return service.getNodeById(id);
    }
}