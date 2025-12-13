export function sortResolver(
  sortField?: string,
  sortOrder?: string,
  defaultSortField?: string,
  defaultSortOrder?: string
) {
  if (sortField) {
    return {
      sort: `${sortField},${
        sortOrder?.substring(0, 1) === "a" ? "asc" : "desc"
      }`,
    };
  }
  return { sort: `${defaultSortField},${defaultSortOrder}` };
}
