import axios, { AxiosPromise } from "axios";
import { PagedResponse } from "../interfaces/PagedResponse";
import { Emprestimo } from "../type/Emprestimo";
import { sortResolver } from "../functions/sortResolver";

const resource = "/emprestimos";

export const findAll = (
  pagination?: any,
  sortField?: string,
  sortOrder?: string,
  search?: string
): AxiosPromise<PagedResponse<Emprestimo>> => {
  return axios.get(`${resource}/find`, {
    params: {
      ...sortResolver(sortField, sortOrder, "livro.titulo", "asc"),
      ...{
        page: pagination.current - 1,
        size: pagination.pageSize,
      },
      search,
    },
  });
};

export const saveOrUpdate = (emprestimo: Emprestimo) => {
  return emprestimo.id
    ? axios.put(resource, emprestimo)
    : axios.post(resource, emprestimo);
};
