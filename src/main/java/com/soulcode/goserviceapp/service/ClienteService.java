package com.soulcode.goserviceapp.service;

import com.soulcode.goserviceapp.domain.Cliente;
import com.soulcode.goserviceapp.repository.ClienteRepository;
import com.soulcode.goserviceapp.service.exceptions.UsuarioNaoAutenticadoException;
import com.soulcode.goserviceapp.service.exceptions.UsuarioNaoEncontradoException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.OptionalInt;

@Service
public class ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;

    public Cliente findAuthenticated(Authentication authentication){
        if (authentication != null && authentication.isAuthenticated()){
            Optional<Cliente> cliente = clienteRepository.findByEmail(authentication.getName());
            if(cliente.isPresent()){
                return cliente.get();
            } else {
                throw new UsuarioNaoEncontradoException();
            }
        } else {
            throw new UsuarioNaoAutenticadoException();
        }
    }

    public Cliente findById(Long id){
        Optional<Cliente> cliente = clienteRepository.findById(id);
        if(cliente.isPresent()){
            return cliente.get();
        } else {
            throw  new UsuarioNaoEncontradoException();
        }
    }
    public Cliente update(Cliente cliente) {
        Cliente updatedCliente = this.findById(cliente.getId());
        updatedCliente.setNome(cliente.getNome());
        updatedCliente.setEmail(cliente.getEmail());
        updatedCliente.setTelefone(cliente.getTelefone());
        updatedCliente.setCpf(cliente.getCpf());
        updatedCliente.setDataNascimento(cliente.getDataNascimento());
        return clienteRepository.save(updatedCliente);
    }

    @Transactional
    public void updateProfilePic(Authentication authentication, String profilePicUrl){
        if(authentication != null && authentication.isAuthenticated()) {
            String emailAuthenticated = authentication.getName();
            Optional<Cliente> cliente = clienteRepository.findByEmail(emailAuthenticated);
            if(cliente.isPresent()) {
                Cliente existingCliente = cliente.get();
                existingCliente.setUrlFoto(profilePicUrl);
                clienteRepository.save(existingCliente);
                return;
            }
            throw new UsuarioNaoEncontradoException();
        }
        throw new UsuarioNaoAutenticadoException();
    }
}
