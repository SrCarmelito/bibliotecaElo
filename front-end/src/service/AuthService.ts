import axios, { AxiosPromise } from "axios";
import { LoginDTO } from "../type/LoginDTO";
import { Email } from "../type/Email";

const resource = "http://localhost:8080/api/auth";

export const login = (loginDTO: LoginDTO) => {
  return axios.post(resource + "/login", loginDTO);
};

export const resetPassword = (email: Email): AxiosPromise => {
  return axios.post(resource + "/reset-password", email);
};

export const verifyToken = (): AxiosPromise => {
  return axios.get(resource + "/verify-token");
};
