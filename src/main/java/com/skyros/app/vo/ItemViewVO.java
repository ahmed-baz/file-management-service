package com.skyros.app.vo;

import com.skyros.app.enums.FileTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ItemViewVO {

    private Long id;
    private String name;
    private FileTypeEnum type;

}
