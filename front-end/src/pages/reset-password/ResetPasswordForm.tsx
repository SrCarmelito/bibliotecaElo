import { Button, Form, Input, Modal } from "antd";
import Title from "antd/lib/typography/Title";
import { confirmResetPassword } from "../../service/AuthService";
import { CheckCircleFilled } from "@ant-design/icons";
import { useNavigate } from "react-router-dom";
import { useNotification } from "../../contexts/notificationContext";
import { handleApiError } from "../../functions/handleApiError";

const initialValues = {
  senha: "",
  senhaConfirmacao: "",
  token: new URLSearchParams(window.location.search).get("token"),
};

const ResetPasswordForm = () => {
  const [modal, contextHolder] = Modal.useModal();
  const openNotification = useNotification();
  const navigate = useNavigate();

  const onSubmit = (resetPassword: string[]) => {
    confirmResetPassword(resetPassword)
      .then(() => {
        modal.success({
          title: "Senha alterada com sucesso",
          content: "",
          icon: <CheckCircleFilled />,
          onOk() {
            navigate("/sigin");
          },
        });
      })
      .catch((errors) =>
        handleApiError(openNotification, errors, "Falha ao trocar a senha.")
      );
  };

  return (
    <Form id="styledform" onFinish={onSubmit} initialValues={initialValues}>
      <Title level={3}>Troca de senha</Title>
      <Form.Item name="token" noStyle />
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
      <Form.Item label={null}>
        <Button type="primary" htmlType="submit" block>
          Confirmar
        </Button>
      </Form.Item>
      {contextHolder}
    </Form>
  );
};

export default ResetPasswordForm;
