import { Button, DatePicker, Form, Input, Modal } from "antd";
import React, { useEffect } from "react";
import { Usuario } from "../../type/Usuario";
import { useNotification } from "../../contexts/notificationContext";

import { saveOrUpdate } from "../../service/UsuarioService";
import { CheckCircleFilled } from "@ant-design/icons";
import { useNavigate } from "react-router-dom";
import Title from "antd/lib/typography/Title";
import { me } from "../../service/AuthService";
import { useAuth } from "../../contexts/authContext";
import dayjs from "dayjs";

const CreateUser: React.FC = () => {
  const openNotification = useNotification();
  const [modal, contextHolder] = Modal.useModal();
  const [form] = Form.useForm();
  const navigate = useNavigate();
  const { token } = useAuth();

  const fetchData = () => {
    if (token) {
      me().then((response) => {
        response.data.dataNascimento = dayjs(response.data.dataNascimento);
        form.setFieldsValue(response.data);
      });
    }
  };

  useEffect(fetchData, [token]);

  const onSubmit = (usuario: Usuario) => {
    saveOrUpdate(usuario)
      .then(() => {
        modal.success({
          title: token
            ? "Usuário atualizado com sucesso."
            : "Usuário atualizado com sucesso.",
          content: token
            ? ""
            : "Entre em contato com o administrador do software para ativar sua conta.",
          icon: <CheckCircleFilled />,
          onOk() {
            if (!token) {
              navigate("/sigin");
            }
          },
        });
      })
      .catch((errors) => {
        const erros = errors.response?.data.errors;
        if (!erros) {
          throw erros;
        }
        erros.forEach((msg: string) => {
          openNotification(
            "error",
            token
              ? "Falha ao atualizar o usuário."
              : "Falha ao cadastrar o usuário.",
            msg
          );
        });
      });
  };

  return (
    <Form<Usuario> form={form} onFinish={onSubmit} id="styledform">
      <Title level={3}>{token ? "Sua conta" : "Crie sua conta"}</Title>
      <Form.Item name="id" noStyle />
      <Form.Item name="situacao" noStyle />
      <Form.Item
        name="nome"
        rules={[{ required: true, message: "Informe o nome." }]}
      >
        <Input placeholder="Informe o seu nome" />
      </Form.Item>
      <Form.Item
        name="email"
        rules={[{ required: true, message: "Informe o e-mail." }]}
      >
        <Input type="email" placeholder="E-mail" />
      </Form.Item>
      <Form.Item
        name="dataNascimento"
        rules={[{ required: true, message: "Informe sua data de nascimento." }]}
      >
        <DatePicker
          placeholder="Data de nascimento"
          style={{ display: "block" }}
        />
      </Form.Item>
      <Form.Item
        name="telefone"
        rules={[{ required: true, message: "Informe o telefone." }]}
      >
        <Input type="number" placeholder="Telefone" />
      </Form.Item>
      <Form.Item
        name="login"
        rules={[{ required: true, message: "Informe o login." }]}
      >
        <Input placeholder="Login." />
      </Form.Item>
      <Form.Item
        name="senha"
        rules={[{ required: true, message: "Informe a senha." }]}
      >
        <Input.Password placeholder="Senha." />
      </Form.Item>
      <Form.Item
        name="senhaConfirmacao"
        rules={[{ required: true, message: "Informe a senha de confirmação." }]}
      >
        <Input.Password placeholder="Confirme sua senha" />
      </Form.Item>
      {contextHolder}
      <Form.Item label={null}>
        <Button type="primary" htmlType="submit" block>
          {token ? "Atualizar" : "Cadastrar"}
        </Button>
      </Form.Item>
      <a href="/">« Voltar</a>
    </Form>
  );
};

export default CreateUser;
