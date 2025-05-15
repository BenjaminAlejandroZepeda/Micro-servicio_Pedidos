package com.Vineyard.microservicio.model;

import java.util.ArrayList;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "pedido")
@Data
@AllArgsConstructor
@NoArgsConstructor

public class Pedido {
@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;


@ManyToOne
@JoinColumn(name = "cliente_id", nullable = false)
private Cliente cliente;


@Column(nullable =  true)   
private ArrayList<Producto> productos;

}
