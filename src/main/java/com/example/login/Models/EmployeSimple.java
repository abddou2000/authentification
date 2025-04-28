package com.example.login.Models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.sql.Date;

@Getter
@Setter
@Entity
public class EmployeSimple {

    @Id
    private String idEmploye;

    private String nom;
    private String prenom;
    private String cin;
    private String emailPro;
    private String emailPerso;
    private String adresse;
    private Long telephone;
    private Date dateNaissance;
    private String lieuNaissance;
    private String genre;
    private String nationalite;
    private String situationFamiliale;
    private Integer enfantsACharge;
    private String departement;
    private String service;
    private String typeProfil;
    private Short permisConduire;
    private String mobiliteGeographique;
    private String languesParlees;
    private String contactUrgence;
    private String etatCompte;
    private String motDePasse;
    private Date dateCreation;
    private Date dateModification;
    private String nomUtilisateur;

    @ManyToOne
    @JoinColumn(name = "id_role", referencedColumnName = "idRole")  // Assurez-vous que 'idRole' correspond à la propriété de l'entité Role
    private Role role;

    @OneToOne(mappedBy = "employeSimple")
    private Rh rh;

    @OneToOne(mappedBy = "employeSimple")
    private Configurateur configurateur;

    @OneToOne(mappedBy = "employeSimple")
    private Administrateur administrateur;


}

