import dayjs from "dayjs";

export type Usuario = {
  id?: string;
  nome?: string;
  email?: string;
  dataNascimento?: dayjs;
  telefone?: string;
  login?: string;
  situacao?: string;
};
