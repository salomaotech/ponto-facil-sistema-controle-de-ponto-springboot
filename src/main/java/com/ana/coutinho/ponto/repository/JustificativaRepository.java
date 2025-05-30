package com.ana.coutinho.ponto.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ana.coutinho.ponto.model.Justificativa;

import jakarta.transaction.Transactional;

@Repository
public interface JustificativaRepository extends JpaRepository<Justificativa, Long> {

        // Query personalizada para buscar justificativas pelo ID do funcionário
        @Query("SELECT j FROM Justificativa j WHERE j.funcionarios.id_funcionario = :idFuncionario")
        List<Justificativa> findByFuncionarioId(long idFuncionario);

        // Query para pesquisar entre datas
        @Query("SELECT j FROM Justificativa j " +
                        "WHERE (:idFuncionario IS NULL OR j.funcionarios.id_funcionario = :idFuncionario) " +
                        "AND (:dataInicio IS NULL OR j.dataFalta >= :dataInicio) " +
                        "AND (:dataFim IS NULL OR j.dataFalta <= :dataFim)")
        List<Justificativa> buscarPorFiltros(@Param("idFuncionario") Long idFuncionario,
                        @Param("dataInicio") LocalDate dataInicio,
                        @Param("dataFim") LocalDate dataFim);

        // Remove todas as justificativas relacionadas ao funcionario
        @Transactional
        @Modifying
        @Query("DELETE FROM Justificativa j WHERE j.funcionarios.id_funcionario = :idFuncionario")
        void deleteByFuncionarioId(@Param("idFuncionario") Long idFuncionario);
}
