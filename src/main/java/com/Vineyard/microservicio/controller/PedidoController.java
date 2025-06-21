package com.Vineyard.microservicio.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import com.Vineyard.microservicio.model.Pedido;
import com.Vineyard.microservicio.service.PedidoService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/v1/pedidos")
@Tag(name = "Pedidos", description = "Operaciones relacionadas con Pedidos")
public class PedidoController {

    @Autowired
    private PedidoService pedidoService;

//Swagger
// http://localhost:8080/doc/swagger-ui/index.html


// Obtener todos los pedidos
// http://localhost:8080/api/v1/pedidos
    @GetMapping
    @Operation(summary = "Obtener todos los pedidos", description = "Obtiene una lista de una lista de pedidos")
    @ApiResponses(value = 
        { 
            @ApiResponse(responseCode = "200", description = "Operación exitosa"), 
            @ApiResponse(responseCode = "404", description = "Pedidos no encontrados") 
        }
    ) 
        public ResponseEntity<List<Pedido>> listarPedidos() {
            return ResponseEntity.ok(pedidoService.listarPedidos());
    }

// Obtener pedidos por cliente
// http://localhost:8080/api/v1/pedidos/cliente/1
    @GetMapping("/cliente/{clienteId}")
    @Operation(summary = "Obtener pedidos por cliente", description = "Obtener los pedidos de un cliente en especifico por id cliente")
    @Parameter(description = "Cliente Id", required = true)
        public ResponseEntity<List<Pedido>> obtenerPedidosPorCliente(@PathVariable Long clienteId) {
            if (clienteId == null || clienteId <= 0) {
                return ResponseEntity.badRequest().build();
            }
            return ResponseEntity.ok(pedidoService.obtenerPedidosPorCliente(clienteId));
        }

// Obtener pedidos por fecha exacta
// http://localhost:8080/api/v1/pedidos/fecha
// Ve a la pestaña Params y agrega Key: fecha Value: ####
    @GetMapping("/fecha")
    @Operation(summary = "Obtener pedidos por fecha exacta", description = "Obtener pedidos por fecha exacta YYYY/MM/DD")
    @Parameter(description = "Fecha", required = true)
        public ResponseEntity<List<Pedido>> obtenerPedidosPorFecha(@RequestParam("fecha") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha) {
            return ResponseEntity.ok(pedidoService.obtenerPedidosPorFecha(fecha));
    }
// Obtener pedidos entre fechas
// http://localhost:8080/api/v1/pedidos/rango-fechas
// Key: desde → Value: ####
// Key: hasta → Value: ####
    @GetMapping("/rango-fechas")
    @Operation(summary = "Obtener pedidos entre fechas exactas", description = "Obtener pedidos entre las fechas dadas")
    @Parameter(description = "Fecha desde", required = true)
    @Parameter(description = "Fecha hasta", required = true)
        public ResponseEntity<List<Pedido>> obtenerPedidosEntreFechas(
            @RequestParam("desde") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate desde,
            @RequestParam("hasta") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate hasta) {
                return ResponseEntity.ok(pedidoService.obtenerPedidosEntreFechas(desde, hasta));
    }
// Contar pedidos por cliente
// http://localhost:8080/api/v1/pedidos/cliente/1/cantidad
    @GetMapping("/cliente/{clienteId}/cantidad")
    @Operation(summary = "Contar pedidos por cliente", description = "Cuenta la lista de pedidos en total del cliente")
    @Parameter(description = "ClienteId", required = true )
        public ResponseEntity<Long> contarPedidosPorCliente(@PathVariable Long clienteId) {
            return ResponseEntity.ok(pedidoService.contarPedidosPorCliente(clienteId));
    }
// Obtener los últimos 10 pedidos
// http://localhost:8080/api/v1/pedidos/ultimos
    @GetMapping("/ultimos")
    @Operation(summary = "Ultimos 10 pedidos", description = "Lee los ultimos 10 pedidos y lo retorna")
        public ResponseEntity<List<Pedido>> obtenerUltimosPedidos() {
            return ResponseEntity.ok(pedidoService.obtenerUltimosPedidos());
    }

/*
 * {
  "clienteId": 2,
  "fecha": "2025-05-24",
  "total": 0,
  "productos": [
    {
      "id": {
        "productoId": 8
      },
      "cantidad": 2
    }
  ]
}
 */


    @PostMapping
    @Operation(summary = "Hacer un pedido", description = "Recibe el json y registra el pedido en la base de datos")
    @ApiResponses(value = {@ApiResponse
        (responseCode = "200", description = "Pedido creado",  
        content = @Content(mediaType = "application/json", 
        schema = @Schema(implementation = Pedido.class))), 
        @ApiResponse
        (responseCode = "404", description = "No se a creado el pedido",  
        content = @Content(mediaType = "application/json", 
        schema = @Schema(implementation = Pedido.class)))}) 

    public ResponseEntity<?> crearPedido(@RequestBody Pedido pedido, BindingResult result) {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body("Datos del pedido inválidos.");
        }

        Pedido nuevoPedido = pedidoService.guardarOActualizarPedido(pedido);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoPedido);
    }



    @PutMapping("/{id}")
    @Operation(summary = "Modificar un pedido", description = "")
    @Parameter(description = "PedidoId", required = true )
    @ApiResponses(value = {@ApiResponse
        (responseCode = "200", description = "Pedido modificado",  
        content = @Content(mediaType = "application/json", 
        schema = @Schema(implementation = Pedido.class))), 
        @ApiResponse
        (responseCode = "404", description = "No se a podido modificar el pedido",  
        content = @Content(mediaType = "application/json", 
        schema = @Schema(implementation = Pedido.class)))})
        
    public ResponseEntity<?> actualizarPedido(@PathVariable Long id, @RequestBody Pedido pedido, BindingResult result) {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body("Datos del pedido inválidos.");
        }

        pedido.setId(id); // Asegura que el ID del path se use en el objeto
        Pedido actualizado = pedidoService.guardarOActualizarPedido(pedido);
        return ResponseEntity.ok(actualizado);
    }


    @DeleteMapping("/{id}")
    @Operation(summary = "Elimina un pedido", description = "Recibe un id y elimina el pedido con ese identificador")
    @ApiResponses(value = {@ApiResponse
        (responseCode = "200", description = "Pedido modificado",  
        content = @Content(mediaType = "application/json", 
        schema = @Schema(implementation = Pedido.class))), 
        @ApiResponse
        (responseCode = "404", description = "No se a podido modificar el pedido",  
        content = @Content(mediaType = "application/json", 
        schema = @Schema(implementation = Pedido.class)))})

        public ResponseEntity<Void> eliminarPedido(@PathVariable Long id) {
            pedidoService.eliminarPedido(id);
                return ResponseEntity.noContent().build();
    }

}

