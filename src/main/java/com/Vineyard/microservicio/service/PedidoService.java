package com.Vineyard.microservicio.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.Vineyard.microservicio.model.Pedido;
import com.Vineyard.microservicio.model.PedidoProducto;
import com.Vineyard.microservicio.repository.PedidoRepository;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class PedidoService {

@Autowired
private PedidoRepository pedidoRepository;

    public List<Pedido> listarPedidos(){
        return pedidoRepository.findAll();
    } 

    public List<Pedido> obtenerPedidosPorCliente(Long clienteId) {
        return pedidoRepository.findByClienteId(clienteId);
    }

    public List<Pedido> obtenerPedidosPorFecha(LocalDate fecha) {
        return pedidoRepository.findByFecha(fecha);
    }

    public List<Pedido> obtenerPedidosEntreFechas(LocalDate desde, LocalDate hasta) {
        return pedidoRepository.findByFechaBetween(desde, hasta);
    }

    public long contarPedidosPorCliente(Long clienteId) {
        return pedidoRepository.countByClienteId(clienteId);
    }

    public List<Pedido> obtenerUltimosPedidos() {
        return pedidoRepository.findTop10ByOrderByFechaDesc();
    }

    public void eliminarPedido(Long id) {
        if (!pedidoRepository.existsById(id)) {
            throw new RuntimeException("El pedido con ID " + id + " no existe.");
            }
            pedidoRepository.deleteById(id);}


    public Pedido guardarOActualizarPedido(Pedido pedido) {
    for (PedidoProducto producto : pedido.getProductos()) {
    producto.setPedido(pedido);
    }
    return pedidoRepository.save(pedido);
    }
}
