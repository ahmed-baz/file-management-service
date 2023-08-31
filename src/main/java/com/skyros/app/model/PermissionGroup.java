package com.skyros.app.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "permission_group")
public class PermissionGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "permission_group_seq")
    @SequenceGenerator(name = "permission_group_seq", sequenceName = "permission_group_seq", allocationSize = 1)
    private Long id;
    @Column(name = "group_name")
    private String groupName;
    @JsonManagedReference
    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    @JoinColumn(name = "permission_group_id")
    private List<Permission> permissions;

    public PermissionGroup(Long id) {
        this.id = id;
    }
}
