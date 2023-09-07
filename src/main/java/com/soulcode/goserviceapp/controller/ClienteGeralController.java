package com.soulcode.goserviceapp.controller;

import com.soulcode.goserviceapp.domain.Cliente;
import com.soulcode.goserviceapp.service.ClienteService;
import com.soulcode.goserviceapp.service.exceptions.UsuarioNaoAutenticadoException;
import com.soulcode.goserviceapp.service.exceptions.UsuarioNaoEncontradoException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class ClienteGeralController {
    @Autowired
    private ClienteService clienteService;

    @Value(value = "default.image.url")
    private String defaultImageUrl;

    @ModelAttribute("defaultImageUrl")
    public String addDefaultImageUrl() {
        return defaultImageUrl;
    }

    @ModelAttribute("cliente")
    public Cliente addClienteGeral(Authentication authentication) {
        if (authentication != null) {
            try {
                return clienteService.findAuthenticated(authentication);
            } catch (UsuarioNaoAutenticadoException | UsuarioNaoEncontradoException ignored) {
            }
        }
        return null;
    }
}
