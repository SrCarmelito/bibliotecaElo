import {
  BookFilled,
  LogoutOutlined,
  UnorderedListOutlined,
} from "@ant-design/icons";
import type { MenuProps } from "antd";
import { Menu } from "antd";
import { useAuth } from "../contexts/LoginContext";

type MenuItem = Required<MenuProps>["items"][number];

export const MenuApp: React.FC = () => {
  const { signOut } = useAuth();

  const items: MenuItem[] = [
    {
      key: "g1",
      type: "group",
      children: [
        {
          key: "1",
          label: <a href="/livro/new">Cadastrar Livro</a>,
          icon: <BookFilled />,
        },
        {
          key: "2",
          label: <a href="/livros">Listar Livros</a>,
          icon: <UnorderedListOutlined />,
        },
        {
          key: "sub2",
          label: <a onClick={signOut}>Sair do software</a>,
          icon: <LogoutOutlined />,
        },
      ],
    },
  ];

  return (
    <div id="divdomenu">
      <Menu
        style={{
          width: 69,
          position: "fixed",
          zIndex: "1000",
          height: "100%",
          boxShadow: "0 0 10px rgba(0, 0, 0, 0.2)",
        }}
        defaultSelectedKeys={["1"]}
        defaultOpenKeys={["sub1"]}
        mode="inline"
        theme="light"
        inlineCollapsed
        items={items}
      />
    </div>
  );
};
