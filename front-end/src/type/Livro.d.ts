import dayjs from "dayjs";
import { Categoria } from "./Categoria";
import { BucketFile } from "./BucketFile";

export type Livro = {
  id?: string;
  titulo?: string;
  autor?: string;
  isbn?: string;
  dataPublicacao: dayjs;
  categoria?: Categoria;
  bucketFile?: BucketFile;
};
