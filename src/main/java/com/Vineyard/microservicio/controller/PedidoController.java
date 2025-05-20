package com.Vineyard.microservicio.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.Vineyard.microservicio.model.Pedido;
import com.Vineyard.microservicio.service.PedidoService;

@RestController
@RequestMapping("/api/v1/pedidos")
public class PedidoController {


@Autowired
private PedidoService pedidoService;


// Obtener todos los pedidos
@GetMapping
public ResponseEntity<List<Pedido>> listarPedidos() {
        return ResponseEntity.ok(pedidoService.listarPedidos());
}


// Obtener pedidos por cliente
@GetMapping("/cliente/{clienteId}")
public ResponseEntity<List<Pedido>> obtenerPedidosPorCliente(@PathVariable Long clienteId) {
        return ResponseEntity.ok(pedidoService.obtenerPedidosPorCliente(clienteId));
}


// Obtener pedidos por fecha exacta
// http://localhost:8080/api/v1/pedidos/fecha
// Ve a la pestaña Params y agrega Key: fecha Value: ####
@GetMapping("/fecha")
public ResponseEntity<List<Pedido>> obtenerPedidosPorFecha(@RequestParam("fecha") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha) {
        return ResponseEntity.ok(pedidoService.obtenerPedidosPorFecha(fecha));
}

// Obtener pedidos entre fechas
// http://localhost:8080/api/v1/pedidos/rango-fechas
// Key: desde → Value: ####
// Key: hasta → Value: ####
@GetMapping("/rango-fechas")
public ResponseEntity<List<Pedido>> obtenerPedidosEntreFechas(
    @RequestParam("desde") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate desde,
    @RequestParam("hasta") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate hasta) {
        return ResponseEntity.ok(pedidoService.obtenerPedidosEntreFechas(desde, hasta));
}


// Contar pedidos por cliente
// http://localhost:8080/api/v1/pedidos/cliente/1/cantidad
@GetMapping("/cliente/{clienteId}/cantidad")
public ResponseEntity<Long> contarPedidosPorCliente(@PathVariable Long clienteId) {
    return ResponseEntity.ok(pedidoService.contarPedidosPorCliente(clienteId));
}


// Obtener los últimos 10 pedidos
// http://localhost:8080/api/v1/pedidos/ultimos
@GetMapping("/ultimos")
public ResponseEntity<List<Pedido>> obtenerUltimosPedidos() {
    return ResponseEntity.ok(pedidoService.obtenerUltimosPedidos());
}

// Post

{
//"clienteId": 1,
//"productoIds": [1, 2, 3],
//"fecha": "2025-05-16",
//"total": 499.99
}


@PostMapping
public ResponseEntity<Pedido> crearPedido(@RequestBody Pedido pedido) {
 Pedido nuevoPedido = pedidoService.guardarOActualizarPedido(pedido);
 return ResponseEntity.status(HttpStatus.CREATED).body(nuevoPedido);
}

@PutMapping("/{id}")
public ResponseEntity<Pedido> actualizarPedido(@PathVariable Long id, @RequestBody Pedido pedido) {
 Pedido actualizado = pedidoService.guardarOActualizarPedido(pedido);
 return ResponseEntity.ok(actualizado);
}

@DeleteMapping("/{id}")
public ResponseEntity<Void> eliminarPedido(@PathVariable Long id) {
 pedidoService.eliminarPedido(id);
    return ResponseEntity.noContent().build();
}

}

