package com.Vineyard.microservicio.model;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Table(name = "pedido_productos")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PedidoProducto implements Serializable {

    @EmbeddedId
    private PedidoProductoId id;
    
    @ManyToOne
    @MapsId("pedidoId")
    @JoinColumn(name = "pedido_id")
    @JsonBackReference
    private Pedido pedido;

    @Column(name = "cantidad")
    private Integer cantidad;


}
