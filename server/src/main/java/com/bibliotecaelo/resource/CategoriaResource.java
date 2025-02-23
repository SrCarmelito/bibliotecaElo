package com.bibliotecaelo.resource;

import com.bibliotecaelo.domain.Categoria;
import com.bibliotecaelo.dto.CategoriaDTO;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/categorias")
public class CategoriaResource extends CrudResource<Categoria, CategoriaDTO>{
}
