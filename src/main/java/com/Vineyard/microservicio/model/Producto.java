package com.Vineyard.microservicio.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Producto {


    private Long id;

    private String nombre;

    private double precio;

    private int cantidad;
}
