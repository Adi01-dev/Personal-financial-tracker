import React, { useState } from 'react';
import axios from 'axios';
import '../pages/TransactionfilterForm.css';

const TransactionFilterForm = ({ setFilteredResults }) => {
  const [filters, setFilters] = useState({
    category: '',
    type: '',
    startDate: '',
    endDate: '',
  });

  const handleChange = (e) => {
    setFilters({ ...filters, [e.target.name]: e.target.value });
  };

  const handleFilter = async (e) => {
    e.preventDefault();
    try {
      const token = localStorage.getItem('token');
      const response = await axios.get('http://localhost:8080/api/transactions/filter', {
        headers: {
          Authorization: `Bearer ${token}`,
        },
        params: filters,
      });
      setFilteredResults(response.data);
    } catch (err) {
      console.error("Filter request failed", err);
    }
  };

  return (
    <form className="filter-form" onSubmit={handleFilter}>
      <input
        type="text"
        name="category"
        placeholder="Category"
        value={filters.category}
        onChange={handleChange}
      />

      <select name="type" value={filters.type} onChange={handleChange}>
        <option value="">All Types</option>
        <option value="income">Income</option>
        <option value="expense">Expense</option>
      </select>

      <input
        type="date"
        name="startDate"
        value={filters.startDate}
        onChange={handleChange}
      />
      <input
        type="date"
        name="endDate"
        value={filters.endDate}
        onChange={handleChange}
      />

      <button type="submit">Apply Filters</button>
    </form>
  );
};

export default TransactionFilterForm;
