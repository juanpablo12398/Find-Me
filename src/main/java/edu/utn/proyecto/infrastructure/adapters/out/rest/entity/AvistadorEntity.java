package edu.utn.proyecto.infrastructure.adapters.out.rest.entity;
import edu.utn.proyecto.infrastructure.adapters.out.rest.entity.enums.Rol;
import jakarta.persistence.*;

@Entity
public class AvistadorEntity {
    // Esta clase va a contener los mismos atributos que la clase Avistador del dominio,
    // pero con anotaciones de JPA para mapearla a una tabla en la base de datos.

    // Registra como Id en mi tabla de PostgreSQL
    @Id
    // Este lo genera automáticamente la base de datos
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Este hace que mi dni sea unico y no nulo en la base de datos
    @Column(unique = true, nullable = false)
    private String dni;

    // Este hace que el enum se guarde en la base como un String
    // Y no como un número haciendo que si cambio el orden de los enums
    // no se rompa la base de datos
    @Enumerated(EnumType.STRING)
    private Rol rol;

}
