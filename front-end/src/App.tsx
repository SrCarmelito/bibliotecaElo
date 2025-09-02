import { ConfigProvider } from "antd";
import pt from "antd/es/date-picker/locale/pt_BR";
import pt_BR from "antd/locale/pt_BR";

import { NotificationProvider } from "./contexts/notificationContext";
import RoutesApp from "./routes/Routes";
import { AuthProvider } from "./contexts/authContext";

import axios from "axios";
import dayjs from "dayjs";

axios.interceptors.response.use((response) => {
  const transformDayJsToDate = (obj: any) => {
    for (const key in obj) {
      if (
        obj[key] &&
        typeof obj[key] === "string" &&
        /\d{4}-\d{2}-\d{2}/.test(obj[key])
      ) {
        obj[key] = dayjs(obj[key]);
      } else if (typeof obj[key] === "object") {
        transformDayJsToDate(obj[key]);
      }
    }
  };

  transformDayJsToDate(response.data);

  return response;
});

const globalLocale: typeof pt_BR = {
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

const App: React.FC = () => {
  return (
    <ConfigProvider locale={globalLocale}>
      <AuthProvider>
        <NotificationProvider>
          <RoutesApp />
        </NotificationProvider>
      </AuthProvider>
    </ConfigProvider>
  );
};

export default App;
