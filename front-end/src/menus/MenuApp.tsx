import "./MenuApp.css";
import { BookFilled, LogoutOutlined } from "@ant-design/icons";
import type { MenuProps } from "antd";
import { Menu } from "antd";
import { useAuth } from "../contexts/authContext";

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
          label: <a href="/livros">Livros</a>,
          icon: <BookFilled />,
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
        id="menu"
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
