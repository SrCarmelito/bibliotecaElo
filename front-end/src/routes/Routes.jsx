import { BrowserRouter, Route, Routes } from "react-router-dom";

import CreateUser from "../pages/CreateUser/CreateUser";
import LoginForm from "../pages/Login/LoginForm";
import PrivateRoutes from "./PrivateRoutes";
import LivroForm from "../pages/Livros/LivroForm";
import LivroList from "../pages/Livros/LivroList";

const RoutesApp = () => {
  return (
    <BrowserRouter>
      <Routes>
        <>
          <Route path="/" element={<LoginForm />} />
          <Route path="/signup" element={<CreateUser />} />
          <Route path="*" element={<LoginForm />} />
          <Route element={<PrivateRoutes />}>
            <Route path="/livro/:id" element={<LivroForm />} />
            <Route path="/livros" element={<LivroList />} />
          </Route>
        </>
      </Routes>
    </BrowserRouter>
  );
};

export default RoutesApp;
