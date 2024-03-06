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

    /*
    Обращение на конвертацию файла в Ноду без сохранения в БД
     */
    @PostMapping
    public Node get(@RequestParam("file") MultipartFile file) {
        log.info("Received request to get Node file");
        return service.getNodesFromFile(file);
    }

    /*
    Сохранение файла в БД и возвращение Id
     */
    @PostMapping("/save")
    public Long save(@RequestParam("file") MultipartFile file) {
        log.info("Received request to save file");
        return service.getIdFromFile(file);
    }

    /*
    Получение файла из БД по Id и конвертация в ноду
     */
    @GetMapping("/{id}")
    public Node get(@PathVariable Long id) {
        log.info("Received request to get Node");
        return service.getNodeById(id);
    }
}