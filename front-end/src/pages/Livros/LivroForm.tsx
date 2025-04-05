import {
  Button,
  DatePicker,
  Form,
  Modal,
  Input,
  InputNumber,
  Select,
} from "antd";

import { findAll } from "../../service/CategoriaService";
import { saveOrUpdate, findById } from "../../service/LivroService";
import { Categoria } from "../../type/Categoria";
import React, { useEffect, useMemo, useState } from "react";
import { Livro } from "../../type/Livro";
import { useNotification } from "../../contexts/notificationContext";
import { useNavigate, useParams } from "react-router";
import dayjs from "dayjs";
import { ExclamationCircleFilled, RollbackOutlined } from "@ant-design/icons";

const formItemLayout = {
  labelCol: {
    xs: { span: 2 },
    sm: { span: 6 },
  },
  wrapperCol: {
    xs: { span: 2 },
    sm: { span: 6 },
  },
};

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

const LivroForm: React.FC = () => {
  const [options, setOptions] = useState<Categoria[]>([]);
  const openNotification = useNotification();
  const navigate = useNavigate();
  const { id } = useParams<Params>();
  const [form] = Form.useForm();
  const [modal, contextHolder] = Modal.useModal();

  const findCategorias = (filter?: string) => {
    findAll(filter).then((response) => {
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
        {...formItemLayout}
        initialValues={form}
        form={form}
        onFinish={onSubmit}
        style={{ width: "95%", margin: "auto" }}
      >
        <Form.Item name="id" />
        <Form.Item
          label="Título"
          name="titulo"
          rules={[{ required: true, message: "Informe o Título!" }]}
        >
          <Input />
        </Form.Item>
        <Form.Item
          label="Autor"
          name="autor"
          rules={[{ required: true, message: "Informe o Autor!" }]}
        >
          <Input />
        </Form.Item>
        <Form.Item label="Publicação" style={{ marginBottom: 0 }}>
          <Form.Item
            name="dataPublicacao"
            style={{ display: "inline-block", width: "calc(32%)" }}
            rules={[
              { required: true, message: "Informe a Data de Publicação!" },
            ]}
          >
            <DatePicker />
          </Form.Item>
          <span
            style={{
              display: "inline-block",
              width: "5%",
              lineHeight: "32px",
              textAlign: "center",
            }}
          ></span>
          <Form.Item
            label="Isbn"
            name="isbn"
            style={{ display: "inline-block", width: "45%" }}
            rules={[{ required: true, message: "Informe o Isbn!" }]}
          >
            <InputNumber
              type="number"
              style={{ display: "inline-block", width: "155%" }}
            />
          </Form.Item>
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
          />
        </Form.Item>
        <Form.Item label={null}>
          <Button type="primary" htmlType="submit">
            Submit
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
