package com.example.testfilereader.repository;

import com.example.testfilereader.model.FileDb;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FileDbRepository extends JpaRepository<FileDb, Long> {
}