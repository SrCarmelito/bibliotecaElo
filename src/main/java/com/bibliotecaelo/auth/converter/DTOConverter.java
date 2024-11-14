package com.bibliotecaelo.auth.converter;

public interface DTOConverter<E, D> {

    default E from (D dto) {
        return from(dto, null);
    }

    E from(D dto, E entity);

    D to(E entity);

}
