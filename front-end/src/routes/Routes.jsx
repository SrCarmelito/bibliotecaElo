import { Fragment } from "react";
import { BrowserRouter, Route, Routes } from "react-router-dom";

import useAuth from "../hooks/useAuth";
import Home from "../pages/Home";
import SignIn from "../pages/SignIn";
import Signup from "../pages/SignUp";
import LivroList from "../pages/Livros/LivroList";
import LivroForm from "../pages/Livros/LivroForm";
import CreateUser from "../pages/CreateUser/CreateUser";

const Private = ({ Item }) => {
  const { signed } = useAuth();

  return signed > 0 ? <Item /> : <SignIn />;
};

const RoutesApp = () => {
  return (
    <BrowserRouter>
      <Fragment>
        <Routes>
          <Route exact path="/home" element={<Private Item={Home} />} />
          <Route path="/livro/:id" element={<LivroForm />} />
          <Route path="/livros" element={<LivroList />} />
          <Route path="/" element={<SignIn />} />
          <Route exact path="/signup" element={<CreateUser />} />
          <Route path="*" element={<SignIn />} />
        </Routes>
      </Fragment>
    </BrowserRouter>
  );
};

export default RoutesApp;
