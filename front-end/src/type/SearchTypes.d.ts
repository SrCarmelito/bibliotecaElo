export type SearchField = {
  value: string;
  label: string;
  type: string;
};

export type Props = {
  optionsFilters: SearchField[];
  runSearch(search: string): void;
};

export type Fields = {
  field: string;
  operator: string;
  search: any;
};

export type Operator = {
  label: string;
  value: string;
};

export type ButtonFilters = { id: string; label: string };
