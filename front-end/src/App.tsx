import { ConfigProvider } from "antd";

import { NotificationProvider } from "./contexts/notificationContext";
import RoutesApp from "./routes/Routes";
import { AuthProvider } from "./contexts/authContext";
import axios from "axios";
import dayjs from "dayjs";
import { globalLocale } from "./consts/globalLocale";
import { ErrorBoundary } from "./components/errorboundary/ErrorBoundary";

axios.defaults.baseURL = `${process.env.REACT_APP_URL_SERVER}/api`;

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

const App: React.FC = () => {
  return (
    <ConfigProvider locale={globalLocale}>
      <ErrorBoundary>
        <AuthProvider>
          <NotificationProvider>
            <RoutesApp />
          </NotificationProvider>
        </AuthProvider>
      </ErrorBoundary>
    </ConfigProvider>
  );
};

export default App;
