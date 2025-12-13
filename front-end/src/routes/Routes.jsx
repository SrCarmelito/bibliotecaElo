import { BrowserRouter, Route, Routes } from "react-router-dom";
import { lazy } from "react";

const CreateUser = lazy(() => import("../pages/CreateUser/CreateUser"));
const ErrorPage = lazy(() => import("../pages/ErrorPage/ErrorPage"));

const LivroList = lazy(() => import("../pages/Livros/LivroList"));
const LoginForm = lazy(() => import("../pages/login/LoginForm"));
const CategoriaList = lazy(() => import("../pages/Categorias/CategoriaList"));
const ResetPasswordForm = lazy(() =>
  import("../pages/reset-password/ResetPasswordForm")
);
const EmprestimoList = lazy(() =>
  import("../pages/Emprestimos/EmprestimoList")
);
const Recomendacoes = lazy(() =>
  import("../pages/Recomendacoes/Recomendacoes")
);
const PrivateRoutes = lazy(() => import("./PrivateRoutes"));
const LivroForm = lazy(() => import("../pages/Livros/LivroForm"));

const RoutesApp = () => {
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/" element={<LoginForm />} />
        <Route path="/signup" element={<CreateUser />} />
        <Route path="/confirm-new-password" element={<ResetPasswordForm />} />
        <Route path="/error" element={<ErrorPage />} />
        <Route path="*" element={<LoginForm />} />
        <Route element={<PrivateRoutes />}>
          <Route path="/livro/:id" element={<LivroForm />} />
          <Route path="/livros" element={<LivroList />} />
          <Route path="/categorias" element={<CategoriaList />} />
          <Route path="/minhaconta" element={<CreateUser />} />
          <Route path="/emprestimos" element={<EmprestimoList />} />
          <Route path="/inicio" element={<Recomendacoes />} />
        </Route>
      </Routes>
    </BrowserRouter>
  );
};

export default RoutesApp;
