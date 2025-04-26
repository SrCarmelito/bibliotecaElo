import axios from "axios";
import { createContext, useContext, useLayoutEffect, useReducer } from "react";
import { verifyToken } from "../service/AuthService";
import { Modal } from "antd";
import { ExclamationCircleFilled } from "@ant-design/icons";

type Prop = { children: any };

type AuthContextType = {
  user: string | undefined;
  signIn: (token: string) => void;
  signOut: () => void;
};

export const useAuth = () => useContext(AuthContext);

export const AuthContext = createContext<AuthContextType>({
  user: undefined,
  signIn: () => {},
  signOut: () => {},
});

type Action = { type: "SET_USER"; payload: string | null };

const reducer = (state: string, action: Action): string => {
  switch (action.type) {
    case "SET_USER":
      return action.payload || "";
    default:
      return state;
  }
};

const init = (): string => {
  return localStorage.getItem("token") || "";
};

export const LoginProvider: React.FC<Prop> = ({ children }) => {
  const [user, dispatch] = useReducer(reducer, "", init);
  const [modal, contextHolder] = Modal.useModal();

  useLayoutEffect(() => {
    const token = localStorage.getItem("token");

    if (token) {
      axios.defaults.headers.common["Authorization"] = `Bearer ${token}`;
      verifyToken()
        .then(() => {
          dispatch({ type: "SET_USER", payload: token });
        })
        .catch(() => {
          localStorage.removeItem("token");
          axios.defaults.headers.common["Authorization"] = null;
          dispatch({ type: "SET_USER", payload: null });
        });
    }
  }, []);

  const signIn = (token: string) => {
    localStorage.setItem("token", token);
    axios.defaults.headers.common[
      "Authorization"
    ] = `Bearer ${localStorage.getItem("token")}`;

    dispatch({ type: "SET_USER", payload: token });
  };

  const signOut = () => {
    modal.confirm({
      title: "Deseja sair do sistema?",
      icon: <ExclamationCircleFilled />,
      onOk() {
        localStorage.removeItem("token");
        dispatch({ type: "SET_USER", payload: null });
      },
    });
  };

  return (
    <AuthContext.Provider value={{ user, signIn, signOut }}>
      {children}
      {contextHolder}
    </AuthContext.Provider>
  );
};
