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
  enumOperators,
  numericOrDateOperators,
  stringOperators,
} from "../../type/OperatorsType";

const operatorMap = {
  ENUM: enumOperators,
  STRING: stringOperators,
  NUMERIC: numericOrDateOperators,
  DATE: numericOrDateOperators,
} as const;

const sanitizeInputString = (value: string) => {
  return value
    .replace(/"/g, '\\"')
    .replace(/\\/g, "\\\\")
    .replace(/"/g, '\\"')
    .replace(/[,\][`!"';._%]/g, ""); // remover os caracteres: , { } [ ] ! " ' ; . __ % `
};

const SearchComponent = ({ optionsFilters, runSearch }: Props) => {
  const [form] = Form.useForm();
  const [operators, setOperators] = useState<BaseOptionType[]>([]);
  const [componentSelector, setComponentSelector] = useState<SearchField>(
    optionsFilters[0]
  );
  const [buttonFilters, setButtonFilters] = useState<ButtonFilters[]>([]);
  const [searchParams, setSearchParams] = useSearchParams();
  const [listSearch, setListSearch] = useState<string[]>([]);
  const [enumValues, setEnumValues] = useState([]);

  // constrói os botões de filtro para o usuário manipular quando a página é criada
  useEffect(() => {
    buildFiltersFromUrl();
  }, []);

  // carregar os operadores sem ter que clicar em um option
  useEffect(() => {
    changeOperators(optionsFilters[0]);
  }, [optionsFilters]);

  // função para criar os botões de filtro quando a página é carregada pela 1 vez e setar a listSearch inicial para
  // posterior manipulação quando for adicionado ou removido um novo filtro
  const buildFiltersFromUrl = () => {
    const searchParam = searchParams.get("search");

    if (searchParam) {
      const arraySearch: string[] = searchParam?.split(";");
      let buttonFilters: ButtonFilters[] = [];

      arraySearch.map((search) => {
        const [field, operator, value] = search.split("__");
        const label = optionsFilters.filter((option) => option.value === field);

        const operatorDescription =
          label[0].type === "STRING"
            ? stringOperators
            : numericOrDateOperators?.filter((o) => o.value === operator);

        buttonFilters.push({
          id: search,
          label:
            label[0].label +
            " " +
            operatorDescription[0].label +
            " " +
            value.replace(/"/g, ""),
        });
      });

      // cria os botões conforme filtros da url
      setButtonFilters(buttonFilters);

      // seta o listsearch com o search da url
      setListSearch([...listSearch, searchParam]);
    }
  };

  const changeOperators = (option: SearchField) => {
    form.setFieldValue("search", "");
    if (option.type === "ENUM") {
      setEnumValues(option.enumValues);
    }
    setComponentSelector(option);
    setOperators(operatorMap[option.type] ?? stringOperators);
  };

  const executeSearch = (values: Fields) => {
    let searchConverter;

    if (values.search) {
      switch (componentSelector.type) {
        case "DATE":
          searchConverter = values.search.format("YYYY-MM-DD");
          break;
        case "STRING":
          searchConverter = `"${sanitizeInputString(String(values.search))}"`;
          break;
        default:
          searchConverter = values.search;
      }
    }

    const newSearch: string = `${values.field}__${values.operator}__${searchConverter}`;

    if (listSearch.includes(newSearch)) {
      return;
    }

    //se a quantidade de filtros for >= concatena com ; como separador da lista
    if (listSearch[0]?.split(";").length >= 1) {
      const listSearchArray = listSearch.join("").concat(";", newSearch);

      setListSearch([listSearchArray]);
      setSearchParams({ search: listSearchArray });
      runSearch(listSearchArray);
    } else {
      const listSearchString = listSearch.join("").concat(newSearch);

      setListSearch([listSearchString]);
      setSearchParams({ search: [listSearchString] });
      runSearch(listSearchString.replaceAll("__", ""));
    }

    form.setFieldValue("search", "");

    const fieldLabel = optionsFilters.find(
      (item) => item.value === values.field
    )?.label;

    const operatorLabel = operators.find(
      (item) => item.value === values.operator
    )?.label;

    if (fieldLabel && operatorLabel) {
      const label = `${fieldLabel} ${operatorLabel} ${searchConverter}`;
      buildFilterFromButtonPesquisar({ id: newSearch, label });
    }
  };

  const buildFilterFromButtonPesquisar = (buttonFilter: ButtonFilters) => {
    setButtonFilters([
      ...buttonFilters,
      { id: buttonFilter.id, label: buttonFilter.label.replace(/"/g, "") },
    ]);
  };

  const removeFilter = (buttonFilter?: ButtonFilters) => {
    if (buttonFilter?.id) {
      setButtonFilters(
        buttonFilters.filter((button) => buttonFilter.id !== button.id)
      );

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
    }
  };

  const removeAllFilters = () => {
    setButtonFilters([]);
    setListSearch([]);
    setSearchParams("");
    runSearch("");
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
        rules={[{ required: true, message: "Informe o campo." }]}
      >
        <Select
          options={optionsFilters}
          onSelect={(value, option) => {
            changeOperators(option);
          }}
          placeholder="Selecione um filtro."
        />
      </Form.Item>

      <Form.Item
        name="operator"
        style={{ width: "15em", margin: "7px" }}
        rules={[{ required: true, message: "Selecione o operador." }]}
      >
        <Select
          options={operators}
          disabled={operators.length === 0}
          placeholder="Selecione um operador."
        />
      </Form.Item>

      <div id="filterdate">
        <Form.Item
          name="search"
          rules={[{ required: true, message: "Informe o filtro desejado." }]}
        >
          {componentSelector.type === "NUMERIC" && (
            <InputNumber
              type="number"
              placeholder="Informe um número."
              id="filter"
            />
          )}
          {componentSelector.type === "DATE" && <DatePicker />}
          {componentSelector.type === "STRING" && (
            <Input
              type="text"
              id="filter"
              placeholder="Informe um texto."
              onChange={(e) =>
                form.setFieldValue(
                  "search",
                  sanitizeInputString(e.target.value)
                )
              }
            />
          )}
          {componentSelector.type === "ENUM" && (
            <Select
              options={Object.keys(enumValues).map((key) => ({
                label: enumValues[key as keyof typeof enumValues],
                value: key,
              }))}
            />
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
        <Button
          htmlType="button"
          id="searchbtn"
          onClick={() => removeAllFilters()}
        >
          Limpar Filtros
        </Button>
      </Form.Item>

      <div id="searchdiv">
        {buttonFilters.map((buttonFilter) => (
          <Button key={buttonFilter.id} id="searchfilters">
            {buttonFilter.label}
            <CloseCircleOutlined onClick={() => removeFilter(buttonFilter)} />
          </Button>
        ))}
      </div>
    </Form>
  );
};

export default SearchComponent;
