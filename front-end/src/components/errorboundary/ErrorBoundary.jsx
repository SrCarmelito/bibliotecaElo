import React from "react";
import ErrorPage from "../../pages/ErrorPage/ErrorPage";

export class ErrorBoundary extends React.Component {
  state = {
    hasError: false,
    errorMessage: "",
  };

  static getDerivedStateFromError(error) {
    console.error(error);
    return { hasError: true };
  }

  render() {
    if (this.state.hasError) {
      return <ErrorPage />;
    }

    return this.props.children;
  }
}
