package com.Vineyard.microservicio.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/pedido")
public class PedidoController {

    @Autowired
    private PedidoController pedidoController;
}
