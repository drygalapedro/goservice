package com.soulcode.goserviceapp.repository;

import com.soulcode.goserviceapp.domain.Endereco;
import com.soulcode.goserviceapp.domain.Prestador;
import com.soulcode.goserviceapp.domain.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface EnderecoRepository extends JpaRepository<Endereco, Long> {


    @Transactional
    @Modifying
    @Query(value = "UPDATE enderecos SET enderecos.cidade = ?, enderecos.logradouro = ?, enderecos.numero = ?, enderecos.uf = ? WHERE usuario_id = ?", nativeQuery = true)
    void updateEndereco(String cidade, String logradouro, String numero, String uf, Long usuarioId);

}
