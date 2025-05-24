import { PagedResponse } from "../interfaces/PagedResponse";
import { Categoria } from "../type/Categoria";
import axios, { AxiosPromise } from "axios";
import { sortResolver } from "./sortResolver";

const resource = "http://localhost:8080/api/categorias";

export const findById = (
  categoriaId: string
): AxiosPromise<Categoria | undefined> => {
  return axios.get(`${resource}/${categoriaId}`);
};

export const deleteById = (categoriaId?: string): AxiosPromise<void> => {
  return axios.delete(`${resource}/${categoriaId}`);
};

export const saveOrUpdate = (categoria: Categoria) => {
  return categoria.id
    ? axios.put(resource, categoria)
    : axios.post(resource, categoria);
};

export const findAll = (
  pagination?: any,
  sortField?: string,
  sortOrder?: string,
  search?: string
): AxiosPromise<PagedResponse<Categoria>> => {
  return axios.get(`${resource}/find`, {
    params: {
      ...sortResolver(sortField, sortOrder, "descricao", "asc"),
      ...{
        page: pagination.current - 1,
        size: pagination.pageSize,
      },
      search,
    },
  });
};
