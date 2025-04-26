import { Navigate, Outlet, useLocation } from "react-router-dom";
import { useAuth } from "../contexts/authContext";
import { MenuApp } from "../menus/MenuApp";

const PrivateRoutes = () => {
  const { user } = useAuth();
  const location = useLocation();

  return user || location.pathname.includes("/signup") ? (
    <div style={{ display: "flex" }}>
      <MenuApp />
      <div style={{ width: "100%", marginLeft: "80px" }}>
        <Outlet />
      </div>
    </div>
  ) : (
    <Navigate to="" />
  );
};

export default PrivateRoutes;
