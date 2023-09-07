package com.soulcode.goserviceapp.service;

import com.soulcode.goserviceapp.domain.Cliente;
import com.soulcode.goserviceapp.domain.Endereco;
import com.soulcode.goserviceapp.domain.Prestador;
import com.soulcode.goserviceapp.domain.Usuario;
import com.soulcode.goserviceapp.domain.enums.Perfil;
import com.soulcode.goserviceapp.repository.EnderecoRepository;
import com.soulcode.goserviceapp.repository.UsuarioRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class EnderecoService {

    @Autowired
    private EnderecoRepository enderecoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private ClienteService clienteService;

    @Autowired
    private PrestadorService prestadorService;


    public EnderecoService(EnderecoRepository enderecoRepository) {
        this.enderecoRepository = enderecoRepository;


    }

    public Endereco findById(Long id) {
        Optional<Endereco> endereco = enderecoRepository.findById(id);
        if (endereco.isPresent()) {
            return endereco.get();
        } else {
            throw new RuntimeException();
        }
    }

    @Transactional
    public void alterarEndereco(Endereco endereco, Authentication authentication) {
        Usuario usuario = usuarioService.findAuthenticated(authentication);
        if (usuario.getPerfil().equals(Perfil.CLIENTE)) {
            Cliente cliente = clienteService.findAuthenticated(authentication);
            Endereco updatedEndereco = this.findById(cliente.getEndereco().getId());
            updatedEndereco.setCidade(endereco.getCidade());
            updatedEndereco.setLogradouro(endereco.getLogradouro());
            updatedEndereco.setNumero(endereco.getNumero());
            updatedEndereco.setUf(endereco.getUf());
            enderecoRepository.save(updatedEndereco);
        }
        if (usuario.getPerfil().equals(Perfil.PRESTADOR)) {
            Prestador prestador = prestadorService.findAuthenticated(authentication);
            Endereco updatedEndereco = this.findById(prestador.getEndereco().getId());
            updatedEndereco.setCidade(endereco.getCidade());
            updatedEndereco.setLogradouro(endereco.getLogradouro());
            updatedEndereco.setNumero(endereco.getNumero());
            updatedEndereco.setUf(endereco.getUf());
            enderecoRepository.save(updatedEndereco);
        }
    }
}





