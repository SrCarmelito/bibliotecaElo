import { Button, DatePicker, Form, Input, Modal } from "antd";
import React from "react";
import { Usuario } from "../../type/Usuario";
import { formItemLayout } from "../../styles/FormItemLayout";
import { useNotification } from "../../contexts/notificationContext";

import { saveOrUpdate } from "../../service/UsuarioService";
import { CheckCircleFilled } from "@ant-design/icons";
import { useNavigate } from "react-router-dom";

const CreateUser: React.FC = () => {
  const openNotification = useNotification();
  const [modal, contextHolder] = Modal.useModal();
  const navigate = useNavigate();

  const onSubmit = (usuario: Usuario) => {
    saveOrUpdate(usuario)
      .then((res) => {
        modal.success({
          title: "Usuário cadastrado com sucesso",
          content:
            "Entre em contato com o administrador do software para ativar sua conta!",
          icon: <CheckCircleFilled style={{ color: "green" }} />,
          onOk() {
            navigate("/sigin");
          },
        });
      })
      .catch((errors) => {
        const erros = errors.response?.data.errors;
        if (!erros) {
          throw erros;
        }
        erros.forEach((msg: string) => {
          openNotification("error", "Falha ao Cadastrar o Usuário", msg);
        });
      });
  };

  return (
    <Form<Usuario> {...formItemLayout} onFinish={onSubmit}>
      <Form.Item
        name="nome"
        label="Nome"
        rules={[{ required: true, message: "Informe o nome!" }]}
      >
        <Input placeholder="Informe o seu nome." />
      </Form.Item>
      <Form.Item
        name="email"
        label="E-mail"
        rules={[{ required: true, message: "Informe o e-mail!" }]}
      >
        <Input placeholder="Informe um e-mail válido." />
      </Form.Item>
      <Form.Item
        name="dataNascimento"
        label="Data de Nascimento"
        rules={[{ required: true, message: "Informe sua data de nascimento!" }]}
      >
        <DatePicker placeholder="Informe sua data de nascimento." />
      </Form.Item>
      <Form.Item
        name="telefone"
        label="Telefone"
        rules={[{ required: true, message: "Informe o telefone!" }]}
      >
        <Input placeholder="Informe seu telefone." />
      </Form.Item>
      <Form.Item
        name="login"
        label="Login"
        rules={[{ required: true, message: "Informe o login!" }]}
      >
        <Input placeholder="Informe seu login." />
      </Form.Item>
      <Form.Item
        name="senha"
        label="Senha"
        rules={[{ required: true, message: "Informe a senha!" }]}
      >
        <Input.Password placeholder="Informe a senha." />
      </Form.Item>
      <Form.Item
        name="senhaConfirmacao"
        label="Confirme a Senha"
        rules={[{ required: true, message: "Informe a senha de confirmação!" }]}
      >
        <Input.Password placeholder="Confirme sua senha" />
      </Form.Item>
      {contextHolder}
      <Form.Item label={null}>
        <Button type="primary" htmlType="submit">
          Confirmar
        </Button>
      </Form.Item>
    </Form>
  );
};

export default CreateUser;
