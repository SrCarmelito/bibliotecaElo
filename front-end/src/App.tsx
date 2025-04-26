import { ConfigProvider } from "antd";
import pt from "antd/es/date-picker/locale/pt_BR";
import pt_BR from "antd/locale/pt_BR";

import { NotificationProvider } from "./contexts/notificationContext";
import RoutesApp from "./routes/Routes";
import { LoginProvider } from "./contexts/LoginContext";

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
      <LoginProvider>
        <NotificationProvider>
          <RoutesApp />
        </NotificationProvider>
      </LoginProvider>
    </ConfigProvider>
  );
};

export default App;
