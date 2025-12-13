export function handleApiError(
  openNotification: any,
  errors: any,
  title: string
) {
  const erros = errors?.response?.data?.errors;

  if (!erros) {
    throw errors;
  }

  erros.forEach((msg: string) => {
    if (msg.match("violates foreign key constraint")) {
      msg =
        "Não é possível excluir pois existem outros registros que dependem dele.";
    }

    openNotification("error", title, msg);
  });
}
