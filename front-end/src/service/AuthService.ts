import axios, { AxiosPromise } from "axios";
import { LoginDTO } from "../type/LoginDTO";
import { Email } from "../type/Email";
import { Usuario } from "../type/Usuario";

const resource = `${process.env.REACT_APP_URL_SERVER}/api/auth`;

export const login = (loginDTO: LoginDTO) => {
  return axios.post(resource + "/login", loginDTO);
};

export const resetPassword = (email: Email): AxiosPromise => {
  return axios.post(resource + "/reset-password", email);
};

export const confirmResetPassword = (
  confirmResetPassword: string[]
): AxiosPromise => {
  return axios.post(resource + "/confirm-reset-password", confirmResetPassword);
};

export const verifyToken = (): AxiosPromise => {
  return axios.get(resource + "/verify-token");
};

export const me = (): AxiosPromise<Usuario> => {
  return axios.get(resource + "/me");
};
