import pt from "antd/es/date-picker/locale/pt_BR";
import pt_BR from "antd/locale/pt_BR";

export const globalLocale: typeof pt_BR = {
  ...pt_BR,
  DatePicker: {
    ...pt_BR.DatePicker!,
    lang: {
      ...pt.lang,
      fieldDateFormat: "DD/MM/YYYY",
      fieldDateTimeFormat: "YYYY/MM/DD HH:mm:ss",
      yearFormat: "YYYY",
      cellYearFormat: "YYYY",
    },
  },
};
