import React from "react";
import { BrowserRouter as Router, Routes, Route } from "react-router-dom";

import LoginForm from "./components/Login";
import RegisterForm from "./components/RegisterForm";
import AddTransactionForm from "./components/AddTransactionForm";
import RequireAuth from "./components/RequireAuth";
import TransactionList from "./components/TransactionList";
import Dashboard from "./components/Dashboard";

function App() {
  return (
    <Router>
      <Routes>
        <Route path="/login" element={<LoginForm />} />
        <Route path="/register" element={<RegisterForm />} />
        <Route
          path="/dashboard"
          element={
            <RequireAuth>
              <Dashboard />
            </RequireAuth>
          }
        />

        <Route
          path="/transactions"
          element={
            <RequireAuth>
              <TransactionList />
            </RequireAuth>
          }
        />
        <Route
          path="/add-transaction"
          element={
            <RequireAuth>
              <AddTransactionForm />
            </RequireAuth>
          }
        />

        {/* Optional: default route */}
        <Route path="*" element={<LoginForm />} />
      </Routes>
    </Router>
  );
}

export default App;
