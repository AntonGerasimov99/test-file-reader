package com.example.testfilereader.service;

import com.example.testfilereader.model.Node;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@Slf4j
public class FileServiceImpl implements FileService {

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
        return null;
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
        Node mainNode = new Node(null, null, null);
        try (BufferedReader reader = new BufferedReader(new FileReader(file))){
            List<String> fileLines = reader.lines().toList();
            List<String> newList = new ArrayList<>();
            newList.add(fileLines.get(0));
            mainNode.setTextInChapter(newList);
            mainNode.setLevel(0);
            for (String line : fileLines.subList(1, fileLines.size())) {
                Integer level = getLevel(line);
                saveNodeInMain(mainNode, line, level);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        log.info("File convert to Node successful");
        return mainNode;
    }

    /*
    Новый метод для нода
     */
    private Node convertFileToNode2(File file) {
        Node mainNode = new Node(null, null, null, null);
        try (BufferedReader reader = new BufferedReader(new FileReader(file))){
            List<String> fileLines = reader.lines().toList();
            String nameChapterForMainNode = fileLines.get(0);
            mainNode.setNameOfChapter(nameChapterForMainNode);
            mainNode.setLevel(0);
            for (String line : fileLines.subList(1, fileLines.size())) {
                Integer level = getLevel(line);
                saveNodeInMain(mainNode, line, level);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        log.info("File convert to Node successful");
        return mainNode;
    }

    private void saveNodeInMain2(Node currentNode, String line, Integer level) {
        if (!Objects.equals(currentNode.getLevel(), level)) {
            if (currentNode.getSubChapters()!=null) {
                saveNodeInMain(currentNode.getSubChapters(), line, level);
            } else {
                List<String> list = new ArrayList<>();
                list.add(line);
                Node newNode = new Node(currentNode, list, null);
                newNode.setLevel(level);
            }
        }
        if (currentNode.getTextInChapter() == null) {
            List<String> list = new ArrayList<>();
            list.add(line);
            currentNode.setTextInChapter(list);
        } else {
            currentNode.getTextInChapter().add(line);
        }
    }

    /*
    Метод который сортирует ноды по разделам и сохраняет в список
    Если уровень раздела не совпадает, используем рекурсию
    Если в найденном разделе отсутствует список стрингов, создаем (невозможная ситуация)
    Иначе добавляем новую строчку в список
     */
    private void saveNodeInMain(Node mainNode, String line, Integer level) {
        if (!Objects.equals(mainNode.getLevel(), level)) {
            if (mainNode.getSubChapters()!=null) {
                saveNodeInMain(mainNode.getSubChapters(), line, level);
            } else {
                List<String> list = new ArrayList<>();
                list.add(line);
                Node newNode = new Node(mainNode, list, null);
                newNode.setLevel(level);
            }
        }
        if (mainNode.getTextInChapter() == null) {
            List<String> list = new ArrayList<>();
            list.add(line);
            mainNode.setTextInChapter(list);
        } else {
            mainNode.getTextInChapter().add(line);
        }
    }

    /*
    Метод считывает кол-во # в начале строчки и возвращает их кол-во
     */
    private Integer getLevel(String line) {
        Integer level = 0;
        for (Character chr : line.toCharArray()) {
            if (chr.equals("#")) {
                level++;
            } else {
                return level;
            }
        }
        return level;
    }
}
