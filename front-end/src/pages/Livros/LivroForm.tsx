import { Button, DatePicker, Form, Modal, Input, Select } from "antd";

import { findAll } from "../../service/CategoriaService";
import { saveOrUpdate, findById } from "../../service/LivroService";
import { Categoria } from "../../type/Categoria";
import React, { useEffect, useMemo, useState } from "react";
import { Livro } from "../../type/Livro";
import { useNotification } from "../../contexts/notificationContext";
import { useNavigate, useParams } from "react-router";
import { ExclamationCircleFilled, RollbackOutlined } from "@ant-design/icons";
import Title from "antd/lib/typography/Title";
import { handleApiError } from "../../functions/handleApiError";

type Params = {
  id: string;
};

const initialLivro: Livro = {
  id: "",
  titulo: "",
  autor: "",
  isbn: "",
  dataPublicacao: "",
  categoria: undefined,
};

const formItemLabelLayout = {
  labelCol: {
    xs: { span: 3 },
    sm: { span: 6 },
  },
  wrapperCol: {
    xs: { span: 2 },
    sm: { span: 18 },
  },
};

const LivroForm: React.FC = () => {
  const [categorias, setCategorias] = useState<Categoria[]>([]);
  const openNotification = useNotification();
  const navigate = useNavigate();
  const { id } = useParams<Params>();
  const [form] = Form.useForm();
  const [modal, contextHolder] = Modal.useModal();

  const findCategorias = (search?: string) => {
    search ? (search = `descricao=ilike=${search}`) : (search = "");
    findAll(0, "", "", search).then((response) => {
      setCategorias(response.data.content);
    });
  };

  const isEditing = useMemo(() => {
    return id && id !== "new";
  }, [id]);

  const fetchData = () => {
    if (isEditing && id) {
      findById(id).then(({ data }) => {
        form.setFieldsValue(data || initialLivro);
        form.setFieldValue("categoria", data.categoria?.id);
      });
    }
  };

  useEffect(fetchData, [id, form, isEditing]);
  useEffect(findCategorias, [setCategorias]);

  const onSubmit = (livro: Livro) => {
    const categoria: any = { id: livro?.categoria };
    livro.categoria = categoria;

    saveOrUpdate(livro)
      .then((res) => {
        const msg =
          res.config.method === "put"
            ? "Livro atualizado com sucesso."
            : "Livro cadastrado com sucesso.";
        openNotification("success", msg);
        navigate(`/livros`);
      })
      .catch((errors) =>
        handleApiError(
          openNotification,
          errors,
          "Falha ao atualizar ou cadastrar o livro."
        )
      );
  };

  const onCancel = () => {
    modal.confirm({
      title: "Deseja retornar à página anterior?",
      content: "Os dados informados serão perdidos!",
      icon: <ExclamationCircleFilled />,
      onOk() {
        navigate("/livros");
      },
    });
  };

  return (
    <>
      <Form<Livro>
        initialValues={form}
        form={form}
        onFinish={onSubmit}
        id="styledform"
        style={{ maxWidth: "30em" }}
        {...formItemLabelLayout}
      >
        <Title level={3}>
          {id === "new" ? "Cadastre um novo livro." : "Editando o livro."}
        </Title>
        <Form.Item name="id" noStyle />
        <Form.Item
          label="Título"
          name="titulo"
          rules={[{ required: true, message: "Informe o Título!" }]}
        >
          <Input placeholder="Digite o título" />
        </Form.Item>
        <Form.Item
          label="Autor"
          name="autor"
          rules={[{ required: true, message: "Informe o Autor!" }]}
        >
          <Input placeholder="Digite o autor" />
        </Form.Item>

        <Form.Item
          name="dataPublicacao"
          rules={[{ required: true, message: "Informe a data de publicação!" }]}
          label="Publicação"
        >
          <DatePicker style={{ display: "block" }} />
        </Form.Item>

        <Form.Item
          label="Isbn"
          name="isbn"
          rules={[
            {
              required: true,
              message: "ISBN deve conter no mínimo 1 e máximo 13 caracteres!",
              max: 13,
            },
          ]}
        >
          <Input type="number" placeholder="Digite o ISBN" />
        </Form.Item>

        <Form.Item
          label="Categoria"
          name="categoria"
          rules={[{ required: true, message: "Informe a Categoria!" }]}
        >
          <Select
            showSearch
            filterOption={(input, option) =>
              (option?.descricao ?? "")
                .toLowerCase()
                .includes(input.toLowerCase())
            }
            onSearch={findCategorias}
            options={categorias}
            fieldNames={{ label: "descricao", value: "id" }}
            placeholder="Selecione a categoria"
          />
        </Form.Item>
        <Form.Item label={null} noStyle>
          <Button block type="primary" htmlType="submit">
            Confirmar
          </Button>
        </Form.Item>
      </Form>
      {contextHolder}
      <Button
        onClick={onCancel}
        type="primary"
        size="large"
        shape="circle"
        icon={<RollbackOutlined />}
        style={{ position: "fixed", zIndex: 1, bottom: 40, right: 40 }}
      />
    </>
  );
};

export default LivroForm;
