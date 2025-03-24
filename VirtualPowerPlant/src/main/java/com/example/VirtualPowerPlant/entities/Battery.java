package com.example.VirtualPowerPlant.entities;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;


@Entity
@Table(name = "battery")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Battery {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty(message = "Name is a required field")
    @Column(unique = true)
    private String name;

    @NotEmpty(message = "Postcode is a required field")
    private String postcode;

    @NotNull(message = "Capacity must not be null")
    @Positive(message = "Capacity must be a positive number")
    private Long capacity; // Capacity should not be negative or zero


}
