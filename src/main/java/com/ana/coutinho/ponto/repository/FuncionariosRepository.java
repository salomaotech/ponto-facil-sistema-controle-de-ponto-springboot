package com.ana.coutinho.ponto.repository;

import com.ana.coutinho.ponto.model.Funcionarios;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FuncionariosRepository extends JpaRepository<Funcionarios, Long> {

    // Sobrescrevendo findByCpfContaining para ordenar por nome em ordem crescente
    @Query("SELECT f FROM Funcionarios f WHERE f.cpf LIKE %:cpf% ORDER BY f.nome ASC")
    List<Funcionarios> findByCpfContaining(String cpf);

    // Sobrescrevendo findAll para ordenar por nome em ordem crescente
    @Override
    @Query("SELECT f FROM Funcionarios f ORDER BY f.nome ASC")
    List<Funcionarios> findAll();

}
