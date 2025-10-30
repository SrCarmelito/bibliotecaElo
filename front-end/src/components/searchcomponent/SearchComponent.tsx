import "./SearchComponent.css";
import { Button, DatePicker, Form, Input, InputNumber, Select } from "antd";
import { useEffect, useState } from "react";
import {
  ButtonFilters,
  Fields,
  Props,
  SearchField,
} from "../../type/SearchTypes";
import { BaseOptionType } from "antd/es/select";
import { CloseCircleOutlined, SearchOutlined } from "@ant-design/icons";
import { useSearchParams } from "react-router-dom";
import {
  numericOrDateOperators,
  stringOperators,
} from "../../type/OperatorsType";

const SearchComponent = ({ optionsFilters, runSearch }: Props) => {
  const [form] = Form.useForm();
  const options = optionsFilters;
  const [operators, setOperators] = useState<BaseOptionType[]>([]);
  const [componentSelector, setComponentSelector] = useState<SearchField>(
    optionsFilters[0]
  );
  const [filters, setFilters] = useState<ButtonFilters[]>([]);
  const [searchParams, setSearchParams] = useSearchParams();
  const [listSearch, setListSearch] = useState<string[]>([]);

  // constrói os botões de filtro para o usuário manipular quando a página é criada
  useEffect(() => {
    buildFiltersFromUrl();
  }, []);

  // carregar os operadores sem ter que clicar em um option
  useEffect(() => {
    changeOperators(options[0]);
  }, [options]);

  // função para criar os botões de filtro quando a página é carregada pela 1 vez e setar a listSearch inicial para
  // posterior manipulação quando for adicionado ou removido um novo filtro
  const buildFiltersFromUrl = () => {
    const searchParam = searchParams.get("search");

    if (searchParam) {
      let arraySearch: string[] = searchParam?.split(";");

      let filtersFromUrl: ButtonFilters[] = [];
      arraySearch.map((search) => {
        const label = optionsFilters.filter(
          (option) => option.value === search.split("__")[0]
        );

        const operator =
          label[0].type === "STRING"
            ? stringOperators
            : numericOrDateOperators?.filter(
                (o) => o.value === search.split("__")[1]
              );

        filtersFromUrl.push({
          id: search,
          label:
            label[0].label +
            " " +
            operator[0].label +
            " " +
            search.replace(/"/g, "").split("__")[2],
        });
      });

      // cria os botões conforme filtros da url
      setFilters(filtersFromUrl);

      // seta o listsearch com o search da url
      setListSearch([...listSearch, searchParam]);
    }
  };

  const changeOperators = (option: SearchField) => {
    form.setFieldValue("search", "");
    setComponentSelector(option);
    setOperators(
      option.type === "STRING" ? stringOperators : numericOrDateOperators
    );
  };

  const executeSearch = (values: Fields) => {
    let searchConverter;

    if (values.search) {
      switch (componentSelector.type) {
        case "DATE":
          searchConverter = values.search.format("YYYY-MM-DD");
          break;
        case "STRING":
          searchConverter = `"${String(values.search)
            .replace(/"/g, '\\"')
            .replace(/\\/g, "\\\\")
            .replace(/"/g, '\\"')}"`;
          break;
        default:
          searchConverter = values.search;
      }
    }

    const newSearch: string = `${values.field}__${values.operator}__${searchConverter}`;

    if (listSearch.includes(newSearch)) {
      return;
    }

    if (listSearch[0]?.split(";").length >= 1) {
      setListSearch([listSearch.join("").concat(";", newSearch)]);
      setSearchParams({ search: [listSearch.join("").concat(";", newSearch)] });
      runSearch(listSearch.join("").concat(";", newSearch));
    } else {
      setListSearch([listSearch.join("").concat(newSearch)]);
      setSearchParams({ search: [listSearch.join("").concat(newSearch)] });
      runSearch(listSearch.join("").concat(newSearch).replaceAll("__", ""));
    }

    form.setFieldValue("search", "");

    const fieldLabel = options.find(
      (item) => item.value === values.field
    )?.label;

    const operatorLabel = operators.find(
      (item) => item.value === values.operator
    )?.label;

    if (fieldLabel && operatorLabel) {
      const label = `${fieldLabel} ${operatorLabel} ${searchConverter}`;
      addFilter({ id: newSearch, label });
    }
  };

  const addFilter = (buttonFilter: ButtonFilters) => {
    setFilters([
      ...filters,
      { id: buttonFilter.id, label: buttonFilter.label.replace(/"/g, "") },
    ]);
  };

  const removeFilter = (buttonFilter?: ButtonFilters) => {
    if (buttonFilter?.id) {
      setFilters(filters.filter((filter) => filter.id !== buttonFilter.id));

      if (listSearch[0].split(";").length - 1 >= 1) {
        const newSearch = listSearch[0]
          .split(";")
          .filter((item) => item !== buttonFilter.id)
          .join(";");

        setListSearch([newSearch]);
        setSearchParams({
          search: [newSearch],
        });
        runSearch(newSearch);
      } else {
        setListSearch([]);
        setSearchParams("");
        runSearch("");
      }
    } else {
      setFilters([]);
      setListSearch([]);
      setSearchParams("");
      runSearch("");
    }
  };

  return (
    <Form<Fields>
      form={form}
      id="form"
      onFinish={executeSearch}
      layout="inline"
    >
      <Form.Item
        name="field"
        id="searchselect"
        style={{ width: "15em", margin: "7px" }}
        rules={[{ required: true, message: "Informe o Campo!" }]}
      >
        <Select
          options={options}
          onSelect={(value, option) => {
            changeOperators(option);
          }}
          placeholder="Selecione um filtro"
        />
      </Form.Item>

      <Form.Item
        name="operator"
        style={{ width: "15em", margin: "7px" }}
        rules={[{ required: true, message: "Informe o Operador!" }]}
      >
        <Select
          options={operators}
          disabled={operators.length === 0}
          placeholder="Selecione um operador"
        />
      </Form.Item>

      <div id="filterdate">
        <Form.Item
          name="search"
          rules={[{ required: true, message: "Informe o filtro desejado!" }]}
        >
          {componentSelector.type === "NUMERIC" && (
            <InputNumber
              type="number"
              placeholder="Informe um número"
              id="filter"
            />
          )}
          {componentSelector.type === "DATE" && <DatePicker />}
          {componentSelector.type === "STRING" && (
            <Input type="text" id="filter" placeholder="Informe um texto" />
          )}
        </Form.Item>
      </div>

      <Form.Item>
        <Button htmlType="submit" id="searchbtn">
          <SearchOutlined />
          Pesquisar
        </Button>
      </Form.Item>

      <Form.Item>
        <Button htmlType="button" id="searchbtn" onClick={() => removeFilter()}>
          Limpar Filtros
        </Button>
      </Form.Item>
      <div id="searchdiv">
        {filters.map((filter) => (
          <Button key={filter.id} id="searchfilters">
            {filter.label}{" "}
            <CloseCircleOutlined onClick={() => removeFilter(filter)} />
          </Button>
        ))}
      </div>
    </Form>
  );
};

export default SearchComponent;
