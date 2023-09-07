package com.soulcode.goserviceapp.repository;

import com.soulcode.goserviceapp.domain.Usuario;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {


    Optional<Usuario> findByEmail(String email);

    @Modifying
    @Query(value = "UPDATE usuarios u SET u.senha = ? WHERE u.email = ?", nativeQuery = true)
    void updatePasswordByEmail(String senha, String email);

    @Modifying
    @Query(value = "UPDATE usuarios u SET u.habilitado = ? WHERE u.id = ?", nativeQuery = true)
    void updateEnableById(boolean habilitado, Long id);

    @Query(value = "SELECT * FROM usuarios u WHERE u.nome LIKE %?1% LIMIT ?, 10", nativeQuery = true)
    List<Usuario> findUserByNome(String search, int offset);

    @Modifying
    @Query(value = "UPDATE usuarios u SET u.nome = ?, u.email = ? WHERE u.id = ?", nativeQuery = true)
    void updateNomeEmail(String nome, String email, Long id);

    @Query(value = "SELECT * FROM usuarios LIMIT ? , 10", nativeQuery = true)
    List<Usuario> buscaUsuariosPaginados(int offset);

    @Query(value = "SELECT u.perfil = ?, COUNT(u) FROM Usuario u GROUP BY u.perfil", nativeQuery = true)
    List<Object[]> findUsersByPerfil(String perfil);
}
