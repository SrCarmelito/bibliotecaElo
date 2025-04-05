import { PagedResponse } from "../interfaces/PagedResponse";
import { Livro } from "./../type/Livro.d";
import axios, { AxiosPromise } from "axios";

const resource = "http://localhost:8080/api/livros";

export const findById = (livroId: string): AxiosPromise<Livro> => {
  return axios.get(`${resource}/${livroId}`);
};

export const deleteById = (livroId?: string): AxiosPromise<void> => {
  return axios.delete(`${resource}/${livroId}`);
};

export const findAll = (
  pagination?: any,
  sortField?: any,
  sortOrder?: any,
  search?: string
): AxiosPromise<PagedResponse<Livro>> => {
  const paginationConverter: any = {
    page: pagination.current - 1,
    size: pagination.pageSize,
  };

  let sortConverter: any = {};
  if (sortField) {
    sortConverter = {
      sort: `${sortField},${
        sortOrder?.substring(0, 1) === "a" ? "asc" : "desc"
      }`,
    };
  } else {
    sortConverter = { sort: "titulo,asc" };
  }

  let searchConverter: any = {};
  search ? (searchConverter = search) : (searchConverter = "");

  return axios.get(`${resource}/find`, {
    params: {
      ...sortConverter,
      ...paginationConverter,
      search: searchConverter,
    },
  });
};

export const saveOrUpdate = (livro: Livro) => {
  return livro.id ? axios.put(resource, livro) : axios.post(resource, livro);
};
