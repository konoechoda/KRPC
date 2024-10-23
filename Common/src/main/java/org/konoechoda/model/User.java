package org.konoechoda.model;

import lombok.Data;

import java.io.Serializable;

@Data
public class User implements Serializable {
    // 名称
    private String name;
}