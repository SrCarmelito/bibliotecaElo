import { useEffect, useState } from "react";
import { Livro } from "../../type/Livro";
import { Card, Col, Row } from "antd";
import { useLoading } from "../../components/searchcomponent/useLoading";
import { recomendacoes } from "../../service/RecomendacaoService";

const Recomendacoes = () => {
  const [livros, setLivros] = useState<Livro[]>([]);
  const [loading, setLoading] = useLoading();

  const fetchData = () => {
    setLoading(
      recomendacoes().then((response) => {
        setLivros(response.data.content);
      })
    );
  };

  useEffect(fetchData, []);

  return (
    <>
      <Row gutter={12}>
        {livros.map((livro) => (
          <Col span={6} key={livro.id}>
            <Card
              loading={loading}
              title={livro.titulo}
              variant="outlined"
              size="small"
              hoverable
            >
              <div
                style={{
                  display: "flex",
                  alignItems: "flex-start",
                  gap: "16px",
                }}
              >
                <div>
                  <p>
                    <b>Categoria: </b>
                    {livro.categoria?.descricao}
                  </p>
                  <p>
                    <b>Por: </b>
                    {livro.autor}
                  </p>
                  <p>
                    <b>Publicado em: </b>
                    {livro.dataPublicacao.format("DD/MM/YYYY")}
                  </p>
                  <p>
                    <b>Isbn: </b>
                    {livro.isbn}
                  </p>
                </div>
              </div>
            </Card>
            <p />
          </Col>
        ))}
      </Row>
    </>
  );
};

export default Recomendacoes;
