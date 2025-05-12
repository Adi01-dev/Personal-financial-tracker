import React, { useEffect, useState } from "react";
import axios from "axios";
import "../pages/Dashboard.css";
import { Link } from "react-router-dom";
// import MonthlyChart from "./MonthlyChart";

const Dashboard = () => {
  const [summary, setSummary] = useState(null);
  const [error] = useState("");

  useEffect(() => {
    const fetchDashboardSummary = async () => {
      const token = localStorage.getItem("token");
      if (!token) return;

      try {
        const res = await axios.get(
          "http://localhost:8080/api/transactions/dashboard",
          {
            headers: {
              Authorization: `Bearer ${token}`,
            },
          }
        );
        setSummary(res.data);
      } catch (err) {
        console.error("Error fetching dashboard summary", err);
      }
    };

    fetchDashboardSummary();
  }, []);

  if (!summary)
    return (
      <div className="text-center mt-10 text-gray-500">
        Loading dashboard...
      </div>
    );

  return (
    <div className="dashboard-container">
      <h2>Dashboard</h2>
      {error && <p className="error">{error}</p>}
      {summary && (
        <>
          <div className="summary-cards">
            <div className="card income">
              <h3>Income</h3>
              <p>₹ {summary.totalIncome}</p>
            </div>
            <div className="card expense">
              <h3>Expense</h3>
              <p>₹ {summary.totalExpense}</p>
            </div>
            <div className="card balance">
              <h3>Balance</h3>
              <p>₹ {summary.balance}</p>
            </div>
          </div>
          {/* <div className="monthly-chart-wrapper">
            <MonthlyChart />
          </div> */}
        </>
      )}
 
      <br></br>
      <Link to="/transactions" className="view-all-link">
        View All Transactions →
      </Link>

     
    </div>
  );
};

export default Dashboard;
