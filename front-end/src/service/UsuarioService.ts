import axios from "axios";
import { Usuario } from "../type/Usuario";

const resource = `${process.env.REACT_APP_URL_SERVER}/api/usuarios`;

export const saveOrUpdate = (usuario: Usuario) => {
  return usuario.id
    ? axios.put(resource, usuario)
    : axios.post(resource + "/novo-usuario", usuario);
};
