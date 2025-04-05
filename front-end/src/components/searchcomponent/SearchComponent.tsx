import { Button, DatePicker, Form, Input, InputNumber, Select } from "antd";
import { useEffect, useState } from "react";
import { SearchField } from "../../type/SearchField";
import { BaseOptionType } from "antd/es/select";
import { CloseCircleOutlined, SearchOutlined } from "@ant-design/icons";
import { useSearchParams } from "react-router-dom";

type Props = { optionsFilters: SearchField[]; runSearch(search: string): void };

type Fields = {
  field: string;
  operator: string;
  search: any;
};

const SearchComponent = ({ optionsFilters, runSearch }: Props) => {
  const [form] = Form.useForm();
  const [options, setOptions] = useState(optionsFilters);
  const [operators, setOperators] = useState<BaseOptionType[]>([]);
  const [componentSelector, setComponentSelector] = useState<SearchField>(
    optionsFilters[0]
  );
  const [filters, setFilters] = useState<{ id: string; label: string }[]>([]);
  const [searchParams, setSearchParams] = useSearchParams();
  const [listSearch, setListSearch] = useState<string[]>([]);

  useEffect(() => {
    const initialSearchParam: string | null = searchParams.get("search");

    if (initialSearchParam) {
      setListSearch([...listSearch, initialSearchParam]);
    }
  }, []);

  useEffect(() => {
    setOptions(optionsFilters);
  }, [optionsFilters]);

  useEffect(() => {
    changeOperators(options[0]);
  }, [options]);

  useEffect(() => {
    if (listSearch.length > 1) {
      setSearchParams({ search: listSearch.join(";") });
      runSearch(listSearch.join(";"));
    } else {
      setSearchParams({ search: listSearch.join("") });
      runSearch(listSearch.join(""));
    }
  }, [listSearch, setSearchParams]);

  const changeOperators = (option: SearchField) => {
    let newOperators: any = [];

    switch (option.type) {
      case "STRING":
        newOperators = [
          { label: "Contém", value: "=ilike=" },
          { label: "Igual", value: "==" },
          { label: "Diferente de", value: "=notlike=" },
        ];
        break;
      case "NUMERIC":
      case "DATE":
        newOperators = [
          { label: "Igual", value: "==" },
          { label: "Maior", value: ">" },
          { label: "Menor", value: "<" },
          { label: "Maior ou igual", value: ">=" },
          { label: "Menor ou igual", value: "<=" },
        ];
        break;
    }

    form.setFieldValue("search", "");
    setComponentSelector(option);
    setOperators(newOperators);
  };

  const executeSearch = (values: Fields) => {
    let converter = values.search;

    if (componentSelector.type === "DATE" && values.search) {
      converter = values.search.format("YYYY-MM-DD");
    }

    const newSearch: string = `${values.field}${values.operator}${converter}`;

    if (listSearch.includes(newSearch)) {
      return;
    }

    setListSearch([...listSearch, newSearch]);

    clearValues();

    const fieldLabel = options.find(
      (item) => item.value === values.field
    )?.label;
    const operatorLabel = operators.find(
      (item) => item.value === values.operator
    )?.label;

    if (fieldLabel && operatorLabel) {
      const label = `${fieldLabel} ${operatorLabel} ${converter}`;
      addFilter(newSearch, label);
    }
  };

  const addFilter = (id: string, label: string) => {
    setFilters([...filters, { id, label }]);
  };

  const removeFilter = (id?: string) => {
    if (id) {
      const updatedFilters = filters.filter((filter) => filter.id !== id);
      const updatedListSearch = listSearch.filter((item) => item !== id);

      setFilters(updatedFilters);
      setListSearch(updatedListSearch);
    } else {
      setFilters([]);
      setListSearch([]);
    }
  };

  const clearValues = () => {
    form.setFieldValue("field", "");
    form.setFieldValue("operator", "");
    form.setFieldValue("search", "");
  };

  return (
    <div
      style={{
        backgroundColor: "rgba(0, 0, 0, 0.25) rgba(5, 5, 5, 0.06)",
        marginTop: "1em",
        borderRadius: "5px",
      }}
    >
      <Form<Fields>
        form={form}
        id="form"
        onFinish={executeSearch}
        layout="inline"
        style={{ marginLeft: "1em", marginBottom: "1em", marginTop: "1em" }}
      >
        <Form.Item
          name="field"
          style={{ width: "15%", margin: "7px" }}
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
          style={{ width: "15%", margin: "7px" }}
          rules={[{ required: true, message: "Informe o Operador!" }]}
        >
          <Select
            options={operators}
            disabled={operators.length === 0}
            placeholder="Selecione um operador"
          />
        </Form.Item>
        <Form.Item
          name="search"
          rules={[{ required: true, message: "Informe o filtro desejado!" }]}
        >
          {componentSelector.type === "NUMERIC" && (
            <InputNumber
              type="number"
              style={{ width: "45em" }}
              placeholder="Informe um número"
            />
          )}
          {componentSelector.type === "DATE" && (
            <DatePicker style={{ width: "45em", margin: "7px" }} />
          )}
          {componentSelector.type === "STRING" && (
            <Input
              type="text"
              style={{ width: "45em", margin: "7px" }}
              placeholder="Informe um texto"
            />
          )}
        </Form.Item>
        <Form.Item>
          <Button
            htmlType="submit"
            style={{ margin: "7px", fontWeight: "bold" }}
          >
            <SearchOutlined />
            Pesquisar
          </Button>
        </Form.Item>
        <Form.Item>
          <Button
            htmlType="button"
            style={{ margin: "7px", fontWeight: "bold" }}
            onClick={() => removeFilter()}
          >
            Limpar Filtros
          </Button>
        </Form.Item>
      </Form>

      <div style={{ marginLeft: "1em", marginTop: "0" }}>
        {filters.map((filter) => (
          <Button key={filter.id} style={{ margin: "0 0 7px 7px" }}>
            {filter.label}{" "}
            <CloseCircleOutlined onClick={() => removeFilter(filter.id)} />
          </Button>
        ))}
      </div>
    </div>
  );
};

export default SearchComponent;
