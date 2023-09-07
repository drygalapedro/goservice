package com.soulcode.goserviceapp.controller;

import com.soulcode.goserviceapp.domain.Endereco;
import com.soulcode.goserviceapp.domain.Usuario;
import com.soulcode.goserviceapp.domain.enums.Perfil;
import com.soulcode.goserviceapp.repository.UsuarioRepository;
import com.soulcode.goserviceapp.service.AuthService;
import com.soulcode.goserviceapp.service.EnderecoService;
import com.soulcode.goserviceapp.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@Controller
public class EnderecoController {

    @Autowired
    private EnderecoService enderecoService;

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping(value = "/alterar-endereco")
    public String alterarEndereco() {
        return "/alterarEndereco";
    }

    @PostMapping(value = "/alterar-endereco")
    public String alterarEndereco(Endereco endereco, RedirectAttributes attributes, Authentication authentication) {

        try{
            enderecoService.alterarEndereco(endereco, authentication);
            attributes.addFlashAttribute("successMessage", "Endereço alterado com sucesso!");
        }
        catch (RuntimeException e ){
            attributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/alterar-endereco";
    }



}
