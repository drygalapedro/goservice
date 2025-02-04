package com.soulcode.goserviceapp.service;

import com.soulcode.goserviceapp.domain.Servico;
import com.soulcode.goserviceapp.domain.Usuario;
import com.soulcode.goserviceapp.repository.ServicoRepository;
import com.soulcode.goserviceapp.service.exceptions.ServicoNaoEncontradoException;
import com.soulcode.goserviceapp.service.exceptions.UsuarioNaoEncontradoException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ServicoService {
    @Autowired
    private ServicoRepository servicoRepository;

    @Cacheable(cacheNames = "redisCache")
    public List<Servico> findAll(){
        System.err.println("BUSCANDO NO BANCO DE DADOS...");
        return servicoRepository.findAll();
    }

    public Servico createServico(Servico servico){
        servico.setId(null);
        return servicoRepository.save(servico);
    }

    public void removeServicoById(Long id){
        servicoRepository.deleteById(id);
    }

    public Servico findById(Long id){
        Optional<Servico> servico = servicoRepository.findById(id);
        if(servico.isPresent()){
            return servico.get();
        } else {
            throw new ServicoNaoEncontradoException();
        }
    }

    public Servico update(Servico servico){
        Servico updatedServico = this.findById(servico.getId());
        updatedServico.setNome(servico.getNome());
        updatedServico.setDescricao(servico.getDescricao());
        updatedServico.setCategoria(servico.getCategoria());
        return servicoRepository.save(updatedServico);
    }

    public List<Servico> findByPrestadorEmail(String email) {
        return servicoRepository.findByPrestadorEmail(email);
    }

    public List<Servico> findServiceByNome(String search, int page){
        return servicoRepository.findServiceByNome(search, page);
    }

    public List<Servico> buscarServicosPaginados(int offset) {
        return servicoRepository.buscaServicosPaginados(offset);
    }
}
