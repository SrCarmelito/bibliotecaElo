import { Navigate, Outlet, useLocation } from "react-router-dom";
import { useAuth } from "../contexts/authContext";
import { MenuApp } from "../menus/MenuApp";

const PrivateRoutes = () => {
  const { token } = useAuth();
  const location = useLocation();

  return token || location.pathname.includes("/signup") ? (
    <div style={{ display: "flex" }}>
      <MenuApp />
      <div style={{ width: "100%", marginLeft: "80px", marginTop: "2%" }}>
        <Outlet />
      </div>
    </div>
  ) : (
    <Navigate to="" />
  );
};

export default PrivateRoutes;
