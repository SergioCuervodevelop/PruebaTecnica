import { useState } from "react";
import {
  getAccountByNumber,
  changeAccountStatus,
  cancelAccount,
} from "../services/api";

function AccountSearch() {
  const [accountNumber, setAccountNumber] = useState("");
  const [account, setAccount] = useState(null);
  const [error, setError] = useState("");
  const [message, setMessage] = useState("");
  const [loading, setLoading] = useState(false);

  const handleSearch = async (event) => {
    event.preventDefault();

    if (!accountNumber.trim()) {
      setError("Ingresa el número de la cuenta.");
      return;
    }

    setLoading(true);
    setError("");
    setMessage("");
    setAccount(null);

    try {
      const data = await getAccountByNumber(
        accountNumber.trim()
      );

      setAccount(data);
    } catch (error) {
      console.error(error);

      setError(
        error.message || "No se encontró la cuenta."
      );
    } finally {
      setLoading(false);
    }
  };

  const handleStatusChange = async (status) => {
    if (!account) {
      return;
    }

    setLoading(true);
    setError("");
    setMessage("");

    try {
      const updatedAccount =
        await changeAccountStatus(
          account.accountNumber,
          status
        );

      setAccount(updatedAccount);

      if (status === "ACTIVE") {
        setMessage("Cuenta activada correctamente.");
      }

      if (status === "INACTIVE") {
        setMessage("Cuenta inactivada correctamente.");
      }
    } catch (error) {
      console.error(error);

      setError(
        error.message ||
          "No se pudo cambiar el estado de la cuenta."
      );
    } finally {
      setLoading(false);
    }
  };

  const handleCancel = async () => {
    if (!account) {
      return;
    }

    const confirmed = window.confirm(
      `¿Seguro que deseas cancelar la cuenta ${account.accountNumber}?`
    );

    if (!confirmed) {
      return;
    }

    setLoading(true);
    setError("");
    setMessage("");

    try {
      const updatedAccount =
        await cancelAccount(account.accountNumber);

      setAccount(updatedAccount);

      setMessage(
        "Cuenta cancelada correctamente."
      );
    } catch (error) {
      console.error(error);

      setError(
        error.message ||
          "No se pudo cancelar la cuenta. Verifica que su saldo sea $0."
      );
    } finally {
      setLoading(false);
    }
  };

  const formatMoney = (value) => {
    return Number(value || 0).toLocaleString(
      "es-CO",
      {
        style: "currency",
        currency: "COP",
        minimumFractionDigits: 0,
      }
    );
  };

  const getStatusText = (status) => {
    if (status === "ACTIVE") {
      return "Activa";
    }

    if (status === "INACTIVE") {
      return "Inactiva";
    }

    if (status === "CANCELLED") {
      return "Cancelada";
    }

    return status;
  };

  return (
    <div className="account-search-container">
      <div className="form-header">
        <h2>Buscar cuenta</h2>

        <p>
          Consulta y administra un producto financiero
          utilizando el número de la cuenta.
        </p>
      </div>

      <form
        className="search-form"
        onSubmit={handleSearch}
      >
        <div className="search-input">
          <label htmlFor="accountNumber">
            Número de cuenta
          </label>

          <input
            id="accountNumber"
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
          {loading ? "Buscando..." : "Buscar"}
        </button>
      </form>

      {error && (
        <div className="form-message error">
          {error}
        </div>
      )}

      {message && (
        <div className="form-message success">
          {message}
        </div>
      )}

      {account && (
        <div className="client-result">
          <div className="client-result-header">
            <div>
              <span className="client-id">
                Número de cuenta
              </span>

              <h3>
                {account.accountNumber}
              </h3>
            </div>

            <span
              className={`status-badge ${
                account.status === "INACTIVE"
                  ? "status-inactive"
                  : account.status === "CANCELLED"
                  ? "status-cancelled"
                  : ""
              }`}
            >
              {getStatusText(account.status)}
            </span>
          </div>

          <div className="client-details">
            <div>
              <span>Tipo de cuenta</span>

              <strong>
                {account.accountType === "SAVINGS"
                  ? "Cuenta de ahorros"
                  : "Cuenta corriente"}
              </strong>
            </div>

            <div>
              <span>Saldo</span>

              <strong>
                {formatMoney(account.balance)}
              </strong>
            </div>

            <div>
              <span>Saldo disponible</span>

              <strong>
                {formatMoney(
                  account.availableBalance
                )}
              </strong>
            </div>

            <div>
              <span>Exenta de GMF</span>

              <strong>
                {account.gmfExempt ? "Sí" : "No"}
              </strong>
            </div>

            <div>
              <span>Estado</span>

              <strong>
                {getStatusText(account.status)}
              </strong>
            </div>
          </div>

          {account.status !== "CANCELLED" && (
            <div className="client-actions">
              {account.status === "ACTIVE" && (
                <button
                  type="button"
                  className="secondary-button"
                  onClick={() =>
                    handleStatusChange(
                      "INACTIVE"
                    )
                  }
                  disabled={loading}
                >
                  Inactivar
                </button>
              )}

              {account.status ===
                "INACTIVE" && (
                <button
                  type="button"
                  className="primary-button"
                  onClick={() =>
                    handleStatusChange(
                      "ACTIVE"
                    )
                  }
                  disabled={loading}
                >
                  Activar
                </button>
              )}

              <button
                type="button"
                className="danger-button"
                onClick={handleCancel}
                disabled={loading}
              >
                Cancelar cuenta
              </button>
            </div>
          )}
        </div>
      )}
    </div>
  );
}

export default AccountSearch;