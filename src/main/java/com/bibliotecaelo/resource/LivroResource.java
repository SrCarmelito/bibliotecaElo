package com.bibliotecaelo.resource;

import com.bibliotecaelo.domain.Livro;
import com.bibliotecaelo.dto.LivroDTO;
import com.bibliotecaelo.service.LivroService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/livros")
public class LivroResource
        extends CrudResource<Livro, LivroDTO> {

    public LivroService getService() {
        return (LivroService) this.crudService;
    }

}
