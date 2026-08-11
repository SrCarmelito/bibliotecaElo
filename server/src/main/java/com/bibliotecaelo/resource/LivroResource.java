package com.bibliotecaelo.resource;

import com.bibliotecaelo.domain.Livro;
import com.bibliotecaelo.dto.LivroDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/livros")
public class LivroResource extends CrudBucketResource<Livro, LivroDTO> {}