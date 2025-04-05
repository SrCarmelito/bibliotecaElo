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

export const findAll = (
  filter?: string
): AxiosPromise<PagedResponse<Categoria>> => {
  const paginationConverter = {
    page: 0,
    size: 20,
  };

  const sortConverter = { sort: "descricao,asc" };

  let searchValue: any = "";
  filter
    ? (searchValue = { search: `descricao=ilike=${filter}` })
    : (searchValue = { search: "" });

  return axios.get(`${resource}/find`, {
    params: {
      ...sortConverter,
      ...paginationConverter,
      ...searchValue,
    },
  });
};
