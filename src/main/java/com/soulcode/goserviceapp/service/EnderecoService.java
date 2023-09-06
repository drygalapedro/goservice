package com.soulcode.goserviceapp.service;

import com.soulcode.goserviceapp.domain.Endereco;
import com.soulcode.goserviceapp.domain.Usuario;
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



    public EnderecoService(EnderecoRepository enderecoRepository) {
        this.enderecoRepository = enderecoRepository;


    }
    @Transactional
    public void alterarDados(Authentication authentication, String cidade, String logradouro, String numero, String uf){
        if(authentication != null && authentication.isAuthenticated()) {
            String emailAuthenticated = authentication.getName();
            Optional<Usuario> optionalUsuario = usuarioRepository.findByEmail(emailAuthenticated);

            if (optionalUsuario.isPresent()){
                Long usuarioId = optionalUsuario.get().getId();
                enderecoRepository.updateEndereco(cidade, logradouro, numero, uf, usuarioId);
            }

        }
    }
}



