import { BrowserRouter, Route, Routes } from "react-router-dom";

import CreateUser from "../pages/CreateUser/CreateUser";

import PrivateRoutes from "./PrivateRoutes";
import LivroForm from "../pages/Livros/LivroForm";
import LivroList from "../pages/Livros/LivroList";
import LoginForm from "../pages/login/LoginForm";
import CategoriaList from "../pages/Categorias/CategoriaList";
import ResetPasswordForm from "../pages/reset-password/ResetPasswordForm";
import EmprestimoList from "../pages/Emprestimos/EmprestimoList";
import Recomendacoes from "../pages/Recomendacoes/Recomendacoes";

const RoutesApp = () => {
  return (
    <BrowserRouter>
      <Routes>
        <>
          <Route path="/" element={<LoginForm />} />
          <Route path="/signup" element={<CreateUser />} />
          <Route path="/confirm-new-password" element={<ResetPasswordForm />} />
          <Route path="*" element={<LoginForm />} />
          <Route element={<PrivateRoutes />}>
            <Route path="/livro/:id" element={<LivroForm />} />
            <Route path="/livros" element={<LivroList />} />
            <Route path="/categorias" element={<CategoriaList />} />
            <Route path="/minhaconta" element={<CreateUser />} />
            <Route path="/emprestimos" element={<EmprestimoList />} />
            <Route path="/inicio" element={<Recomendacoes />} />
          </Route>
        </>
      </Routes>
    </BrowserRouter>
  );
};

export default RoutesApp;
