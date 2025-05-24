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

     /*
     * Obtiene todos los pedidos registrados en la base de datos.
     * return Lista de todos los pedidos.
     */
    public List<Pedido> listarPedidos(){
        return pedidoRepository.findAll();
    } 

     /*
     * Obtiene todos los pedidos realizados por un cliente específico.
     */
    public List<Pedido> obtenerPedidosPorCliente(Long clienteId) {
        return pedidoRepository.findByClienteId(clienteId);
    }

     /*
     * Obtiene todos los pedidos realizados en una fecha específica.
     */
    public List<Pedido> obtenerPedidosPorFecha(LocalDate fecha) {
        return pedidoRepository.findByFecha(fecha);
    }

     /*
     *  Obtiene los pedidos realizados entre dos fechas.
     *  desde Fecha de inicio.
     *  hasta Fecha de fin.
     */
    public List<Pedido> obtenerPedidosEntreFechas(LocalDate desde, LocalDate hasta) {
        return pedidoRepository.findByFechaBetween(desde, hasta);
    }

     /*
     * Cuenta la cantidad de pedidos realizados por un cliente específico.
     */
    public long contarPedidosPorCliente(Long clienteId) {
        return pedidoRepository.countByClienteId(clienteId);
    }

     /*
     * Obtiene los últimos 10 pedidos ordenados por fecha descendente.
     */
    public List<Pedido> obtenerUltimosPedidos() {
        return pedidoRepository.findTop10ByOrderByFechaDesc();
    }


     /*
     * Elimina un pedido por su ID.
     * 
     * ID del pedido a eliminar.
     * throws RuntimeException si el pedido no existe.
     */
    public void eliminarPedido(Long id) {
        if (!pedidoRepository.existsById(id)) {
            throw new RuntimeException("El pedido con ID " + id + " no existe.");
            }
            pedidoRepository.deleteById(id);}


     /*
     * Guarda un nuevo pedido o actualiza uno existente.
     * También establece la relación entre el pedido y sus productos.
     * 
     * Objeto Pedido a guardar o actualizar.
     * return Pedido guardado con sus productos asociados.
     */
    public Pedido guardarOActualizarPedido(Pedido pedido) {
        for (PedidoProducto producto : pedido.getProductos()) {
            producto.setPedido(pedido);
            }
        return pedidoRepository.save(pedido);
    }
}
