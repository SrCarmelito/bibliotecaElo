import axios from "axios";
import {
  createContext,
  useContext,
  useLayoutEffect,
  useReducer,
  useState,
} from "react";
import { verifyToken } from "../service/AuthService";
import { Modal } from "antd";
import { ExclamationCircleFilled } from "@ant-design/icons";
import { Usuario } from "../type/Usuario";

type Prop = { children: any };

type AuthContextType = {
  token: string | undefined;
  signIn: (token: string) => void;
  signOut: () => void;
};

export const useAuth = () => useContext(AuthContext);

export const AuthContext = createContext<AuthContextType>({
  token: undefined,
  signIn: () => {},
  signOut: () => {},
});

type Action = { type: "SET_TOKEN"; payload: string | null };

const reducer = (state: string, action: Action): string => {
  switch (action.type) {
    case "SET_TOKEN":
      return action.payload || "";
    default:
      return state;
  }
};

const init = (): string => {
  return localStorage.getItem("token") || "";
};

export const AuthProvider: React.FC<Prop> = ({ children }) => {
  const [token, dispatch] = useReducer(reducer, "", init);
  const [modal, contextHolder] = Modal.useModal();
  const [user, setUser] = useState<Usuario>();

  useLayoutEffect(() => {
    const token = localStorage.getItem("token");

    if (token) {
      axios.defaults.headers.common["Authorization"] = `Bearer ${token}`;
      verifyToken()
        .then(() => {
          dispatch({ type: "SET_TOKEN", payload: token });
        })
        .catch(() => {
          localStorage.removeItem("token");
          axios.defaults.headers.common["Authorization"] = null;
          dispatch({ type: "SET_TOKEN", payload: null });
        });
    }
  }, []);

  const signIn = (token: string) => {
    localStorage.setItem("token", token);
    axios.defaults.headers.common[
      "Authorization"
    ] = `Bearer ${localStorage.getItem("token")}`;

    dispatch({ type: "SET_TOKEN", payload: token });
  };

  const signOut = () => {
    modal.confirm({
      title: "Deseja sair do sistema?",
      icon: <ExclamationCircleFilled />,
      onOk() {
        localStorage.removeItem("token");
        dispatch({ type: "SET_TOKEN", payload: null });
      },
    });
  };

  return (
    <AuthContext.Provider value={{ token, signIn, signOut }}>
      {children}
      {contextHolder}
    </AuthContext.Provider>
  );
};
