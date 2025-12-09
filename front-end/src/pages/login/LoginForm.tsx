import { Button, Form, Input, Modal, Spin } from "antd";
import { LoginDTO } from "../../type/LoginDTO";

import { login, resetPassword } from "../../service/AuthService";
import { useNotification } from "../../contexts/notificationContext";
import { useNavigate } from "react-router-dom";
import { useAuth } from "../../contexts/authContext";
import Title from "antd/lib/typography/Title";
import {
  CheckCircleFilled,
  LockOutlined,
  UserOutlined,
} from "@ant-design/icons";
import React, { useState } from "react";
import { Email } from "../../type/Email";
import { useLoading } from "../../consts/useLoading";
import { handleApiError } from "../../functions/handleApiError";

const LoginForm: React.FC = () => {
  const [open, setOpen] = useState(false);
  const [modal, contextHolder] = Modal.useModal();
  const [loading, setLoading] = useLoading();
  const [form] = Form.useForm();
  const openNotification = useNotification();
  const navigate = useNavigate();
  const { signIn, token } = useAuth();

  if (token) {
    navigate("/inicio");
  }

  const onSubmit = (loginDTO: LoginDTO) => {
    setLoading(
      login(loginDTO)
        .then((data) => {
          signIn(data.data);
          navigate("/inicio");
        })
        .catch((errors) =>
          handleApiError(openNotification, errors, "Falha ao logar no sistema.")
        )
    );
  };

  const changePassword = (e: Email) => {
    setLoading(
      resetPassword(e)
        .then((res) => {
          modal.success({
            title: "Alteração de senha solicitada com sucesso.",
            content: "Verifique seu e-mail e siga as instruções.",
            icon: <CheckCircleFilled />,
          });
          setOpen(!open);
        })
        .catch((errors) =>
          handleApiError(
            openNotification,
            errors,
            "Falha ao iniciar a troca de senha."
          )
        )
    );
    form.setFieldValue("email", "");
  };

  return (
    <>
      <Form<LoginDTO> onFinish={onSubmit} id="styledform">
        <Spin spinning={loading} fullscreen />
        <Title level={3}>Entrar</Title>
        <Form.Item
          name="login"
          rules={[{ required: true, message: "Informe o login!" }]}
        >
          <Input
            autoComplete={"user"}
            prefix={<UserOutlined />}
            placeholder="Informe o seu login."
          />
        </Form.Item>
        <Form.Item
          name="senha"
          rules={[{ required: true, message: "Informe a Senha!" }]}
        >
          <Input.Password
            autoComplete={"password"}
            prefix={<LockOutlined />}
            placeholder="Informe a senha."
          />
        </Form.Item>

        <Form.Item>
          <Button block type="primary" htmlType="submit">
            Login
          </Button>
        </Form.Item>
        <Form.Item>
          <div style={{ display: "flex", justifyContent: "space-between" }}>
            <a href="/signup">Crie sua conta</a>
            <a type="link" onClick={() => setOpen(!open)}>
              Esqueceu sua senha?
            </a>
          </div>
        </Form.Item>
      </Form>
      <Modal
        open={open}
        footer
        title="Troca de senha"
        onCancel={() => setOpen(!open)}
      >
        <Spin spinning={loading} fullscreen />
        <p>
          Insira seu e-mail no campo abaixo e clique em Altear senha. Será
          encaminhado um e-mail com as instruções para confirmação da troca de
          sua senha.
        </p>
        <Form<Email> form={form} onFinish={changePassword} id="styledform">
          <Form.Item
            name="email"
            rules={[{ required: true, message: "Informe o e-mail." }]}
          >
            <Input type="mail" placeholder="Informe seu e-mail" />
          </Form.Item>
          {contextHolder}
          <Form.Item>
            <Button htmlType="submit" block type="primary">
              Altear senha
            </Button>
          </Form.Item>
        </Form>
      </Modal>
    </>
  );
};

export default LoginForm;
