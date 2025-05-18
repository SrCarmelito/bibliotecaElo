import { PagedResponse } from "../interfaces/PagedResponse";
import { Categoria } from "../type/Categoria";
import axios, { AxiosPromise } from "axios";

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
  sortField?: any,
  sortOrder?: any,
  search?: string
): AxiosPromise<PagedResponse<Categoria>> => {
  const paginationConverter: any = {
    page: pagination?.current - 1 || 0,
    size: pagination?.pageSize || 20,
  };

  let sortConverter: any = {};
  if (sortField) {
    sortConverter = {
      sort: `${sortField},${
        sortOrder?.substring(0, 1) === "a" ? "asc" : "desc"
      }`,
    };
  } else {
    sortConverter = { sort: "descricao,asc" };
  }

  return axios.get(`${resource}/find`, {
    params: {
      ...sortConverter,
      ...paginationConverter,
      search,
    },
  });
};
