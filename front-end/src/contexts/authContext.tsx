import axios from "axios";
import {
  createContext,
  useContext,
  useLayoutEffect,
  useReducer,
  useState,
} from "react";
import { me, verifyToken } from "../service/AuthService";
import { Modal } from "antd";
import { ExclamationCircleFilled } from "@ant-design/icons";
import { Usuario } from "../type/Usuario";

type Prop = { children: any };

type AuthContextType = {
  token: string | undefined;
  usuario: Usuario | undefined;
  signIn: (token: string) => void;
  signOut: () => void;
};

export const useAuth = () => useContext(AuthContext);

const initialUsuario: Usuario = {
  id: "",
  nome: "",
  email: "",
  dataNascimento: "",
  telefone: "",
  login: "",
  situacao: "",
};

export const AuthContext = createContext<AuthContextType>({
  token: undefined,
  usuario: initialUsuario,
  signIn: () => { },
  signOut: () => { },
});

type Action = {
  type: "SET_TOKEN" | "SET_USUARIO";
  payload: any;
};

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
  const [usuario, setUsuario] = useState<Usuario>(() => {
    const usuario = localStorage.getItem("usuario");
    return usuario ? JSON.parse(usuario) : initialUsuario;
  });
  const [modal, contextHolder] = Modal.useModal();

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
    me().then((response) => {
      localStorage.setItem("usuario", JSON.stringify(response.data));
      setUsuario(response.data);
    });
  };

  const signOut = () => {
    modal.confirm({
      title: "Deseja sair do sistema?",
      icon: <ExclamationCircleFilled />,
      onOk() {
        localStorage.removeItem("token");
        localStorage.removeItem("usuario");
        dispatch({ type: "SET_TOKEN", payload: null });
        dispatch({ type: "SET_USUARIO", payload: null });
      },
    });
  };

  return (
    <AuthContext.Provider value={{ token, usuario, signIn, signOut }}>
      {children}
      {contextHolder}
    </AuthContext.Provider>
  );
};
