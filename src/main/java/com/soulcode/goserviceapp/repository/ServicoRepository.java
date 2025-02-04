package com.soulcode.goserviceapp.repository;

import com.soulcode.goserviceapp.domain.Servico;
import com.soulcode.goserviceapp.domain.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ServicoRepository extends JpaRepository<Servico, Long> {


    @Query(value =
            "SELECT s.*" +
                    " FROM servicos s" +
                    " JOIN prestadores_servicos ps ON s.id = ps.servico_id" +
                    " JOIN usuarios u ON u.id = ps.prestador_id" +
                    " WHERE u.email = ?", nativeQuery = true)
    List<Servico> findByPrestadorEmail(String email);

    @Query(value = "SELECT * FROM servicos s WHERE s.nome LIKE %?% LIMIT ? , 10", nativeQuery = true)
    List<Servico> findServiceByNome(String search, int page);

    @Query(value = "SELECT * FROM servicos LIMIT ? , 10", nativeQuery = true)
    List<Servico> buscaServicosPaginados(int offset);
}
