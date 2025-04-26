import axios from "axios";
import { Usuario } from "../type/Usuario";

const resource = "http://localhost:8080/api/usuarios";

export const saveOrUpdate = (usuario: Usuario) => {
  return usuario.id
    ? axios.put(resource, usuario)
    : axios.post(resource + "/novo-usuario", usuario);
};
