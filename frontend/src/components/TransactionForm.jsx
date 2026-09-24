import { useState } from "react";
import {
  createTransaction,
  transferMoney,
} from "../services/api";

function TransactionForm() {
  const [operation, setOperation] = useState("DEPOSIT");

  const [formData, setFormData] = useState({
    accountNumber: "",
    sourceAccountNumber: "",
    destinationAccountNumber: "",
    amount: "",
  });

  const [message, setMessage] = useState("");
  const [messageType, setMessageType] = useState("");
  const [loading, setLoading] = useState(false);

  const handleChange = (event) => {
    const { name, value } = event.target;

    setFormData((previousData) => ({
      ...previousData,
      [name]: value,
    }));
  };

  const handleOperationChange = (event) => {
    setOperation(event.target.value);
    setMessage("");
    setMessageType("");

    setFormData({
      accountNumber: "",
      sourceAccountNumber: "",
      destinationAccountNumber: "",
      amount: "",
    });
  };

  const handleSubmit = async (event) => {
    event.preventDefault();

    setLoading(true);
    setMessage("");
    setMessageType("");

    try {
      if (operation === "TRANSFER") {
        await transferMoney({
          sourceAccountNumber:
            formData.sourceAccountNumber.trim(),

          destinationAccountNumber:
            formData.destinationAccountNumber.trim(),

          amount: Number(formData.amount),
        });

        setMessage(
          "Transferencia realizada correctamente."
        );
      } else {
        await createTransaction({
          accountNumber:
            formData.accountNumber.trim(),

          transactionType: operation,

          amount: Number(formData.amount),
        });

        if (operation === "DEPOSIT") {
          setMessage(
            "Depósito realizado correctamente."
          );
        }

        if (operation === "WITHDRAWAL") {
          setMessage(
            "Retiro realizado correctamente."
          );
        }
      }

      setMessageType("success");

      setFormData({
        accountNumber: "",
        sourceAccountNumber: "",
        destinationAccountNumber: "",
        amount: "",
      });
    } catch (error) {
      console.error(error);

      setMessage(
        error.message ||
          "No se pudo realizar la operación."
      );

      setMessageType("error");
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="transaction-form-container">
      <div className="form-header">
        <h2>Realizar transacción</h2>

        <p>
          Realiza depósitos, retiros y transferencias
          entre cuentas.
        </p>
      </div>

      <form
        className="client-form"
        onSubmit={handleSubmit}
      >
        <div className="form-grid">
          <div className="form-group">
            <label htmlFor="operation">
              Tipo de operación
            </label>

            <select
              id="operation"
              value={operation}
              onChange={handleOperationChange}
            >
              <option value="DEPOSIT">
                Depósito
              </option>

              <option value="WITHDRAWAL">
                Retiro
              </option>

              <option value="TRANSFER">
                Transferencia
              </option>
            </select>
          </div>

          {operation !== "TRANSFER" && (
            <div className="form-group">
              <label htmlFor="accountNumber">
                Número de cuenta
              </label>

              <input
                id="accountNumber"
                name="accountNumber"
                type="text"
                value={formData.accountNumber}
                onChange={handleChange}
                placeholder="Ej. 5312345678"
                required
              />
            </div>
          )}

          {operation === "TRANSFER" && (
            <>
              <div className="form-group">
                <label htmlFor="sourceAccountNumber">
                  Cuenta de origen
                </label>

                <input
                  id="sourceAccountNumber"
                  name="sourceAccountNumber"
                  type="text"
                  value={
                    formData.sourceAccountNumber
                  }
                  onChange={handleChange}
                  placeholder="Número de cuenta origen"
                  required
                />
              </div>

              <div className="form-group">
                <label htmlFor="destinationAccountNumber">
                  Cuenta de destino
                </label>

                <input
                  id="destinationAccountNumber"
                  name="destinationAccountNumber"
                  type="text"
                  value={
                    formData.destinationAccountNumber
                  }
                  onChange={handleChange}
                  placeholder="Número de cuenta destino"
                  required
                />
              </div>
            </>
          )}

          <div className="form-group">
            <label htmlFor="transactionAmount">
              Valor
            </label>

            <input
              id="transactionAmount"
              name="amount"
              type="number"
              min="0.01"
              step="0.01"
              value={formData.amount}
              onChange={handleChange}
              placeholder="Ej. 50000"
              required
            />
          </div>
        </div>

        {message && (
          <div
            className={`form-message ${messageType}`}
          >
            {message}
          </div>
        )}

        <div className="form-actions">
          <button
            type="submit"
            className="primary-button"
            disabled={loading}
          >
            {loading
              ? "Procesando..."
              : operation === "DEPOSIT"
              ? "Realizar depósito"
              : operation === "WITHDRAWAL"
              ? "Realizar retiro"
              : "Realizar transferencia"}
          </button>
        </div>
      </form>
    </div>
  );
}

export default TransactionForm;