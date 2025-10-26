import {
  numericOrDateOperators,
  stringOperators,
} from "../../type/OperatorsType";

const analyzeSearch = (searchToAnalyze: string): string => {
  if (searchToAnalyze) {
    const searchParamsToAnalyze = searchToAnalyze.split(";");

    searchParamsToAnalyze.forEach((search) => {
      const searchOperator = search.split("__")[1];
      const stringOperatorFinded = stringOperators.find(
        (operator) => operator.value === searchOperator
      );

      const numericOrDateOperatorsFinded = numericOrDateOperators.find(
        (operator) => operator.value === searchOperator
      );

      // Remove parâmetros inválidos
      if (!(stringOperatorFinded || numericOrDateOperatorsFinded)) {
        const searchParamsToAnalyzeInvalidRemoved =
          searchParamsToAnalyze.filter((element) => element !== search);

        const url = new URL(window.location.href);
        url.searchParams.set(
          "search",
          searchParamsToAnalyzeInvalidRemoved.join(";")
        );

        window.location.href = url.toString();
      }
    });
  }

  return searchToAnalyze;
};

export const getSearchParam = (): string => {
  const searchParams = new URLSearchParams(window.location.search).get(
    "search"
  );

  if (searchParams) {
    return analyzeSearch(searchParams).replaceAll("__", "");
  }

  return "";
};
