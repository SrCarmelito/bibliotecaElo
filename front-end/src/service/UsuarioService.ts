import axios from "axios";
import { Usuario } from "../type/Usuario";

const resource = "/usuarios";

export const saveOrUpdate = (usuario: Usuario) => {
  return usuario.id
    ? axios.put(resource, usuario)
    : axios.post(resource + "/novo-usuario", usuario);
};
