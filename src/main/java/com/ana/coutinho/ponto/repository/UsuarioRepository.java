package com.ana.coutinho.ponto.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.ana.coutinho.ponto.model.Usuario;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Optional<Usuario> findByEmailAndPassword(String email, String password);

    Optional<Usuario> findByEmail(String email);

    Optional<Usuario> findByEmailAndKeypass(String email, String keypass);

}
