package com.Vineyard.microservicio.controller;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import com.Vineyard.microservicio.assemblers.PedidoModelAssembler;
import com.Vineyard.microservicio.model.Pedido;
import com.Vineyard.microservicio.model.PedidoProducto;
import com.Vineyard.microservicio.service.PedidoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityNotFoundException;

@RestController
@RequestMapping("/api/v1/pedidos")
@Tag(name = "Pedidos", description = "Operaciones relacionadas con Pedidos")
public class PedidoController {

    @Autowired
    private PedidoService pedidoService;

    
    @Autowired
    private PedidoModelAssembler assembler;


//Swagger
// http://localhost:8080/doc/swagger-ui/index.html


// Obtener todos los pedidos
// http://localhost:8080/api/v1/pedidos
    @GetMapping(produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Obtener todos los pedidos", description = "Obtiene una lista de pedidos con enlaces HATEOAS")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Operación exitosa"),
        @ApiResponse(responseCode = "404", description = "Pedidos no encontrados")
    })
        public ResponseEntity<CollectionModel<EntityModel<Pedido>>> listarPedidos() {
        List<Pedido> pedidos = pedidoService.listarPedidos();
        if (pedidos.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        List<EntityModel<Pedido>> pedidosModel = pedidos.stream()
            .map(assembler::toModel)
            .toList();
        CollectionModel<EntityModel<Pedido>> collectionModel = CollectionModel.of(
            pedidosModel,
            linkTo(methodOn(PedidoController.class).listarPedidos()).withSelfRel()
        );
        return ResponseEntity.ok(collectionModel);
        }



// Obtener pedidos por cliente
// http://localhost:8080/api/v1/pedidos/cliente/1
    @GetMapping("/cliente/{clienteId}")
    @Operation(summary = "Obtener pedidos por cliente", description = "Obtener los pedidos de un cliente en específico por ID de cliente")
    @Parameter(name = "clienteId", description = "ID del cliente", required = true)
    public ResponseEntity<CollectionModel<EntityModel<Pedido>>> obtenerPedidosPorCliente(@PathVariable Long clienteId) {
        if (clienteId == null || clienteId <= 0) {
            return ResponseEntity.badRequest().build();
        }

        List<Pedido> pedidos = pedidoService.obtenerPedidosPorCliente(clienteId);

        if (pedidos.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        List<EntityModel<Pedido>> pedidosModel = pedidos.stream()
            .map(assembler::toModel)
            .toList();

        CollectionModel<EntityModel<Pedido>> collectionModel = CollectionModel.of(
            pedidosModel,
            linkTo(methodOn(PedidoController.class).obtenerPedidosPorCliente(clienteId)).withSelfRel()
        );

        return ResponseEntity.ok(collectionModel);
    }


// Obtener pedidos por fecha exacta
// http://localhost:8080/api/v1/pedidos/fecha
// Ve a la pestaña Params y agrega Key: fecha Value: ####
    @GetMapping("/fecha")
    @Operation(summary = "Obtener pedidos por fecha exacta", description = "Obtener pedidos por fecha exacta en formato YYYY-MM-DD")
    @Parameter(name = "fecha", description = "Fecha exacta", required = true)
    public ResponseEntity<CollectionModel<EntityModel<Pedido>>> obtenerPedidosPorFecha(
            @RequestParam("fecha") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha) {
        List<Pedido> pedidos = pedidoService.obtenerPedidosPorFecha(fecha);
        if (pedidos.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        List<EntityModel<Pedido>> pedidosModel = pedidos.stream()
            .map(assembler::toModel)
            .toList();
        CollectionModel<EntityModel<Pedido>> collectionModel = CollectionModel.of(
            pedidosModel,
            linkTo(methodOn(PedidoController.class).obtenerPedidosPorFecha(fecha)).withSelfRel()
        );
        return ResponseEntity.ok(collectionModel);
    }

// Obtener pedidos entre fechas
// http://localhost:8080/api/v1/pedidos/rango-fechas
// Key: desde → Value: ####
// Key: hasta → Value: ####
    @GetMapping("/rango-fechas")
    @Operation(summary = "Obtener pedidos entre fechas exactas", description = "Obtener pedidos entre las fechas dadas")
    @Parameters({
        @Parameter(name = "desde", description = "Fecha desde (YYYY-MM-DD)", required = true),
        @Parameter(name = "hasta", description = "Fecha hasta (YYYY-MM-DD)", required = true)
    })
    public ResponseEntity<CollectionModel<EntityModel<Pedido>>> obtenerPedidosEntreFechas(
            @RequestParam("desde") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate desde,
            @RequestParam("hasta") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate hasta) {
        List<Pedido> pedidos = pedidoService.obtenerPedidosEntreFechas(desde, hasta);
        if (pedidos.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        List<EntityModel<Pedido>> pedidosModel = pedidos.stream()
            .map(assembler::toModel)
            .toList();
        CollectionModel<EntityModel<Pedido>> collectionModel = CollectionModel.of(
            pedidosModel,
            linkTo(methodOn(PedidoController.class).obtenerPedidosEntreFechas(desde, hasta)).withSelfRel()
        );
        return ResponseEntity.ok(collectionModel);
    }

    // Contar pedidos por cliente
    // http://localhost:8080/api/v1/pedidos/cliente/1/cantidad
    @GetMapping("/cliente/{clienteId}/cantidad")
    @Operation(
        summary = "Contar pedidos por cliente",
        description = "Cuenta la lista de pedidos en total del cliente"
    )
    @Parameter(
        name = "clienteId",
        description = "ID del cliente",
        required = true
    )
    public ResponseEntity<Long> contarPedidosPorCliente(@PathVariable Long clienteId) {
    Long cantidad = pedidoService.contarPedidosPorCliente(clienteId);
    return ResponseEntity.ok(cantidad);
    }


// Obtener los últimos 10 pedidos
// http://localhost:8080/api/v1/pedidos/ultimos
    @GetMapping(value = "/ultimos", produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Últimos 10 pedidos", description = "Lee los últimos 10 pedidos y los retorna con enlaces HATEOAS")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Operación exitosa"),
        @ApiResponse(responseCode = "404", description = "No se encontraron pedidos")
    })
    public ResponseEntity<CollectionModel<EntityModel<Pedido>>> obtenerUltimosPedidos() {
        List<Pedido> ultimos = pedidoService.obtenerUltimosPedidos();
        List<EntityModel<Pedido>> pedidosModel = ultimos.stream()
            .map(assembler::toModel)
            .toList();
        CollectionModel<EntityModel<Pedido>> collectionModel = CollectionModel.of(
            pedidosModel,
            linkTo(methodOn(PedidoController.class).obtenerUltimosPedidos()).withSelfRel(),
            linkTo(methodOn(PedidoController.class).listarPedidos()).withRel("todos")
        );
        return ResponseEntity.ok(collectionModel);
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
    @Operation(summary = "Modificar pedido", description = "Busca un pedido por ID y lo modifica")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Operación exitosa"),
        @ApiResponse(responseCode = "404", description = "Pedido no encontrado")
    })
    public ResponseEntity<Pedido> actualizar(@PathVariable Long id, @RequestBody Pedido pedido) {
        try {
            Pedido ped = pedidoService.findById(id);

            ped.setId(id);
            ped.setClienteId(pedido.getClienteId());
            ped.setFecha(pedido.getFecha());
            ped.setTotal(pedido.getTotal());

            // Limpia productos anteriores y asigna los nuevos
            ped.getProductos().clear();
            for (PedidoProducto producto : pedido.getProductos()) {
                producto.setPedido(ped); // Relación bidireccional
                ped.getProductos().add(producto);
            }

            pedidoService.save(ped);
            return ResponseEntity.ok(ped);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
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