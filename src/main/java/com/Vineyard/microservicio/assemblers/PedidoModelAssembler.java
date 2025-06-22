package com.Vineyard.microservicio.assemblers;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;
import com.Vineyard.microservicio.controller.PedidoController;
import com.Vineyard.microservicio.model.Pedido;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;


@Component
public class PedidoModelAssembler implements RepresentationModelAssembler<Pedido, EntityModel<Pedido>> {

    @Override
    public EntityModel<Pedido> toModel(Pedido pedido) {
        return EntityModel.of(pedido,
        
            linkTo(methodOn(PedidoController.class).listarPedidos()).withRel("todos"),
            linkTo(methodOn(PedidoController.class).obtenerPedidosPorCliente(pedido.getClienteId())).withRel("por-cliente"),
            linkTo(methodOn(PedidoController.class).contarPedidosPorCliente(pedido.getClienteId())).withRel("cantidad-cliente"),
            linkTo(methodOn(PedidoController.class).eliminarPedido(pedido.getId())).withRel("eliminar"),
            linkTo(methodOn(PedidoController.class).obtenerUltimosPedidos()).withRel("ultimos")
        );
    }
}
