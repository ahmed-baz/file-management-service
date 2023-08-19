package com.skyros.app.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.skyros.app.enums.PermissionLevelEnum;
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
@Table(name = "permission")
public class Permission {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "permission_seq")
    @SequenceGenerator(name = "permission_seq", sequenceName = "permission_seq", allocationSize = 1)
    private Long id;
    @Column(name = "user_email")
    private String userEmail;
    @Column(name = "level")
    @Enumerated(EnumType.STRING)
    private PermissionLevelEnum level;
    @JsonBackReference
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "permission_group_id")
    private PermissionGroup permissionGroup;

}
