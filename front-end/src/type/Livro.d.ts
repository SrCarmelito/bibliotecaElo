import dayjs from "dayjs";
import { Categoria } from "./Categoria";

export type Livro = {
  id?: string;
  titulo?: string;
  autor?: string;
  isbn?: string;
  dataPublicacao: dayjs;
  categoria?: Categoria;
};
