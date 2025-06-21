package com.Vineyard.microservicio.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "pedido")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Entidad que representa un pedido")
public class Pedido {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Identificador de pedido")
        private Long id;

    @Schema(description = "Identificador de cliente")
    @Column(name = "cliente_id", nullable = false)
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
    @Schema(description = "Lista de productos")
        private List<PedidoProducto> productos = new ArrayList<>();
    @Schema(description = "fecha del pedido")
    @Column(name = "fecha", nullable = false)
        private LocalDate fecha;
    @Schema(description = "Total del pedido")
    @Column(name = "total")
        private double total;
}
