import { TableParams } from "../interfaces/ItableParams";

export const getRandomUserParams = (params: TableParams) => ({
  results: params.pagination?.pageSize,
  page: params.pagination?.current,
  ...params,
});
