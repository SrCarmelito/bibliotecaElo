package com.bibliotecaelo.repository;

import io.github.perplexhub.rsql.RSQLJPASupport;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface RsqlRepository<T, I> extends JpaRepository<T, I>, JpaSpecificationExecutor<T> {

    default Page<T> findByRsql(String search, Pageable pageable) {
        Specification<T> specification = RSQLJPASupport.toSpecification(search);
        return findAll(specification, pageable);
    }

}
