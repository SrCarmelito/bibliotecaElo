import React from "react";
import { createRoot } from "react-dom/client";
import App from "./App";

const container = document.getElementById("root");

if (container) {
  const root = createRoot(container); // Novo método de renderização
  root.render(<App />);
} else {
  console.error("Elemento 'root' não encontrado no DOM.");
}
