import { PagedResponse } from "./../interfaces/PagedResponse.d";
import axios, { AxiosPromise } from "axios";
import { Livro } from "../type/Livro";

const resource = `${process.env.REACT_APP_URL_SERVER}/api/recomendacoes`;

export const recomendacoes = (): AxiosPromise<PagedResponse<Livro>> => {
  return axios.get(resource, {
    params: {
      ...{
        page: 0,
        size: 100,
      },
    },
  });
};
