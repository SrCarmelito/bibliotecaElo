import { TableProps } from "antd";
import { Key } from "react";

interface TableParams {
  pagination?: any;
  filters?: any;
  sortField?: Key | readonly Key[];
  sortOrder?: string | null;
}

export function genericTableChange<T>(
  tableParams: TableParams,
  setTableParams: React.Dispatch<React.SetStateAction<TableParams>>,
  setData: React.Dispatch<React.SetStateAction<T[]>>
) {
  const handleTableChange: TableProps<T>["onChange"] = (
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
      setData([]);
    }
  };

  return { handleTableChange };
}
