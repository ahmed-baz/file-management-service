package com.skyros.app.vo;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class FileVO {

    private Long id;
    private String name;
    private String fileBinary;
    private ItemVO item;

}
