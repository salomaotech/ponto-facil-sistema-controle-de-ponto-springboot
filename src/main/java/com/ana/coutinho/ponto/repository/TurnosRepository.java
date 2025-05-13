package com.ana.coutinho.ponto.repository;

import com.ana.coutinho.ponto.model.Turnos;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TurnosRepository extends JpaRepository<Turnos, Long> {

    /**
     * Verifica se existe pelo menos um registro na tabela.
     *
     * Exemplo de SQL gerado:
     * 
     * SELECT CASE WHEN COUNT(*) > 0 THEN TRUE ELSE FALSE END FROM nome_da_tabela;
     * 
     * 
     * @return true se houver registros, false caso contrário.
     */
    boolean existsBy();

}