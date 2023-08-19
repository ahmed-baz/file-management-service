package com.skyros.app.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.skyros.app.enums.FileTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "item")
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "item_seq")
    @SequenceGenerator(name = "item_seq", sequenceName = "item_seq", allocationSize = 1)
    @Column(name = "id", unique = true, nullable = false)
    private Long id;
    @Column(name = "name")
    private String name;
    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    private FileTypeEnum type;
    @JsonBackReference
    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "permission_group_id")
    private PermissionGroup permissionGroup;
    @JsonBackReference
    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "parent_id")
    private Item parent;

}
