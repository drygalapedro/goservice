package com.soulcode.goserviceapp.repository;

import com.soulcode.goserviceapp.domain.Agendamento;
import com.soulcode.goserviceapp.domain.Servico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface AgendamentoRepository extends JpaRepository<Agendamento, Long> {



    @Query(value="SELECT a.* FROM agendamentos a JOIN usuarios u ON a.cliente_id = u.id WHERE u.email = ? ORDER BY data LIMIT ?, 10", nativeQuery = true)
    List<Agendamento> findByClienteEmail(String email, int page);
    
    @Query(value ="SELECT status_agendamento, COUNT(status_agendamento)FROM agendamentos GROUP BY status_agendamento", nativeQuery = true )
    List<Agendamento> findByServicoStatus();
  
    @Query(value = "SELECT a.* FROM agendamentos a JOIN usuarios u ON a.prestador_id = u.id WHERE u.email = ? ORDER BY data LIMIT ?, 10", nativeQuery = true)
    List<Agendamento> findByPrestadorEmail(String email, int page);

    @Query(value = "SELECT * FROM agendamentos a WHERE data BETWEEN ? AND ? LIMIT ?, 10", nativeQuery = true)
    List<Agendamento> findByData(String dataInicio, String dataFim, int page);
}
