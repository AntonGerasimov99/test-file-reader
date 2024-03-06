package com.example.testfilereader.service;

import com.example.testfilereader.exception.NotFoundElementException;
import com.example.testfilereader.model.FileDb;
import com.example.testfilereader.model.Node;
import com.example.testfilereader.repository.FileDbRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@Slf4j
public class FileServiceImpl implements FileService {

    @Autowired
    private FileDbRepository fileDbRepository;

    /*
    Получение структурированной ноды
     */
    @Override
    public Node getNodesFromFile(MultipartFile file) {
        File convertFile = convertFile(file);
        Node mainNode = convertFileToNode(convertFile);
        log.info("getNodesFromFile in FileService successful");
        return mainNode;
    }

    /*
    Сохранение ноды в БД и возвращение Id
     */
    @Override
    public Long getIdFromFile(MultipartFile file) {
        String fileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
        FileDb fileDb = null;
        try {
            fileDb = new FileDb(fileName, file.getContentType(), file.getBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return fileDbRepository.save(fileDb).getId();
    }

    @Override
    public Node getNodeById(Long id) {
        FileDb fileDb = fileDbRepository.findById(id)
                .orElseThrow(() -> new NotFoundElementException("File with id " + id + " not found"));
        File file = convertFileDbInFile(fileDb);
        Node mainNode = convertFileToNode(file);
        log.info("getNodeById in FileService successful");
        return mainNode;

    }

    private File convertFileDbInFile(FileDb fileDb) {
        File convertFile = new File(Objects.requireNonNull(fileDb.getName()));
        try (FileOutputStream fileOutputStream = new FileOutputStream(convertFile)) {
            fileOutputStream.write(fileDb.getData());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return convertFile;
    }

    private File convertFile(MultipartFile file) {
        File convertFile = new File(Objects.requireNonNull(file.getOriginalFilename()));
        try (FileOutputStream fileOutputStream = new FileOutputStream(convertFile)) {
            fileOutputStream.write(file.getBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        log.info("MultipartFile convert to File successful");
        return convertFile;
    }

    /*
    Метод который конвертирует файл в ноды и сортирует по разделам
     */
    private Node convertFileToNode(File file) {
        Node mainNode = new Node(null, null, null, null);
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            List<String> fileLines = reader.lines().toList();
            String nameChapterForMainNode = fileLines.get(0);
            mainNode.setNameOfChapter(nameChapterForMainNode);
            mainNode.setLevel(0);
            Node currentNode = mainNode;
            for (String line : fileLines.subList(1, fileLines.size())) {
                Integer level = getLevel(line);
                currentNode = saveNodeInMain(currentNode, line, level);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        log.info("File convert to Node successful");
        return mainNode;
    }

    /*
    Метод который сортирует ноды по разделам и сохраняет в список
    Если уровень раздела не совпадает, используем рекурсию
    Если в найденном разделе отсутствует список стрингов, создаем (невозможная ситуация)
    Иначе добавляем новую строчку в список
     */
    private Node saveNodeInMain(Node currentNode, String line, Integer level) {
        if (level == 0) {
            if (currentNode.getTextInChapter() == null) {
                List<String> textInChapter = new ArrayList<>();
                textInChapter.add(line);
                currentNode.setTextInChapter(textInChapter);
            } else {
                currentNode.getTextInChapter().add(line);
            }
            return currentNode;
        }
        if (currentNode.getLevel() < level) {
            if (currentNode.getSubChapters() == null) {
                Node newNode = new Node(line, currentNode, null, null);
                newNode.setLevel(level);
                List<Node> subChapters = new ArrayList<>();
                subChapters.add(newNode);
                currentNode.setSubChapters(subChapters);
                return newNode;
            } else {
                Node newNode = new Node(line, currentNode, null, null);
                newNode.setLevel(level);
                currentNode.getSubChapters().add(newNode);
                return newNode;
            }
        }
        if (currentNode.getLevel() > level) {
            Node prevNode = currentNode.getPrev();
            while (!Objects.equals(prevNode.getLevel() + 1, level)) {
                prevNode = prevNode.getPrev();
            }
            if (prevNode.getSubChapters() == null) {
                Node newNode = new Node(line, prevNode, null, null);
                newNode.setLevel(level);
                List<Node> subChapters = new ArrayList<>();
                subChapters.add(newNode);
                prevNode.setSubChapters(subChapters);
                return newNode;
            } else {
                Node newNode = new Node(line, prevNode, null, null);
                newNode.setLevel(level);
                prevNode.getSubChapters().add(newNode);
                return newNode;
            }
        }
        return currentNode;
    }

    /*
    Метод считывает кол-во # в начале строчки и возвращает их кол-во
     */
    private Integer getLevel(String line) {
        Integer level = 0;
        for (Character chr : line.toCharArray()) {
            if (chr.equals('#')) {
                level++;
            } else {
                return level;
            }
        }
        return level;
    }
}