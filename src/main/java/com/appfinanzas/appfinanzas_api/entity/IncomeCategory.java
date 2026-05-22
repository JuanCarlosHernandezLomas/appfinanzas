package com.appfinanzas.appfinanzas_api.entity;

import com.appfinanzas.appfinanzas_api.enums.MovementType;
import jakarta.persistence.*;

@Entity
@Table(name = "income_categories")
public class IncomeCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /*
     * Nombre de la categoría.
     * Ejemplo: Salario, Pensión, Freelance.
     */
    @Column(name = "name", nullable = false, length = 100)
    private String name;

    /*
     * FIXED = ingreso fijo
     * VARIABLE = ingreso variable
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false, length = 30)
    private MovementType type;

    @Column(name = "active", nullable = false)
    private Boolean active = true;

    public IncomeCategory() {
    }

    @PrePersist
    public void prePersist() {
        if (this.active == null) {
            this.active = true;
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public MovementType getType() {
        return type;
    }

    public void setType(MovementType type) {
        this.type = type;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }
}