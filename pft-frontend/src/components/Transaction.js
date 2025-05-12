import React, { useEffect, useState } from 'react';
import api from '../api/api';

function Transactions() {
  const [transactions, setTransactions] = useState([]);

  useEffect(() => {
    const token = localStorage.getItem('token');

    api.get('/transactions', {
      headers: {
        Authorization: `Bearer ${token}`,
      },
    }).then(res => {
      setTransactions(res.data);
    }).catch(err => {
      console.error(err);
    });
  }, []);

  return (
    <div>
      <h2>Transactions</h2>
      <ul>
        {transactions.map(t => (
          <li key={t.id}>{t.date} - {t.category} - {t.amount}</li>
        ))}
      </ul>
    </div>
  );
}

export default Transactions;
