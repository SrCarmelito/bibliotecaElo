delete from biblioteca.emprestimo;

insert into biblioteca.emprestimo(id, usuario_id, livro_id, data_emprestimo, data_devolucao, status, datacriacao, dataalteracao, usuariocriacao, usuarioalteracao) VALUES ('2638b13d-df17-4baa-bd65-46f90b43abe7', '5bc26f63-fc13-4e4f-8fc3-524b223a7d34', '9d707fa8-ce8b-4ec9-8b6d-5e235386a3da', '2024-05-14 06:33:59'::timestamp, '2025-05-14 06:33:59'::timestamp, 'AGUARDANDO_DEVOLUCAO', '2024-11-14 06:33:59'::timestamp, '2024-11-14 06:33:59'::timestamp, 'system', 'system');
