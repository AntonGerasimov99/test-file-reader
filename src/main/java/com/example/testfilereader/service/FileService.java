package com.example.testfilereader.service;

import com.example.testfilereader.model.Node;
import org.springframework.web.multipart.MultipartFile;

public interface FileService {

    Node getNodesFromFile(MultipartFile file);

    Long getIdFromFile(MultipartFile file);

    Node getNodeById(Long id);
}