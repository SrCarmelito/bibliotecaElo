import "./MenuApp.css";
import {
  AppstoreTwoTone,
  BookTwoTone,
  DollarTwoTone,
  HomeTwoTone,
  LogoutOutlined,
  ProfileTwoTone,
  UserOutlined,
} from "@ant-design/icons";
import type { MenuProps } from "antd";
import { Breadcrumb, Menu } from "antd";
import { useAuth } from "../contexts/authContext";
import { ItemType } from "antd/es/breadcrumb/Breadcrumb";
import {
  MenuDividerType,
  MenuItemGroupType,
  MenuItemType,
  SubMenuType,
} from "antd/es/menu/interface";
import { Link } from "react-router-dom";

type MenuItem = Required<MenuProps>["items"][number];

export const MenuApp: React.FC = () => {
  const { signOut } = useAuth();

  const items: MenuItem[] = [
    {
      key: "g1",
      type: "group",
      children: [
        {
          key: "inicio",
          label: (
            <Link to="/inicio">
              <AppstoreTwoTone /> Recomendações
            </Link>
          ),
          icon: <AppstoreTwoTone />,
        },
        {
          key: "livros",
          label: (
            <Link to="/livros">
              <BookTwoTone /> Livros
            </Link>
          ),
          icon: <BookTwoTone />,
        },
        {
          key: "categorias",
          label: (
            <Link to="/categorias">
              <ProfileTwoTone /> Categorias
            </Link>
          ),
          icon: <ProfileTwoTone />,
        },
        {
          key: "emprestimos",
          label: (
            <Link to="/emprestimos">
              <DollarTwoTone /> Empréstimos
            </Link>
          ),
          icon: <DollarTwoTone />,
        },
        {
          key: "minhaconta",
          label: (
            <Link to="/minhaconta">
              <UserOutlined /> Minha conta
            </Link>
          ),
          icon: <UserOutlined />,
        },
        {
          key: "Sair do software",
          label: <a onClick={signOut}>Sair do software</a>,
          icon: <LogoutOutlined />,
        },
      ],
    },
  ];

  const initialBreadcrumb: ItemType[] = [
    {
      key: "/inicio",
      href: "/inicio",
      title: (
        <>
          <HomeTwoTone /> Inicio
        </>
      ),
    },
  ];

  const getBreadCrumb = (): ItemType[] => {
    const pathname = window.location.pathname.split("/").slice(1);

    let breadCrumbs: ItemType[] = initialBreadcrumb;

    const group = items[0];

    function isMenuItemType(
      item:
        | MenuDividerType
        | MenuItemType
        | SubMenuType<MenuItemType>
        | MenuItemGroupType<MenuItemType>
    ): item is MenuItemType {
      return "label" in item && "key" in item;
    }

    if (group && group.type === "group") {
      group.children!.forEach((child) => {
        if (child && child?.key && "key" in child) {
          if (
            pathname.includes(child?.key.toString()) &&
            isMenuItemType(child)
          ) {
            const labelElement = child.label as React.ReactElement;

            breadCrumbs = [
              ...breadCrumbs,
              {
                key: child.key,
                href: labelElement.props.href,
                title: child.label,
              },
            ];
          }
        }
      });
    }

    return breadCrumbs;
  };

  return (
    <div>
      <Breadcrumb className="breadcrumb" items={getBreadCrumb()} />
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
    </div>
  );
};
