delete from biblioteca.emprestimo;
delete from biblioteca.usuario;

insert into biblioteca.usuario (
	id,
	nome,
	email,
	data_cadastro,
	telefone,
	login,
	senha,
	situacao,
	reset_token,
	datacriacao,
	dataalteracao,
	usuariocriacao,
	usuarioalteracao)
values(
	'ee4ae880-a4db-4563-b330-7e2a27d26115',
	'Carmelito Junior Delcielo Benali',
	'carmelito.benali@gmail.com',
	'2024-11-13 10:17:52.579',
	'44988080437',
	'junior',
	'$2a$12$gSOIuZSP3hDBZ5V0v.9k0O1wLMYyBGOG/uPAe3odJ8.ygto5Hxi0e',
	'ATIVO',
	'eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJqdW5pb3IiLCJpZCI6ImVlNGFlODgwLWE0ZGItNDU2My1iMzMwLTdlMmEyN2QyNjExNSIsImV4cCI6MTczMTUwODE0Nn0.5HCyVCE5Ige4aFDjywk7tpHz_j0pYSpE6mye9VXyujc',
	'2024-07-13 22:17:52.579',
	'2024-07-13 22:17:52.579',
	'system',
	'system');

insert into biblioteca.usuario (
	id,
	nome,
	email,
	data_cadastro,
	telefone,
	login,
	senha,
	situacao,
	reset_token,
	datacriacao,
	dataalteracao,
	usuariocriacao,
	usuarioalteracao)
values(
	'5bc26f63-fc13-4e4f-8fc3-524b223a7d34',
	'Ozzy Osbourne',
	'ozzy.osbourne@gmail.com',
	'1970-7-2 10:17:52.579',
	'4531236578',
	'ozzy',
	'$2a$12$gSOIuZSP3hDBZ5V0v.9k0O1wLMYyBGOG/uPAe3odJ8.ygto5Hxi0e',
	'ATIVO',
	'ee4ae880-a4db-4563-b330-7e2a27d26115',
	'2024-07-13 22:17:52.579',
	'2024-07-13 22:17:52.579',
	'system',
	'system');