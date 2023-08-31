package com.skyros.app.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
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
@Table(name = "file")
public class File {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "file_seq")
    @SequenceGenerator(name = "file_seq", sequenceName = "file_seq", allocationSize = 1)
    @Column(name = "id", unique = true, nullable = false)
    private Long id;
    @Column(name = "name")
    private String name;
    @Lob
    @Column(name = "file_binary")
    private String fileBinary;
    @JsonBackReference
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "item_id")
    private ItemFile item;

}
