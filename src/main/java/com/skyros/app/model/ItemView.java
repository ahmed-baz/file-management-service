package com.skyros.app.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.skyros.app.enums.FileTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Immutable;

import javax.persistence.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Immutable
@Table(name = "app_items")
public class ItemView {

    @Id
    private Long id;
    private String name;
    private String type;

}
