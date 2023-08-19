package com.skyros.app.vo;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.skyros.app.enums.FileTypeEnum;
import com.skyros.app.model.Item;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ItemVO {

    private Long id;
    private String name;
    private FileTypeEnum type;
    private PermissionGroupVO permissionGroup;
    private ItemVO parent;

}
