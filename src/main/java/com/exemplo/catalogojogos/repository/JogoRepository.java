package com.exemplo.catalogojogos.repository;

import com.exemplo.catalogojogos.model.Jogo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface JogoRepository extends JpaRepository<Jogo, Long> {

    @Query("""
            SELECT j FROM Jogo j
            WHERE (:nome IS NULL OR LOWER(j.nome) = LOWER(:nome))
              AND (:genero IS NULL OR LOWER(j.genero) = LOWER(:genero))
              AND (:plataforma IS NULL OR LOWER(j.plataforma) = LOWER(:plataforma))
              AND (:concluido IS NULL OR j.concluido = :concluido)
            ORDER BY j.id
            """)
    List<Jogo> filtrar(@Param("nome") String nome,
                       @Param("genero") String genero,
                       @Param("plataforma") String plataforma,
                       @Param("concluido") Boolean concluido);
}
