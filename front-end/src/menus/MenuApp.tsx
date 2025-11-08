import "./MenuApp.css";
import {
  BookTwoTone,
  DollarTwoTone,
  HomeOutlined,
  LogoutOutlined,
  OrderedListOutlined,
  UserOutlined,
} from "@ant-design/icons";
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
          label: <a href="/inicio">Recomendações</a>,
          icon: <HomeOutlined />,
        },
        {
          key: "2",
          label: <a href="/livros">Livros</a>,
          icon: <BookTwoTone />,
        },
        {
          key: "3",
          label: <a href="/categorias">Categorias</a>,
          icon: <OrderedListOutlined />,
        },
        {
          key: "4",
          label: <a href="/emprestimos">Empréstimos</a>,
          icon: <DollarTwoTone />,
        },
        {
          key: "5",
          label: <a href="/minhaconta">Minha conta</a>,
          icon: <UserOutlined />,
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
        defaultOpenKeys={["sub1"]}
        mode="inline"
        theme="light"
        inlineCollapsed
        items={items}
      />
    </div>
  );
};
