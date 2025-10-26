import React, { useEffect, useState } from "react";
import { Button, Modal, Table, TableProps } from "antd";
import { deleteById, findAll } from "../../service/LivroService";
import { Livro } from "../../type/Livro";
import {
  DeleteTwoTone,
  EditTwoTone,
  ExclamationCircleFilled,
  PlusOutlined,
} from "@ant-design/icons";
import { useNavigate } from "react-router";
import SearchComponent from "../../components/searchcomponent/SearchComponent";
import { SearchField } from "../../type/SearchTypes";
import { useNotification } from "../../contexts/notificationContext";
import { ColumnsType } from "antd/es/table";
import { TableParams } from "../../interfaces/ItableParams";
import { getRandomUserParams } from "../../consts/getRandomUserParams";
import { getSearchParam } from "../../components/searchcomponent/searchFunction";

const searchFields: SearchField[] = [
  {
    label: "Autor",
    type: "STRING",
    value: "autor",
  },
  {
    label: "Categoria",
    type: "STRING",
    value: "categoria.descricao",
  },
  {
    label: "Isbn",
    type: "STRING",
    value: "isbn",
  },
  {
    label: "Publicação",
    type: "DATE",
    value: "dataPublicacao",
  },
  {
    label: "Titulo",
    type: "STRING",
    value: "titulo",
  },
];

const LivroList: React.FC = () => {
  const [livros, setLivros] = useState<Livro[]>([]);
  const [modal, contextHolder] = Modal.useModal();
  const navigate = useNavigate();
  const openNotification = useNotification();
  const [spinning, setSpinning] = useState(true);
  const [tableParams, setTableParams] = useState<TableParams>({
    pagination: {
      current: 1,
      pageSize: 20,
      position: ["topCenter"],
    },
    search: getSearchParam(),
  });

  const fetchData = () => {
    setSpinning(true);
    findAll(
      getRandomUserParams(tableParams).pagination,
      getRandomUserParams(tableParams).sortField,
      getRandomUserParams(tableParams).sortOrder,
      getSearchParam()
    ).then((response) => {
      setLivros(response.data.content);
      setTableParams({
        ...tableParams,
        pagination: {
          ...tableParams.pagination,
          total: response.data.totalElements,
        },
        search: getSearchParam(),
      });
    });
    setSpinning(false);
  };

  useEffect(fetchData, [
    tableParams.pagination?.current,
    tableParams.pagination?.pageSize,
    tableParams?.sortField,
    tableParams?.sortOrder,
    JSON.stringify(tableParams.filters),
  ]);

  const handleTableChange: TableProps<Livro>["onChange"] = (
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
      setLivros([]);
    }
  };

  const onRemove = (livro: Livro) => {
    modal.confirm({
      title: "Confirma Exclusão do Livro?",
      icon: <ExclamationCircleFilled />,
      content: `${livro.titulo} - ${
        livro.categoria?.descricao
      } - ${livro.dataPublicacao.format("DD/MM/YYYY")}`,
      onOk() {
        deleteById(livro.id)
          .then(() => openNotification("success", "Livro Excluído com Sucesso"))
          .then(() => fetchData())
          .catch((errors) => {
            const erros = errors.response.data.errors;
            if (!erros) {
              throw erros;
            }
            erros.forEach((msg: string) => {
              if (msg.match("constraint violation")) {
                msg =
                  "Não é possível excluir o Livro pois existem registros que dependem dele.";
              }
              openNotification("error", "Falha ao Excluir o Livro", msg);
            });
          });
      },
    });
  };

  const columns: ColumnsType<Livro> = [
    {
      title: "Titulo",
      dataIndex: "titulo",
      key: "titulo",
      sorter: true,
    },
    {
      title: "Autor",
      dataIndex: "autor",
      key: "autor",
      sorter: true,
    },
    {
      title: "Categoria",
      dataIndex: "categoria",
      key: "categoria",
      render: (categoria) => categoria.descricao,
      sorter: true,
    },
    {
      title: "Isbn",
      dataIndex: "isbn",
      key: "isbn",
      sorter: true,
    },
    {
      title: "Publicação",
      dataIndex: "dataPublicacao",
      key: "Date",
      render: (dataPublicacao) => dataPublicacao.format("DD/MM/YYYY"),
      sorter: true,
    },
    {
      title: "Ações",
      width: 100,
      render: (livro: Livro) => (
        <>
          <Button
            danger
            icon={<EditTwoTone />}
            type="link"
            onClick={() => navigate(`/livro/${livro.id}`)}
          />{" "}
          <Button
            icon={<DeleteTwoTone />}
            type="link"
            onClick={() => onRemove(livro)}
          />
        </>
      ),
    },
  ];

  return (
    <>
      <>
        <SearchComponent optionsFilters={searchFields} runSearch={fetchData} />
        <Table<Livro>
          rowKey={"id"}
          dataSource={livros}
          columns={columns}
          bordered
          size="small"
          pagination={tableParams.pagination}
          loading={spinning}
          onChange={handleTableChange}
        />
        {contextHolder}
      </>

      <Button
        onClick={() => navigate("/livro/new")}
        type="primary"
        size="large"
        shape="circle"
        icon={<PlusOutlined />}
        style={{ position: "fixed", zIndex: 1, bottom: 40, right: 40 }}
      />
    </>
  );
};

export default LivroList;
