package com.example.demo.common.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.BatchSize;

import java.util.HashSet;
import java.util.Set;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString(exclude = {"attachFiles"})
public class FileUploadEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long uno;

    private String name;


    @ElementCollection
    @Builder.Default
    @BatchSize(size = 2)
    private Set<AttachFile> attachFiles = new HashSet<>();


    public void addFile(String filename) {
        if (attachFiles.size() >= 2) {
            throw new IllegalStateException("파일은 최대 2개까지 업로드할 수 있습니다.");
        }
        attachFiles.add(new AttachFile(attachFiles.size(), filename));
    }

    public void clearFiles() {
        attachFiles.clear();
    }
}
