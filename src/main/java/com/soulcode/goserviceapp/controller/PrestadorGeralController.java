package com.soulcode.goserviceapp.controller;

import com.soulcode.goserviceapp.domain.Prestador;
import com.soulcode.goserviceapp.service.PrestadorService;
import com.soulcode.goserviceapp.service.exceptions.UsuarioNaoAutenticadoException;
import com.soulcode.goserviceapp.service.exceptions.UsuarioNaoEncontradoException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class PrestadorGeralController {


    @Autowired
    private PrestadorService prestadorService;

//    @Value("${default.image.url}")
    @Value(value = "default.image.url")
    private String defaultImageUrl;

    @ModelAttribute("defaultImageUrl")
    public String addDefaultImageUrl() {
        return defaultImageUrl;
    }

    @ModelAttribute("prestador")
    public Prestador addPrestadorGeral(Authentication authentication) {
        if (authentication != null) {
            try {
                return prestadorService.findAuthenticated(authentication);
            } catch (UsuarioNaoAutenticadoException | UsuarioNaoEncontradoException ignored) {
            }
        }
        return null;
    }
}
