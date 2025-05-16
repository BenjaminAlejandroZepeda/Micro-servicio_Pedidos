package com.Vineyard.microservicio.model;

import java.time.LocalDate;
import java.util.List;
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
@   Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Integer id;

@Column(name = "cliente_id")
private Long clienteId;


@ElementCollection
@CollectionTable(name = "PEDIDO_PRODUCTOS", joinColumns = @JoinColumn(name = "PEDIDO_ID"))
@Column(name = "producto_id")
private List<Long> productoIds;


@Column(name = "fecha")
private LocalDate fecha;


@Column(name = "total")
private double total;
}
