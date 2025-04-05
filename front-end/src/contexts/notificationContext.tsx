import { notification } from "antd";
import { createContext, useCallback, useContext } from "react";
import { NotificationType } from "../type/NotificationType";

const NotificationContext = createContext(
  {} as (type: NotificationType, message?: string, description?: string) => void
);

export const useNotification = () => useContext(NotificationContext);
type Prop = { children: any };

export const NotificationProvider: React.FC<Prop> = ({ children }) => {
  const [api, contextHolderNotification] = notification.useNotification();

  const openNotification = useCallback(
    (type: NotificationType, message?: string, description?: string) => {
      return api[type]({
        message: message,
        description: description,
      });
    },
    [api]
  );

  return (
    <NotificationContext.Provider value={openNotification}>
      {children}
      {contextHolderNotification}
    </NotificationContext.Provider>
  );
};
