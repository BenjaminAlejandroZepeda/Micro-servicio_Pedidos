package com.Vineyard.microservicio.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import com.Vineyard.microservicio.model.Pedido;
import com.Vineyard.microservicio.model.PedidoProducto;
import com.Vineyard.microservicio.repository.PedidoRepository;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
// 
@SpringBootTest
public class PedidoServiceTest {
//	Inyecta automáticamente dependencias 
    @Autowired
    private PedidoService pedidoService;
//Inyecta un mock del repositorio en el contexto de Spring.
    @MockitoBean
    private PedidoRepository pedidoRepository;

    @Test
    public void testListarPedidos() {
        when(pedidoRepository.findAll()).thenReturn(List.of(new Pedido()));

        List<Pedido> pedidos = pedidoService.listarPedidos();

        assertNotNull(pedidos);
        assertEquals(1, pedidos.size());
    }

    @Test
    public void testObtenerPedidosPorCliente() {
        Long clienteId = 1L;
        when(pedidoRepository.findByClienteId(clienteId)).thenReturn(List.of(new Pedido()));

        List<Pedido> pedidos = pedidoService.obtenerPedidosPorCliente(clienteId);

        assertNotNull(pedidos);
        assertEquals(1, pedidos.size());
    }

    @Test
    public void testObtenerPedidosPorFecha() {
        LocalDate fecha = LocalDate.now();
        when(pedidoRepository.findByFecha(fecha)).thenReturn(List.of(new Pedido()));

        List<Pedido> pedidos = pedidoService.obtenerPedidosPorFecha(fecha);

        assertNotNull(pedidos);
        assertEquals(1, pedidos.size());
    }

    @Test
    public void testObtenerPedidosEntreFechas() {
        LocalDate desde = LocalDate.now().minusDays(5);
        LocalDate hasta = LocalDate.now();
        when(pedidoRepository.findByFechaBetween(desde, hasta)).thenReturn(List.of(new Pedido()));

        List<Pedido> pedidos = pedidoService.obtenerPedidosEntreFechas(desde, hasta);

        assertNotNull(pedidos);
        assertEquals(1, pedidos.size());
    }

    @Test
    public void testContarPedidosPorCliente() {
        Long clienteId = 1L;
        when(pedidoRepository.countByClienteId(clienteId)).thenReturn(3L);

        long count = pedidoService.contarPedidosPorCliente(clienteId);

        assertEquals(3L, count);
    }

    @Test
    public void testObtenerUltimosPedidos() {
        when(pedidoRepository.findTop10ByOrderByFechaDesc()).thenReturn(List.of(new Pedido()));

        List<Pedido> pedidos = pedidoService.obtenerUltimosPedidos();

        assertNotNull(pedidos);
        assertEquals(1, pedidos.size());
    }

    @Test
    public void testEliminarPedido_Existe() {
        Long id = 1L;
        when(pedidoRepository.existsById(id)).thenReturn(true);
        doNothing().when(pedidoRepository).deleteById(id);

        pedidoService.eliminarPedido(id);

        verify(pedidoRepository, times(1)).deleteById(id);
    }

    @Test
    public void testEliminarPedido_NoExiste() {
        Long id = 1L;
        when(pedidoRepository.existsById(id)).thenReturn(false);

        assertThrows(RuntimeException.class, () -> pedidoService.eliminarPedido(id));
    }

    @Test
    public void testGuardarOActualizarPedido() {
        Pedido pedido = new Pedido();
        PedidoProducto producto = new PedidoProducto();
        pedido.setProductos(List.of(producto));

        when(pedidoRepository.save(pedido)).thenReturn(pedido);

        Pedido resultado = pedidoService.guardarOActualizarPedido(pedido);

        assertNotNull(resultado);
        assertEquals(pedido, producto.getPedido());
    }
}
