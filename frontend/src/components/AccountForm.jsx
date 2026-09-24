import { useState } from "react";
import { createAccount } from "../services/api";

function AccountForm({ onAccountCreated }) {
  const [formData, setFormData] = useState({
    identificationType: "CC",
    identificationNumber: "",
    accountType: "SAVINGS",
  });

  const [createdAccount, setCreatedAccount] = useState(null);

  const [createdClient, setCreatedClient] = useState(null);

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

  const handleSubmit = async (event) => {
    event.preventDefault();

    setLoading(true);
    setMessage("");
    setMessageType("");
    setCreatedAccount(null);
    setCreatedClient(null);

    try {
      const account = await createAccount({
        accountType: formData.accountType,
        identificationType: formData.identificationType,
        identificationNumber: formData.identificationNumber.trim(),
      });

      setCreatedAccount(account);

      setCreatedClient({
        identificationType: formData.identificationType,
        identificationNumber: formData.identificationNumber.trim(),
      });

      setMessage("Cuenta creada correctamente.");
      setMessageType("success");

      setFormData({
        identificationType: "CC",
        identificationNumber: "",
        accountType: "SAVINGS",
      });

      if (onAccountCreated) {
        onAccountCreated();
      }
    } catch (error) {
      console.error(error);

      setMessage(
        error.message ||
          "No se pudo crear la cuenta. Verifica el cliente y los datos ingresados.",
      );

      setMessageType("error");
    } finally {
      setLoading(false);
    }
  };

  const getIdentificationTypeLabel = (type) => {
    switch (type) {
      case "CC":
        return "Cédula de ciudadanía";
      case "CE":
        return "Cédula de extranjería";
      case "PA":
        return "Pasaporte";
      default:
        return type;
    }
  };

  return (
    <div className="account-form-container">
      <div className="form-header">
        <h2>Crear cuenta</h2>

        <p>Crea un producto financiero para un cliente registrado.</p>
      </div>

      <form className="client-form" onSubmit={handleSubmit}>
        <div className="form-grid">
          <div className="form-group">
            <label htmlFor="identificationType">Tipo de identificación</label>

            <select
              id="identificationType"
              name="identificationType"
              value={formData.identificationType}
              onChange={handleChange}
              required
            >
              <option value="CC">Cédula de ciudadanía</option>

              <option value="CE">Cédula de extranjería</option>

              <option value="PA">Pasaporte</option>
            </select>
          </div>

          <div className="form-group">
            <label htmlFor="identificationNumber">
              Número de identificación
            </label>

            <input
              id="identificationNumber"
              name="identificationNumber"
              type="text"
              value={formData.identificationNumber}
              onChange={handleChange}
              placeholder="Ej: 1075000000"
              required
            />
          </div>

          <div className="form-group">
            <label htmlFor="accountType">Tipo de cuenta</label>

            <select
              id="accountType"
              name="accountType"
              value={formData.accountType}
              onChange={handleChange}
              required
            >
              <option value="SAVINGS">Cuenta de ahorros</option>

              <option value="CHECKING">Cuenta corriente</option>
            </select>
          </div>
        </div>

        {message && (
          <div className={`form-message ${messageType}`}>{message}</div>
        )}

        <div className="form-actions">
          <button type="submit" className="primary-button" disabled={loading}>
            {loading ? "Creando..." : "Crear cuenta"}
          </button>
        </div>
      </form>

      {createdAccount && createdClient && (
        <div className="client-result">
          <div className="client-result-header">
            <div>
              <span className="client-id">Número de cuenta</span>

              <h3>{createdAccount.accountNumber}</h3>
            </div>

            <span className="status-badge">{createdAccount.status}</span>
          </div>

          <div className="client-details">
            <div>
              <span>Tipo de cuenta</span>

              <strong>
                {createdAccount.accountType === "SAVINGS"
                  ? "Cuenta de ahorros"
                  : "Cuenta corriente"}
              </strong>
            </div>

            <div>
              <span>Saldo</span>

              <strong>
                ${Number(createdAccount.balance || 0).toLocaleString("es-CO")}
              </strong>
            </div>

            <div>
              <span>Saldo disponible</span>

              <strong>
                $
                {Number(createdAccount.availableBalance || 0).toLocaleString(
                  "es-CO",
                )}
              </strong>
            </div>

            <div>
              <span>Tipo de identificación</span>

              <strong>
                {getIdentificationTypeLabel(createdClient.identificationType)}
              </strong>
            </div>

            <div>
              <span>Número de identificación</span>

              <strong>{createdClient.identificationNumber}</strong>
            </div>
          </div>
        </div>
      )}
    </div>
  );
}

export default AccountForm;
