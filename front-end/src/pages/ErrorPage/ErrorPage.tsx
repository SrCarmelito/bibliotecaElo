import { MehTwoTone } from "@ant-design/icons";
import { Button } from "antd";

const ErrorPage = () => {
  return (
    <div id="errorPage">
      <MehTwoTone type="danger" style={{ fontSize: "50px", color: "red" }} />
      <p>Ocorreu um erro inesperado.</p>
      <Button type="primary" href="/inicio">
        Voltar ao inicio
      </Button>
    </div>
  );
};

export default ErrorPage;
