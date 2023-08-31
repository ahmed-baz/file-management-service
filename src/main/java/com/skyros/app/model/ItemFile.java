package com.skyros.app.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "item")
public class ItemFile extends BaseItem {

    @JsonBackReference
    @ManyToOne(cascade = CascadeType.MERGE)
    private PermissionGroup permissionGroup;
    @JsonBackReference
    @ManyToOne(cascade = CascadeType.MERGE)
    private Item parent;

}
