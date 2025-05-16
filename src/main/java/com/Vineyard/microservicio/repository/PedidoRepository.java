package com.Vineyard.microservicio.repository;
import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.Vineyard.microservicio.model.Pedido;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Long>{


// Buscar pedidos por cliente
List<Pedido> findByClienteId(Long clienteId);


// Buscar pedidos por fecha exacta
List<Pedido> findByFecha(LocalDate fecha);


// Buscar pedidos entre fechas
List<Pedido> findByFechaBetween(LocalDate desde, LocalDate hasta);


// Contar pedidos por cliente
long countByClienteId(Long clienteId);


// Obtener los pedidos más recientes (ordenados por fecha descendente)
List<Pedido> findTop10ByOrderByFechaDesc();

// Eliminar pedido por id
void deleteById(Long id);


}
