import React, { useEffect, useState } from "react";
import {
  Button,
  Modal,
  Form,
  Table,
  TableProps,
  Spin,
  DatePicker,
  Select,
} from "antd";
import SearchComponent from "../../components/searchcomponent/SearchComponent";
import { SearchField } from "../../type/SearchTypes";
import { Emprestimo } from "../../type/Emprestimo";
import { ColumnsType } from "antd/es/table";
import { TableParams } from "../../interfaces/ItableParams";
import { useSearchParams } from "react-router-dom";
import { EditTwoTone, PlusOutlined } from "@ant-design/icons";
import { findAll, saveOrUpdate } from "../../service/EmprestimoService";
import {
  findAll as findAllLivros,
  findById as findLivroById,
} from "../../service/LivroService";
import { getRandomUserParams } from "../../consts/getRandomUserParams";
import { StatusEmprestimoEnum } from "../../enums/StatusEmprestimoEnum";
import { Livro } from "../../type/Livro";
import { useAuth } from "../../contexts/authContext";
import { useNotification } from "../../contexts/notificationContext";

const searchFields: SearchField[] = [
  {
    label: "Livro",
    type: "STRING",
    value: "livro.titulo",
  },
  {
    label: "Data Empréstimo",
    type: "DATE",
    value: "dataEmprestimo",
  },
  {
    label: "Data Devolução",
    type: "DATE",
    value: "dataDevolucao",
  },
  {
    label: "Situação",
    type: "DATE",
    value: "dataDevolucao",
  },
];

const initialEmprestimo: Emprestimo = {
  id: "",
  dataDevolucao: "",
  dataEmprestimo: "",
  livro: undefined,
  status: "",
  usuario: undefined,
};

const EmprestimoList: React.FC = () => {
  const [emprestimos, setEmprestimos] = useState<Emprestimo[]>([]);
  const openNotification = useNotification();
  const [modal, contextHolder] = Modal.useModal();
  const [open, setOpen] = useState(false);
  const [titleModal, setTitleModal] = useState<string>();
  const [form] = Form.useForm();
  const [spinning, setSpinning] = useState(true);
  const [searchParams] = useSearchParams();
  const [livros, setLivros] = useState<Livro[]>([]);
  const { token, usuario } = useAuth();
  const [tableParams, setTableParams] = useState<TableParams>({
    pagination: {
      current: 1,
      pageSize: 20,
      position: ["topCenter"],
    },
    search: undefined,
  });

  const columns: ColumnsType<Emprestimo> = [
    {
      title: "Titulo",
      dataIndex: "livro",
      key: "livro",
      render: (livro) => livro.titulo,
      sorter: true,
    },
    {
      title: "Data Empréstimo",
      dataIndex: "dataEmprestimo",
      key: "dataEmprestimo",
      render: (dataEmprestimo) => dataEmprestimo.format("DD/MM/YYYY"),
      sorter: true,
    },
    {
      title: "Devolução",
      dataIndex: "dataDevolucao",
      key: "Date",
      render: (dataDevolucao) => dataDevolucao.format("DD/MM/YYYY"),
      sorter: true,
    },
    {
      title: "Situação",
      dataIndex: "status",
      key: "status",
      render: (text) =>
        StatusEmprestimoEnum[text as keyof typeof StatusEmprestimoEnum],
      sorter: true,
    },
    {
      title: "Ações",
      width: 60,
      render: (emprestimo: Emprestimo) => (
        <Button
          danger
          icon={<EditTwoTone />}
          type="link"
          onClick={() => handleOpenModal(emprestimo)}
        />
      ),
    },
  ];

  const handleTableChange: TableProps<Emprestimo>["onChange"] = (
    pagination,
    filters,
    sorter
  ) => {
    setTableParams({
      pagination,
      filters,
      sortField: Array.isArray(sorter) ? undefined : sorter.field,
      sortOrder: Array.isArray(sorter) ? undefined : sorter.order,
    });

    if (pagination.pageSize !== tableParams.pagination?.pageSize) {
      setEmprestimos([]);
    }
  };

  const fetchData = (search?: string) => {
    setSpinning(true);
    findAll(
      getRandomUserParams(tableParams).pagination,
      getRandomUserParams(tableParams).sortField,
      getRandomUserParams(tableParams).sortOrder,
      search || searchParams.get("search")?.replaceAll("__", "")
    ).then((response) => {
      setEmprestimos(response.data.content);
      setTableParams({
        ...tableParams,
        pagination: {
          ...tableParams.pagination,
          total: response.data.totalElements,
        },
        search: search || searchParams.get("search")?.replaceAll("__", ""),
      });
    });
    setSpinning(false);
  };

  const findLivros = (search?: string) => {
    search ? (search = `titulo=ilike=${search}`) : (search = "");
    findAllLivros(0, "titulo", "asc", search).then((response) => {
      setLivros(response.data.content);
    });
  };

  useEffect(fetchData, [
    tableParams.pagination?.current,
    tableParams.pagination?.pageSize,
    tableParams?.sortField,
    tableParams?.sortOrder,
    JSON.stringify(tableParams.filters),
  ]);

  useEffect(findLivros, [setLivros]);

  const handleOpenModal = (emprestimo?: Emprestimo) => {
    emprestimo
      ? setTitleModal("Editando o Empréstimo")
      : setTitleModal("Faça um novo Empréstimo");

    if (emprestimo) {
      form.setFieldsValue(emprestimo);
      findLivroById(emprestimo?.livro?.id).then((response) => {
        setLivros([...livros, ...[response?.data]]);
      });
      form.setFieldsValue({
        livro: emprestimo?.livro?.id,
      });
    }

    setOpen(!open);
  };

  const handleCloseModal = () => {
    form.setFieldsValue(initialEmprestimo);
    setOpen(!open);
  };

  const onSubmit = (emprestimo: Emprestimo) => {
    emprestimo.livro = livros.find((l) => l.id === emprestimo.livro);

    if (!emprestimo.usuario) {
      emprestimo.usuario = usuario;
    }

    saveOrUpdate(emprestimo)
      .then((res) => {
        const msg =
          res.config.method === "put"
            ? "Empréstimo Atualizado com Sucesso!"
            : "Empréstimo Cadastrado com Sucesso!";
        openNotification("success", msg);
        handleCloseModal();
        fetchData();
      })
      .then(() => fetchData())
      .catch((errors) => {
        const erros = errors.response?.data.errors;
        if (!erros) {
          throw erros;
        }
        erros.forEach((msg: string) => {
          openNotification("error", "Falha ao Cadastrar o Empréstimo", msg);
        });
      });
  };

  return (
    <>
      <>
        <SearchComponent optionsFilters={searchFields} runSearch={fetchData} />
        <Table<Emprestimo>
          rowKey={"id"}
          dataSource={emprestimos}
          columns={columns}
          bordered
          size="small"
          pagination={tableParams.pagination}
          loading={spinning}
          onChange={handleTableChange}
        />
        {contextHolder}
      </>
      <Modal
        open={open}
        footer
        title={titleModal}
        onCancel={() => handleCloseModal()}
      >
        <Spin spinning={spinning} fullscreen />
        <Form<Emprestimo>
          initialValues={initialEmprestimo}
          form={form}
          onFinish={onSubmit}
          id="styledform"
        >
          <Form.Item name="id" noStyle />
          <Form.Item name="usuario" noStyle />
          <Form.Item
            label="Livro"
            name="livro"
            rules={[{ required: true, message: "Informe o Livro!" }]}
          >
            <Select
              showSearch
              disabled={form.getFieldValue("id")}
              filterOption={(input, livro) =>
                (livro?.titulo ?? "")
                  .toLowerCase()
                  .includes(input.toLowerCase())
              }
              onSearch={findLivros}
              options={livros}
              fieldNames={{ label: "titulo", value: "id" }}
              placeholder="Selecione o livro"
            />
          </Form.Item>
          <Form.Item
            name="dataEmprestimo"
            label="Data"
            rules={[
              { required: true, message: "Informe a data do empréstimo." },
            ]}
          >
            <DatePicker
              disabled={form.getFieldValue("id")}
              placeholder="Data do empréstimo"
              style={{ display: "block" }}
            />
          </Form.Item>
          <Form.Item
            name="dataDevolucao"
            label="Devolução"
            rules={[
              { required: true, message: "Informe a data de devolução." },
            ]}
          >
            <DatePicker
              placeholder="Data de devolução"
              style={{ display: "block" }}
            />
          </Form.Item>
          <Form.Item name="status" label="Situação">
            <Select
              options={Object.entries(StatusEmprestimoEnum).map(
                ([key, value]) => ({
                  label: value,
                  value: key,
                })
              )}
              placeholder="Digite o título"
            />
          </Form.Item>
          <Form.Item label={null}>
            <Button type="primary" htmlType="submit" block>
              {token ? "Atualizar" : "Cadastrar"}
            </Button>
          </Form.Item>
        </Form>
      </Modal>
      <Button
        onClick={() => handleOpenModal()}
        type="primary"
        size="large"
        shape="circle"
        icon={<PlusOutlined />}
        style={{ position: "fixed", zIndex: 1, bottom: 40, right: 40 }}
      />
    </>
  );
};

export default EmprestimoList;
