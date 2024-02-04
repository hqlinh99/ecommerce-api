package com.hqlinh.sachapi.file;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.awt.*;
import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "file_uploads")
public class FileUpload {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(name = "name")
    private String name;
    @Column(name = "extension")
    private String extension;
    @Column(name = "content_type")
    private String contentType;
    @Column(name = "size")
    private long size;
    @Column(name = "url")
    private String url;
    @Column(name = "dimension")
    private Dimension dimension;
}
