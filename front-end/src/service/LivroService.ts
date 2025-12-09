import { sortResolver } from "../functions/sortResolver";
import { PagedResponse } from "../interfaces/PagedResponse";
import { Livro } from "./../type/Livro.d";
import axios, { AxiosPromise } from "axios";

const resource = "/livros";

export const findById = (livroId?: string): AxiosPromise<Livro> => {
  return axios.get(`${resource}/${livroId}`);
};

export const deleteById = (livroId?: string): AxiosPromise<void> => {
  return axios.delete(`${resource}/${livroId}`);
};

export const findAll = (
  pagination?: any,
  sortField?: string,
  sortOrder?: string,
  search?: string
): AxiosPromise<PagedResponse<Livro>> => {
  return axios.get(`${resource}/find`, {
    params: {
      ...sortResolver(sortField, sortOrder, "titulo", "asc"),
      ...{
        page: pagination.current - 1 || 0,
        size: pagination.pageSize || 20,
      },
      search,
    },
  });
};

export const saveOrUpdate = (livro: Livro) => {
  return livro.id ? axios.put(resource, livro) : axios.post(resource, livro);
};
