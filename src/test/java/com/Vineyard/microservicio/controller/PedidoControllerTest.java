package com.Vineyard.microservicio.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.Vineyard.microservicio.assemblers.PedidoModelAssembler;
import com.Vineyard.microservicio.model.Pedido;
import com.Vineyard.microservicio.model.PedidoProducto;
import com.Vineyard.microservicio.service.PedidoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import org.springframework.http.MediaType;

//Carga solo el contexto web de Spring MVC para probar controladores.

@WebMvcTest(PedidoController.class)
public class PedidoControllerTest {
//Inyecta automáticamente dependencias 
    @Autowired
    private MockMvc mockMvc;
//Inyecta un mock del repositorio en el contexto de Spring.
    @MockitoBean
    private PedidoService pedidoService;

    @MockitoBean
    private PedidoModelAssembler assembler;

    @Autowired
    private ObjectMapper objectMapper;

    private Pedido pedido;
//Inicializa datos de prueba antes de cada test.

    @BeforeEach
    void setUp() {
        pedido = new Pedido();
        pedido.setId(1L);
        pedido.setFecha(LocalDate.now());
        pedido.setTotal(100.0);
        pedido.setClienteId(2L);
        pedido.setProductos(List.of(new PedidoProducto())); 

    EntityModel<Pedido> pedidoModel = EntityModel.of(pedido);
    when(assembler.toModel(any(Pedido.class))).thenReturn(pedidoModel);
    when(assembler.toCollectionModel(any())).thenReturn(CollectionModel.of(List.of(pedidoModel)));
    }


    @Test
    public void testListarPedidos() throws Exception {
        // Simula el modelo HATEOAS
        EntityModel<Pedido> pedidoModel = EntityModel.of(pedido);
        when(pedidoService.listarPedidos()).thenReturn(List.of(pedido));
        when(assembler.toCollectionModel(any())).thenReturn(CollectionModel.of(List.of(pedidoModel)));

        mockMvc.perform(get("/api/v1/pedidos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("_embedded.pedidoList[0].id").value(1));
    }


    @Test
    public void testObtenerPedidosPorCliente() throws Exception {
        // Simula el modelo HATEOAS
        EntityModel<Pedido> pedidoModel = EntityModel.of(pedido);
        when(pedidoService.obtenerPedidosPorCliente(2L)).thenReturn(List.of(pedido));
        when(assembler.toCollectionModel(any())).thenReturn(CollectionModel.of(List.of(pedidoModel)));

        mockMvc.perform(get("/api/v1/pedidos/cliente/2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("_embedded.pedidoList[0].clienteId").value(2));
    }


    @Test
    public void testObtenerPedidosPorFecha() throws Exception {
        LocalDate fecha = LocalDate.now();

        // Simula el modelo HATEOAS
        EntityModel<Pedido> pedidoModel = EntityModel.of(pedido);
        when(pedidoService.obtenerPedidosPorFecha(fecha)).thenReturn(List.of(pedido));
        when(assembler.toCollectionModel(any())).thenReturn(CollectionModel.of(List.of(pedidoModel)));

        mockMvc.perform(get("/api/v1/pedidos/fecha")
                        .param("fecha", fecha.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("_embedded.pedidoList[0].id").value(1));
    }

    @Test
    public void testObtenerPedidosEntreFechas() throws Exception {
        LocalDate desde = LocalDate.now().minusDays(5);
        LocalDate hasta = LocalDate.now();

        // Simula el modelo HATEOAS
        EntityModel<Pedido> pedidoModel = EntityModel.of(pedido);
        when(pedidoService.obtenerPedidosEntreFechas(desde, hasta)).thenReturn(List.of(pedido));
        when(assembler.toCollectionModel(any())).thenReturn(CollectionModel.of(List.of(pedidoModel)));

        mockMvc.perform(get("/api/v1/pedidos/rango-fechas")
                        .param("desde", desde.toString())
                        .param("hasta", hasta.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("_embedded.pedidoList[0].id").value(1));
    }

        

    @Test
    public void testContarPedidosPorCliente() throws Exception {
        when(pedidoService.contarPedidosPorCliente(2L)).thenReturn(5L);

        mockMvc.perform(get("/api/v1/pedidos/cliente/2/cantidad"))
                .andExpect(status().isOk())
                .andExpect(content().string("5"));
    }

    @Test
    public void testObtenerUltimosPedidos() throws Exception {
        // Simula el modelo HATEOAS
        EntityModel<Pedido> pedidoModel = EntityModel.of(pedido);
        when(pedidoService.obtenerUltimosPedidos()).thenReturn(List.of(pedido));
        when(assembler.toCollectionModel(any())).thenReturn(CollectionModel.of(List.of(pedidoModel)));

        mockMvc.perform(get("/api/v1/pedidos/ultimos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("_embedded.pedidoList[0].id").value(1));
    }


    @Test
    public void testCrearPedido() throws Exception {
        // Simula el modelo HATEOAS
        EntityModel<Pedido> pedidoModel = EntityModel.of(pedido);
        when(pedidoService.guardarOActualizarPedido(any(Pedido.class))).thenReturn(pedido);
        when(assembler.toModel(any(Pedido.class))).thenReturn(pedidoModel);

        mockMvc.perform(post("/api/v1/pedidos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(pedido)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("id").value(1));
    }


    @Test
    public void testEliminarPedido() throws Exception {
        // Simula la eliminación sin errores
        doNothing().when(pedidoService).eliminarPedido(1L);

        mockMvc.perform(delete("/api/v1/pedidos/1"))
                .andExpect(status().isNoContent());

        // Verifica que el servicio fue llamado exactamente una vez
        verify(pedidoService, times(1)).eliminarPedido(1L);
    }

}


