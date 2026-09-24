import { useState } from "react";
import { getTransactionsByAccount } from "../services/api";

function TransactionHistory() {
  const [accountNumber, setAccountNumber] = useState("");
  const [transactions, setTransactions] = useState([]);
  const [searched, setSearched] = useState(false);
  const [loading, setLoading] = useState(false);
  const [message, setMessage] = useState("");

  const formatMoney = (value) => {
    if (value === null || value === undefined) {
      return "$0";
    }

    return new Intl.NumberFormat("es-CO", {
      style: "currency",
      currency: "COP",
      minimumFractionDigits: 0,
    }).format(Number(value));
  };

  const formatDate = (value) => {
    if (!value) {
      return "Sin fecha";
    }

    return new Date(value).toLocaleString("es-CO");
  };

  const getTransactionName = (type) => {
    switch (type) {
      case "DEPOSIT":
        return "Depósito";

      case "WITHDRAWAL":
        return "Retiro";

      case "TRANSFER":
        return "Transferencia";

      default:
        return type;
    }
  };

  const handleSearch = async (event) => {
    event.preventDefault();

    if (!accountNumber.trim()) {
      return;
    }

    try {
      setLoading(true);
      setMessage("");
      setTransactions([]);
      setSearched(false);

      const data = await getTransactionsByAccount(
        accountNumber.trim()
      );

      setTransactions(
        Array.isArray(data) ? data : []
      );

      setSearched(true);
    } catch (error) {
      console.error(error);

      setTransactions([]);
      setSearched(true);

      setMessage(
        error.message ||
          "No se pudo consultar el historial de movimientos."
      );
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="transaction-history-container">
      <div className="form-header">
        <h2>Historial de movimientos</h2>

        <p>
          Consulta los depósitos, retiros y
          transferencias registrados en una cuenta.
        </p>
      </div>

      <form
        className="history-search-form"
        onSubmit={handleSearch}
      >
        <div className="search-input">
          <label htmlFor="historyAccountNumber">
            Número de cuenta
          </label>

          <input
            id="historyAccountNumber"
            type="text"
            value={accountNumber}
            onChange={(event) =>
              setAccountNumber(event.target.value)
            }
            placeholder="Ej. 5312345678"
            required
          />
        </div>

        <button
          type="submit"
          className="primary-button"
          disabled={loading}
        >
          {loading
            ? "Consultando..."
            : "Consultar movimientos"}
        </button>
      </form>

      {message && (
        <div className="form-message error">
          {message}
        </div>
      )}

      {searched &&
        !message &&
        transactions.length === 0 && (
          <div className="empty-history">
            <strong>Sin movimientos</strong>

            <p>
              Esta cuenta todavía no tiene
              transacciones registradas.
            </p>
          </div>
        )}

      {transactions.length > 0 && (
        <>
          <div className="history-summary">
            <span>
              Cuenta consultada
              <strong>{accountNumber}</strong>
            </span>

            <span>
              Movimientos
              <strong>
                {transactions.length}
              </strong>
            </span>
          </div>

          <div className="table-container history-table-container">
            <table className="clients-table history-table">
              <thead>
                <tr>
                  <th>Cuenta</th>
                  <th>Tipo</th>
                  <th>Movimiento</th>
                  <th>Valor</th>
                  <th>Fecha</th>
                  <th>Transferencia</th>
                </tr>
              </thead>

              <tbody>
                {transactions.map(
                  (transaction, index) => (
                    <tr
                      key={`${transaction.transactionType}-${transaction.transactionDate}-${index}`}
                    >
                      <td>
                        {transaction.accountNumber}
                      </td>

                      <td>
                        <span
                          className={`transaction-badge ${
                            transaction.transactionType
                              ?.toLowerCase()
                          }`}
                        >
                          {getTransactionName(
                            transaction.transactionType
                          )}
                        </span>
                      </td>

                      <td>
                        {transaction.movementType ===
                        "CREDIT"
                          ? "Crédito"
                          : "Débito"}
                      </td>

                      <td className="transaction-amount">
                        {formatMoney(
                          transaction.amount
                        )}
                      </td>

                      <td>
                        {formatDate(
                          transaction.transactionDate
                        )}
                      </td>

                      <td className="transfer-id">
                        {transaction.transferId ||
                          "-"}
                      </td>
                    </tr>
                  )
                )}
              </tbody>
            </table>
          </div>
        </>
      )}
    </div>
  );
}

export default TransactionHistory;