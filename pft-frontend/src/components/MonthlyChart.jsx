import React, { useEffect, useState } from "react";
import axios from "axios";
import {
  LineChart,
  Line,
  XAxis,
  YAxis,
  Tooltip,
  Legend,
  ResponsiveContainer,
  CartesianGrid,
} from "recharts";
import '../pages/MonthlyChart.css';

const MonthlyChart = () => {
  const [chartData, setChartData] = useState([]);
  const [error, setError] = useState("");

  useEffect(() => {
    const fetchChartData = async () => {
    
        const token = localStorage.getItem("token");
        try{
        const response = await axios.get(
          "http://localhost:8080/api/transactions/chart/monthly",
          {
            headers: {
              Authorization: `Bearer ${token}`,
            },
          }
        );
        setChartData(response.data);
      } catch (err) {
        console.error("Failed to fetch chart data:", err);
        setError("Failed to load chart. Please try again.");
      }
    };

    fetchChartData();
  }, []);

  return (
    <div className="chart-container" style={{ marginTop: "2rem" }}>
      <h3 style={{ textAlign: "center" }}>Monthly Income vs Expense</h3>
      {error && <p style={{ color: "red", textAlign: "center" }}>{error}</p>}
      <ResponsiveContainer width="100%" height={300}>
        <LineChart data={chartData}>
          <CartesianGrid strokeDasharray="3 3" />
          <XAxis dataKey="month" />
          <YAxis />
          <Tooltip />
          <Legend />
          <Line
            type="monotone"
            dataKey="totalIncome"
            stroke="#82ca9d"
            name="Income"
          />
          <Line
            type="monotone"
            dataKey="totalExpense"
            stroke="#ff6666"
            name="Expense"
          />
        </LineChart>
      </ResponsiveContainer>
    </div>
  );
};

export default MonthlyChart;
