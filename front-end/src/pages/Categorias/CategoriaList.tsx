import { Button, Form, Input, Modal, Spin, Table, TableProps } from "antd";
import React, { useEffect, useState } from "react";
import { Categoria } from "../../type/Categoria";
import { ColumnsType } from "antd/es/table";
import {
  DeleteTwoTone,
  EditTwoTone,
  ExclamationCircleFilled,
  PlusOutlined,
} from "@ant-design/icons";
import { useNotification } from "../../contexts/notificationContext";
import { SearchField } from "../../type/SearchTypes";
import {
  findAll,
  deleteById,
  saveOrUpdate,
} from "../../service/CategoriaService";
import SearchComponent from "../../components/searchcomponent/SearchComponent";
import { TableParams } from "../../interfaces/ItableParams";
import { getRandomUserParams } from "../../consts/getRandomUserParams";
import { getSearchParam } from "../../components/searchcomponent/searchFunction";
import { useLoading } from "../../components/searchcomponent/useLoading";

const searchFields: SearchField[] = [
  {
    label: "Descrição",
    type: "STRING",
    value: "descricao",
  },
];

const initialCategoria: Categoria = {
  id: "",
  descricao: "",
};

const CategoriaList: React.FC = () => {
  const [categorias, setCategorias] = useState<Categoria[]>([]);
  const [modal, contextHolder] = Modal.useModal();
  const [titleModal, setTitleModal] = useState<string>();
  const [open, setOpen] = useState(false);
  const [form] = Form.useForm();
  const openNotification = useNotification();
  const [loading, setLoading] = useLoading();
  const [tableParams, setTableParams] = useState<TableParams>({
    pagination: {
      current: 1,
      pageSize: 20,
      position: ["topCenter"],
    },
    search: getSearchParam(),
  });

  const columns: ColumnsType<Categoria> = [
    {
      title: "Descricao",
      dataIndex: "descricao",
      key: "descricao",
      sorter: true,
    },
    {
      title: "Ações",
      width: 100,
      render: (categoria: Categoria) => (
        <>
          <Button
            danger
            icon={<EditTwoTone />}
            type="link"
            onClick={() => handleOpenModal(categoria)}
          />{" "}
          <Button
            icon={<DeleteTwoTone />}
            type="link"
            onClick={() => onRemove(categoria)}
          />
        </>
      ),
    },
  ];

  const handleOpenModal = (categoria?: Categoria) => {
    categoria
      ? setTitleModal(`Editando a Categoria: ${categoria.descricao}`)
      : setTitleModal("Crie uma nova Categoria");
    form.setFieldsValue(categoria);
    setOpen(!open);
  };

  const handleCloseModal = () => {
    form.setFieldValue("id", "");
    form.setFieldValue("descricao", "");
    setOpen(!open);
  };

  const onRemove = (categoria: Categoria) => {
    modal.confirm({
      title: "Confirma Exclusão da Categoria?",
      icon: <ExclamationCircleFilled />,
      content: `${categoria.descricao}`,
      onOk() {
        deleteById(categoria.id)
          .then(() =>
            openNotification("success", "Categoria Excluída com Sucesso")
          )
          .then(() => fetchData())
          .catch((errors) => {
            const erros = errors.response.data.errors;
            if (!erros) {
              throw erros;
            }
            erros.forEach((msg: string) => {
              if (msg.match("constraint violation")) {
                msg =
                  "Não é possível excluir a Categoria pois existem registros que dependem dela.";
              }
              openNotification("error", "Falha ao Excluir a Categoria", msg);
            });
          });
      },
    });
  };

  const fetchData = (search?: string) => {
    setLoading(
      findAll(
        getRandomUserParams(tableParams).pagination,
        getRandomUserParams(tableParams).sortField,
        getRandomUserParams(tableParams).sortOrder,
        getSearchParam()
      ).then((response) => {
        setCategorias(response.data.content);
        setTableParams({
          ...tableParams,
          pagination: {
            ...tableParams.pagination,
            total: response.data.totalElements,
          },
          search: getSearchParam(),
        });
      })
    );
  };

  useEffect(fetchData, [
    tableParams.pagination?.current,
    tableParams.pagination?.pageSize,
    tableParams?.sortField,
    tableParams?.sortOrder,
    JSON.stringify(tableParams.filters),
  ]);

  const handleTableChange: TableProps<Categoria>["onChange"] = (
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
      setCategorias([]);
    }
  };

  const onSubmit = (categoria: Categoria) => {
    setLoading(
      saveOrUpdate(categoria)
        .then((res) => {
          const msg =
            res.config.method === "put"
              ? "Categoria Atualizada com Sucesso!"
              : "Categoria Cadastrada com Sucesso!";
          openNotification("success", msg);
          handleCloseModal();
        })
        .then(() => fetchData())
        .catch((errors) => {
          const erros = errors.response?.data.errors;
          if (!erros) {
            throw erros;
          }
          erros.forEach((msg: string) => {
            openNotification("error", "Falha ao Cadastrar a Categoria", msg);
          });
        })
    );
  };

  return (
    <>
      <SearchComponent optionsFilters={searchFields} runSearch={fetchData} />
      <Table<Categoria>
        rowKey="id"
        bordered
        size="small"
        columns={columns}
        dataSource={categorias}
        pagination={tableParams.pagination}
        onChange={handleTableChange}
        loading={loading}
      />
      {contextHolder}
      <Button
        type="primary"
        size="large"
        shape="circle"
        onClick={() => handleOpenModal()}
        icon={<PlusOutlined />}
        style={{ position: "fixed", zIndex: 1, bottom: 40, right: 40 }}
      />
      <Modal
        open={open}
        footer
        title={titleModal}
        onCancel={() => handleCloseModal()}
      >
        <Spin spinning={loading} fullscreen />
        <Form<Categoria>
          initialValues={initialCategoria}
          form={form}
          onFinish={onSubmit}
          id="styledform"
          key="id"
        >
          <Form.Item name="id" noStyle />
          <Form.Item
            name="descricao"
            rules={[{ required: true, message: "Informe a Descrição!" }]}
          >
            <Input placeholder="Informe a Descrição" />
          </Form.Item>
          <Form.Item>
            <Button htmlType="submit" block type="primary">
              Confirmar
            </Button>
          </Form.Item>
        </Form>
      </Modal>
    </>
  );
};

export default CategoriaList;
