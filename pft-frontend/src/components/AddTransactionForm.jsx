import React, { useState } from "react";
import axios from "axios";

import "../pages/AddTransactionForm.css";
import { Link } from "react-router-dom";

const AddTransactionForm = () => {
  const [formData, setFormData] = useState({
    amount: "",
    category: "",
    type: "expense",
    date: "",
    description: "",
  });

  const [successMessage, setSuccessMessage] = useState("");
  const [errorMessage, setErrorMessage] = useState("");

  const handleChange = (e) => {
    setFormData({ ...formData, [e.target.name]: e.target.value });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setSuccessMessage("");
    setErrorMessage("");

    try {
      const token = localStorage.getItem("token");
      await axios.post("http://localhost:8080/api/transactions", formData, {
        withCredentials: true,
        headers: {
          Authorization: `Bearer ${token}`,
        },
      });

      setSuccessMessage("Transaction added successfully!");
      setFormData({
        amount: "",
        category: "",
        type: "expense",
        date: "",
        description: "",
      });
    } catch (error) {
      setErrorMessage("Failed to add transaction. Please try again.");
      console.error(error);
    }
  };

  return (
    <div className="add-transaction-container">
      <h2>Add Transaction</h2>
      {successMessage && <p className="success-message">{successMessage}</p>}
      {errorMessage && <p className="error-message">{errorMessage}</p>}

      <form className="add-transaction-form" onSubmit={handleSubmit}>
        <input
          type="number"
          name="amount"
          placeholder="Amount"
          value={formData.amount}
          onChange={handleChange}
          required
        />
        <input
          type="text"
          name="category"
          placeholder="Category"
          value={formData.category}
          onChange={handleChange}
          required
        />
        <select name="type" value={formData.type} onChange={handleChange}>
          <option value="income">Income</option>
          <option value="expense">Expense</option>
        </select>
        <input
          type="date"
          name="date"
          value={formData.date}
          onChange={handleChange}
          required
        />
        <input
          type="text"
          name="description"
          placeholder="Description"
          value={formData.description}
          onChange={handleChange}
        />
        <button type="submit">Add</button>

        <Link to="/transactions" className="back-button">
          View Transactions
        </Link>
      </form>
    </div>
  );
};

export default AddTransactionForm;
