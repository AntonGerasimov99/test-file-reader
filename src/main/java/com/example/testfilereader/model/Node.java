package com.example.testfilereader.model;

import java.util.List;

public class Node {

    /*
    Название главы (#Имя)
     */
    private String nameOfChapter;

    /*
    Строчки такого же раздела как нода
    */
    private List<String> textInChapter;

    /*
    Нода следующего раздела, которая хранит информацию о следующих
     */
    private List<Node> subChapters;

    /*
    Нода предыдущего раздела, которая хранит информацию о вышестоящих разделах
     */
    private Node prev;

    /*
    Обозначение уровня раздела
    */
    private Integer level;

    public List<String> getTextInChapter() {
        return textInChapter;
    }

    public void setTextInChapter(List<String> textInChapter) {
        this.textInChapter = textInChapter;
    }

    public Node getPrev() {
        return prev;
    }

    public void setPrev(Node prev) {
        this.prev = prev;
    }

    public List<Node> getSubChapters() {
        return subChapters;
    }

    public void setSubChapters(List<Node> subChapters) {
        this.subChapters = subChapters;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public String getNameOfChapter() {
        return nameOfChapter;
    }

    public void setNameOfChapter(String nameOfChapter) {
        this.nameOfChapter = nameOfChapter;
    }

    public Node(String nameOfChapter, Node prev, List<String> result, List<Node> subChapters) {
        this.nameOfChapter = nameOfChapter;
        this.prev = prev;
        this.textInChapter = result;
        this.subChapters = subChapters;
    }
}