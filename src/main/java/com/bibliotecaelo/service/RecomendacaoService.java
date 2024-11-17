package com.bibliotecaelo.service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.bibliotecaelo.converter.LivroDTOConverter;
import com.bibliotecaelo.domain.Livro;
import com.bibliotecaelo.dto.LivroDTO;
import com.bibliotecaelo.repository.LivroRepository;
import org.springframework.stereotype.Service;

@Service
public class RecomendacaoService {

    private final LivroRepository livroRepository;

    private final LivroDTOConverter livroDTOConverter;

    public RecomendacaoService(LivroRepository livroRepository, LivroDTOConverter livroDTOConverter) {
        this.livroRepository = livroRepository;
        this.livroDTOConverter = livroDTOConverter;
    }

    public List<LivroDTO> getRecomendacoes(UUID usuarioId) {

        List<Livro> livrosEmprestados = livroRepository.livrosEmprestadosPorUsuarioId(usuarioId);
        List<Livro> livrosRecomendados = new ArrayList<>();
        List<LivroDTO> livrosRecomendadosDto = new ArrayList<>();

        livrosEmprestados
                .stream()
                .map(Livro::getCategoria)
                .distinct()
                .map(livroRepository::findAllByCategoria)
                .forEach(livrosRecomendados::addAll);

        livrosRecomendados.removeAll(livrosEmprestados);

        livrosRecomendados.forEach(lr -> livrosRecomendadosDto.add(livroDTOConverter.to(lr)));

        return livrosRecomendadosDto;
    }
}
