import { Button, DatePicker, Form, Modal, Input, Select } from "antd";

import { findAll } from "../../service/CategoriaService";
import { saveOrUpdate, findById } from "../../service/LivroService";
import { Categoria } from "../../type/Categoria";
import React, { useEffect, useMemo, useState } from "react";
import { Livro } from "../../type/Livro";
import { useNotification } from "../../contexts/notificationContext";
import { useNavigate, useParams } from "react-router";
import dayjs from "dayjs";
import { ExclamationCircleFilled, RollbackOutlined } from "@ant-design/icons";
import Title from "antd/lib/typography/Title";

type Params = {
  id: string;
};

const initialLivro: Livro = {
  id: "",
  titulo: "",
  autor: "",
  isbn: "",
  dataPublicacao: dayjs,
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
  const [options, setOptions] = useState<Categoria[]>([]);
  const openNotification = useNotification();
  const navigate = useNavigate();
  const { id } = useParams<Params>();
  const [form] = Form.useForm();
  const [modal, contextHolder] = Modal.useModal();

  const findCategorias = (search?: string) => {
    search ? (search = `descricao=ilike=${search}`) : (search = "");
    findAll("", "", "", search).then((response) => {
      setOptions(response.data.content);
    });
  };

  const isEditing = useMemo(() => {
    return id && id !== "new";
  }, [id]);

  const fetchData = () => {
    if (isEditing && id) {
      findById(id).then(({ data }) => {
        data.dataPublicacao = dayjs(data.dataPublicacao);
        form.setFieldsValue(data || initialLivro);
        form.setFieldValue("categoria", data.categoria?.id);
      });
    }
  };

  useEffect(fetchData, [id, form, isEditing]);
  useEffect(findCategorias, [setOptions]);

  const onSubmit = (livro: Livro) => {
    const categoria: any = { id: livro?.categoria };
    livro.categoria = categoria;

    saveOrUpdate(livro)
      .then((res) => {
        const msg =
          res.config.method === "put"
            ? "Livro Atualizado com Sucesso!"
            : "Livro Cadastrado com Sucesso!";
        openNotification("success", msg);
        navigate(`/livros`);
      })
      .catch((errors) => {
        const erros = errors.response?.data.errors;
        if (!erros) {
          throw erros;
        }
        erros.forEach((msg: string) => {
          openNotification("error", "Falha ao Cadastrar o Livro", msg);
        });
      });
  };

  const onCancel = () => {
    modal.confirm({
      title: "Deseja Retornar à página anterior?",
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
          {id === "new" ? "Cadastre um novo livro" : "Editando o livro"}
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
          rules={[{ required: true, message: "Informe a Data de Publicação!" }]}
          label="Publicação"
        >
          <DatePicker style={{ display: "block" }} />
        </Form.Item>

        <Form.Item
          label="Isbn"
          name="isbn"
          rules={[{ required: true, message: "Informe o Isbn!" }]}
        >
          <Input type="number" placeholder="Digite o isbn" />
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
            options={options}
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
