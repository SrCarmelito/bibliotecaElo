import { sortResolver } from "../functions/sortResolver";
import { PagedResponse } from "../interfaces/PagedResponse";
import { Livro } from "./../type/Livro.d";
import axios, { AxiosPromise } from "axios";
import { BucketService } from "./BucketService";

export class LivroService extends BucketService<Livro> {

  constructor() {
    super("/livros");
  }

  findById = (livroId?: string): AxiosPromise<Livro> => {
    return axios.get(`${this.resource}/${livroId}`);
  };

  deleteById = (livroId?: string): AxiosPromise<void> => {
    return axios.delete(`${this.resource}/${livroId}`);
  };

  findAll = (
    pagination?: any,
    sortField?: string,
    sortOrder?: string,
    search?: string
  ): AxiosPromise<PagedResponse<Livro>> => {
    return axios.get(`${this.resource}/find`, {
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

  saveOrUpdate = (livro: Livro) => {
    return livro.id ? axios.put(this.resource, livro) : axios.post(this.resource, livro);
  };

}

export const livroService = new LivroService();