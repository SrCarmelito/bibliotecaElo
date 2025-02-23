package com.bibliotecaelo.converter;

import com.bibliotecaelo.interfaces.Entidade;
import com.bibliotecaelo.interfaces.EntidadeDTO;

public interface DTOConverter<E extends Entidade, D extends EntidadeDTO> {

    default E from (D dto) {
        return from(dto, null);
    }

    E from(D dto, E entity);

    D to(E entity);

}
