package com.reto.usuario.infrastructure.drivenadapter.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "usuarios")
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idUser;
    @Column(name = "nombre")
    private String name;
    @Column(name = "apellido")
    private String lastName;
    @Column(name = "documento_identidad")
    private Long identificationDocument;
    @Column(name = "celular")
    private String cellPhone;
    @Column(name = "correo")
    private String email;
    @Column(name = "clave")
    private String password;
    @ManyToOne
    @JoinColumn(name = "id_rol", referencedColumnName = "idRol")
    private RolEntity rol;
}

