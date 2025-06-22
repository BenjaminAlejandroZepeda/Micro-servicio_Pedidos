package com.Vineyard.microservicio.model;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonBackReference;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Table(name = "pedido_productos")
@Data
@AllArgsConstructor
@NoArgsConstructor

// Implementa Serializable, lo cual es requisito de JPA para claves compuestas.
@Schema(description = "Entidad que representa la relación entre pedido y producto")
public class PedidoProducto implements Serializable {

    //Clave primaria compuesta que identifica de forma única la relación entre un pedido y un producto
    /*
     * Esta clave está embebida y compuesta por:
     * pedidoId: el ID del pedido al que pertenece este producto.
     * productoId: el ID del producto incluido en el pedido.
     * 
     * Esto es útil en relaciones muchos-a-muchos con atributos adicionales
     * (como la cantidad), donde se necesita una tabla intermedia (pedido_productos).
     * 
     * PedidoProductoId contiene los campos que forman la clave primaria.
     * 
     * Porque en una relación muchos-a-muchos con atributos adicionales (como cantidad),
     * no puedes usar directamente @ManyToMany.
     */
    @EmbeddedId
    @Schema(description = "Identificador compuesto del pedido-producto")
        private PedidoProductoId id;
    
    /*
     * @MapsId("pedidoId"): enlaza este campo con el campo `pedidoId` de la clave compuesta `PedidoProductoId`.
     * @JsonBackReference: evita la serialización recursiva infinita al convertir a JSON, ya que el lado "padre"
     */
    @ManyToOne
    @MapsId("pedidoId")
    @JoinColumn(name = "pedido_id")
    @JsonBackReference
    @Schema(description = "Pedido")
        private Pedido pedido;

    @Schema(description = "Cantidad del pedido")
    @Column(name = "cantidad")
        private Integer cantidad;

}
