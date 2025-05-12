import React, { useEffect, useState } from "react";
import axios from "axios";
import { Link } from "react-router-dom";
import "../pages/TransactionList.css";
import TransactionFilterForm from "./TransactionfilterForm";

const TransactionList = () => {
  const [, setTransactions] = useState([]);
  const [filteredTransactions, setFilteredTransactions] = useState([]);

  useEffect(() => {
    const fetchTransactions = async () => {
      try {
        const res = await axios.get("http://localhost:8080/api/transactions", {
          headers: {
            Authorization: `Bearer ${localStorage.getItem("token")}`,
          },
        });
        setTransactions(res.data);
        setFilteredTransactions(res.data);
      } catch (error) {
        console.error("Error fetching transactions", error);
      }
    };

    fetchTransactions();
  }, []);

  const handleExport = async () => {
    const token = localStorage.getItem("token");
    try {
      const response = await fetch(
        "http://localhost:8080/api/transactions/export",
        {
          method: "GET",
          headers: {
            Authorization: `Bearer ${token}`,
          },
        }
      );

      const blob = await response.blob();
      const url = window.URL.createObjectURL(blob);

      const link = document.createElement("a");
      link.href = url;
      link.setAttribute("download", "transactions.csv");
      document.body.appendChild(link);
      link.click();
      link.remove();
    } catch (err) {
      console.error("Export failed", err);
    }
  };

  return (
    <div className="transaction-list-page">
      <div className="transaction-header">
        <h2>All Transactions</h2>
        <div className="transaction-header-buttons">
          <Link to="/add-transaction" className="add-transaction-button">
            Add Transaction
          </Link>
        </div>
      </div>

      <TransactionFilterForm setFilteredResults={setFilteredTransactions} />

      <table className="transaction-table">
        <thead>
          <tr>
            <th>Date</th>
            <th>Type</th>
            <th>Amount</th>
            <th>Category</th>
            <th>Description</th>
          </tr>
        </thead>
        <tbody>
          {filteredTransactions.map((tx) => (
            <tr key={tx.id}>
              <td data-label="Date">{tx.date}</td>
              <td data-label="Type">{tx.type}</td>
              <td data-label="Amount">₹ {tx.amount}</td>
              <td data-label="Category">{tx.category}</td>
              <td data-label="Description">{tx.description}</td>
            </tr>
          ))}
        </tbody>
      </table>

      <br />
      <Link to="/dashboard" className="dashboard-button">
        ← Back to Dashboard
      </Link>
      <button onClick={handleExport} className="export-button">
        Export to CSV
      </button>
    </div>
  );
};

export default TransactionList;
