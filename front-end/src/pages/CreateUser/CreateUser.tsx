import { Button, DatePicker, Form, Input, Modal } from "antd";
import React from "react";
import { Usuario } from "../../type/Usuario";
import { useNotification } from "../../contexts/notificationContext";

import { saveOrUpdate } from "../../service/UsuarioService";
import { CheckCircleFilled } from "@ant-design/icons";
import { useNavigate } from "react-router-dom";
import Title from "antd/lib/typography/Title";

const CreateUser: React.FC = () => {
  const openNotification = useNotification();
  const [modal, contextHolder] = Modal.useModal();
  const navigate = useNavigate();

  const onSubmit = (usuario: Usuario) => {
    saveOrUpdate(usuario)
      .then(() => {
        modal.success({
          title: "Usuário cadastrado com sucesso.",
          content:
            "Entre em contato com o administrador do software para ativar sua conta.",
          icon: <CheckCircleFilled />,
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
          openNotification("error", "Falha ao cadastrar o usuário.", msg);
        });
      });
  };

  return (
    <Form<Usuario> onFinish={onSubmit} id="styledform">
      <Title level={3}>Crie sua conta</Title>
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
          Cadastrar
        </Button>
      </Form.Item>
      <a href="/">« Voltar</a>
    </Form>
  );
};

export default CreateUser;
