package com.Vineyard.microservicio.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;

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
        private Long id;

    @Column(name = "cliente_id")
        private Long clienteId;

    /* Relación uno-a-muchos con PedidoProducto
     * un pedido puede tener muchos productos asociados.
     * mappedBy = "pedido": indica que la relación está definida en el campo pedido de la clase PedidoProducto.
     * cascade = CascadeType.ALL: las operaciones como guardar, actualizar o eliminar se propagan a los productos.
     * orphanRemoval = true: si se elimina un producto de la lista, también se elimina de la base de datos.
     */
    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL, orphanRemoval = true)
    /*
    * @JsonManagedReference: evita la recursión infinita al serializar a JSON 
    * (relación bidireccional con @JsonBackReference en PedidoProducto).
    */
    @JsonManagedReference
        private List<PedidoProducto> productos = new ArrayList<>();

    @Column(name = "fecha")
        private LocalDate fecha;

    @Column(name = "total")
        private double total;
}
