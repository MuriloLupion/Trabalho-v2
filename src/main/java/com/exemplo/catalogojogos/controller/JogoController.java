package com.exemplo.catalogojogos.controller;

import com.exemplo.catalogojogos.model.Jogo;
import com.exemplo.catalogojogos.repository.JogoRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/jogos")
public class JogoController {

    private final JogoRepository jogoRepository;

    public JogoController(JogoRepository jogoRepository) {
        this.jogoRepository = jogoRepository;
    }

    @GetMapping
    public ResponseEntity<List<Jogo>> listar(
            @RequestParam(required = false) String nome,
            @RequestParam(required = false) String genero,
            @RequestParam(required = false) String plataforma,
            @RequestParam(required = false) Boolean concluido) {

        List<Jogo> resultado;
        if (nome == null && genero == null && plataforma == null && concluido == null) {
            resultado = jogoRepository.findAll();
        } else {
            resultado = jogoRepository.filtrar(nome, genero, plataforma, concluido);
        }

        return ResponseEntity.ok(resultado);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Jogo> buscarPorId(@PathVariable Long id) {
        Jogo jogo = encontrarJogo(id);
        if (jogo == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(jogo);
    }

    @PostMapping
    public ResponseEntity<Jogo> cadastrar(@RequestBody Jogo novoJogo) {
        if (!dadosValidos(novoJogo)) {
            return ResponseEntity.badRequest().build();
        }

        Jogo jogo = new Jogo(null, novoJogo.getNome(), novoJogo.getGenero(),
                novoJogo.getPlataforma(), novoJogo.isConcluido());
        jogoRepository.save(jogo);
        return ResponseEntity.status(HttpStatus.CREATED).body(jogo);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Jogo> atualizar(@PathVariable Long id, @RequestBody Jogo dados) {
        Jogo jogo = encontrarJogo(id);
        if (jogo == null) {
            return ResponseEntity.notFound().build();
        }
        if (!dadosValidos(dados)) {
            return ResponseEntity.badRequest().build();
        }

        jogo.setNome(dados.getNome());
        jogo.setGenero(dados.getGenero());
        jogo.setPlataforma(dados.getPlataforma());
        jogo.setConcluido(dados.isConcluido());
        jogoRepository.save(jogo);
        return ResponseEntity.ok(jogo);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        Jogo jogo = encontrarJogo(id);
        if (jogo == null) {
            return ResponseEntity.notFound().build();
        }
        jogoRepository.delete(jogo);
        return ResponseEntity.noContent().build();
    }

    private Jogo encontrarJogo(Long id) {
        return jogoRepository.findById(id).orElse(null);
    }

    private boolean dadosValidos(Jogo jogo) {
        return jogo != null
                && jogo.getNome() != null && !jogo.getNome().isBlank()
                && jogo.getGenero() != null && !jogo.getGenero().isBlank()
                && jogo.getPlataforma() != null && !jogo.getPlataforma().isBlank();
    }
}
