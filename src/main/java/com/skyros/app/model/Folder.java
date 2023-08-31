package com.skyros.app.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.skyros.app.enums.FileTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "item")
public class Folder extends BaseItem {

    @JsonBackReference
    @ManyToOne(cascade = CascadeType.MERGE)
    private PermissionGroup permissionGroup;
    @JsonBackReference
    @ManyToOne(cascade = CascadeType.MERGE)
    private Item parent;

}
