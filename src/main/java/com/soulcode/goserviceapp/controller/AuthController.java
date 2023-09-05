package com.soulcode.goserviceapp.controller;

import com.soulcode.goserviceapp.domain.Cliente;
import com.soulcode.goserviceapp.domain.Usuario;
import com.soulcode.goserviceapp.domain.enums.Perfil;
import com.soulcode.goserviceapp.repository.UsuarioRepository;
import com.soulcode.goserviceapp.service.AuthService;
import com.soulcode.goserviceapp.service.UsuarioService;
import com.soulcode.goserviceapp.service.exceptions.SenhaIncorretaException;
import com.soulcode.goserviceapp.service.exceptions.UsuarioNaoAutenticadoException;
import com.soulcode.goserviceapp.service.exceptions.UsuarioNaoEncontradoException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@Controller
@RequestMapping(value = "/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @GetMapping(value = "/login")
    public ModelAndView login(@RequestParam(name = "error", required = false) String error) {
        ModelAndView mv = new ModelAndView("login");
        if(error != null) {
            mv.addObject("errorMessage", "Erro ao autenticar no sistema. Verifique suas credenciais.");
        }
        return mv;
    }

    @GetMapping(value = "/cadastro")
    public String cadastro() {
        return "cadastroCliente";
    }

    @PostMapping(value = "/cadastro")
    public String cadastrarCliente(Cliente cliente, RedirectAttributes attributes) {
        try {
            authService.createCliente(cliente);
            attributes.addFlashAttribute("successMessage", "Novo cliente cadastrado com sucesso!");
            return "redirect:/auth/login";
        } catch (Exception ex) {
            attributes.addFlashAttribute("errorMessage", "Erro ao cadastrar novo cliente.");
            return "redirect:/auth/cadastro";
        }
    }

    @GetMapping(value = "/password/new")
    public String alterarSenha() {
        return "alterarSenha";
    }

    @PostMapping(value = "/password/new")
    public String updatePassword(
            @RequestParam(name = "senhaAtual") String senhaAtual,
            @RequestParam(name = "senhaNova") String senhaNova,
            Authentication authentication,
            RedirectAttributes attributes
    ) {
        try {
            authService.updatePassword(authentication, senhaAtual, senhaNova);
            attributes.addFlashAttribute("successMessage", "Senha alterada.");
        } catch (UsuarioNaoAutenticadoException | UsuarioNaoEncontradoException | SenhaIncorretaException ex) {
            attributes.addFlashAttribute("errorMessage", ex.getMessage());
        } catch (Exception ex) {
            attributes.addFlashAttribute("errorMessage", "Erro ao tentar alterar a senha.");
        }
        return "redirect:/auth/password/new";
    }

    @GetMapping(value = "/alterar-dados")
    public String alterarDadosCadastrais() { return "alterarDadosCadastrais"; }

    @PostMapping(value = "/alterar-dados")
    public String alterarDados(
            @RequestParam(name = "nome") String nome,
            @RequestParam(name = "email") String email,
            Authentication authentication,
            RedirectAttributes attributes
    ){

        if(authentication != null && authentication.isAuthenticated()) {

            String emailAuthenticated = authentication.getName();
            Optional<Usuario> optionalUsuario = usuarioRepository.findByEmail(emailAuthenticated);

            if (optionalUsuario.isPresent() && optionalUsuario.get().getPerfil() == Perfil.ADMIN){

                if (nome.isEmpty() | email.isEmpty()){
                    attributes.addFlashAttribute("errorMessage", "Preencha todos os dados");
                    return "redirect:/auth/alterar-dados";
                }

                authService.alterarDados(authentication, nome, email);
                attributes.addFlashAttribute("successMessage", "Dados alterados, faça login novamente!");
            } else {
                attributes.addFlashAttribute("errorMessage", "Você não é um adiministrador");
                return "redirect:/auth/alterar-dados";
            }
        }
        return "redirect:/auth/login";
    }

}
