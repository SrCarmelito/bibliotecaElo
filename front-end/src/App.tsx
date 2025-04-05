import { ConfigProvider } from "antd";
import pt from "antd/es/date-picker/locale/pt_BR";
import pt_BR from "antd/locale/pt_BR";
import React from "react";

import { AuthProvider } from "./contexts/auth";
import { NotificationProvider } from "./contexts/notificationContext";
import RoutesApp from "./routes/Routes";

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
