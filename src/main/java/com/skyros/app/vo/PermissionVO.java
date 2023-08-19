package com.skyros.app.vo;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.skyros.app.enums.PermissionLevelEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PermissionVO {

    private Long id;
    private String userEmail;
    private PermissionLevelEnum level;
    private PermissionGroupVO permissionGroup;

}
