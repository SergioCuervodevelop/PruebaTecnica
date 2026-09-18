import { useState } from "react";
import { createClient } from "../services/api";

function ClientForm({ onClientCreated }) {
  const [formData, setFormData] = useState({
    identificationType: "CC",
    identificationNumber: "",
    firstName: "",
    lastName: "",
    email: "",
    birthDate: "",
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

  const handleSubmit = async (event) => {
    event.preventDefault();

    setLoading(true);
    setMessage("");
    setMessageType("");

    try {
      await createClient(formData);

      setMessage("Cliente creado correctamente.");
      setMessageType("success");

      setFormData({
        identificationType: "CC",
        identificationNumber: "",
        firstName: "",
        lastName: "",
        email: "",
        birthDate: "",
      });

      if (onClientCreated) {
        onClientCreated();
      }
    } catch (error) {
      setMessage(error.message);
      setMessageType("error");
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="client-form-container">
      <div className="form-header">
        <p>Registra un nuevo cliente en el sistema financiero.</p>
      </div>

      <form className="client-form" onSubmit={handleSubmit}>
        <div className="form-grid">
          <div className="form-group">
            <label htmlFor="identificationType">
              Tipo de identificación
            </label>

            <select
              id="identificationType"
              name="identificationType"
              value={formData.identificationType}
              onChange={handleChange}
              required
            >
              <option value="CC">Cédula de ciudadanía</option>
              <option value="TI">Tarjeta de identidad</option>
              <option value="PA">Pasaporte</option>
              <option value="CE">Cédula de extranjería</option>
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
              placeholder="Ej. 1075000000"
              required
            />
          </div>

          <div className="form-group">
            <label htmlFor="firstName">Nombres</label>

            <input
              id="firstName"
              name="firstName"
              type="text"
              value={formData.firstName}
              onChange={handleChange}
              placeholder="Ej. Sergio"
              minLength="2"
              required
            />
          </div>

          <div className="form-group">
            <label htmlFor="lastName">Apellidos</label>

            <input
              id="lastName"
              name="lastName"
              type="text"
              value={formData.lastName}
              onChange={handleChange}
              placeholder="Ej. Cuervo"
              minLength="2"
              required
            />
          </div>

          <div className="form-group">
            <label htmlFor="email">Correo electrónico</label>

            <input
              id="email"
              name="email"
              type="email"
              value={formData.email}
              onChange={handleChange}
              placeholder="correo@ejemplo.com"
            />
          </div>

          <div className="form-group">
            <label htmlFor="birthDate">Fecha de nacimiento</label>

            <input
              id="birthDate"
              name="birthDate"
              type="date"
              value={formData.birthDate}
              onChange={handleChange}
              required
            />
          </div>
        </div>

        {message && (
          <div className={`form-message ${messageType}`}>
            {message}
          </div>
        )}

        <div className="form-actions">
          <button
            className="primary-button"
            type="submit"
            disabled={loading}
          >
            {loading ? "Creando..." : "Crear cliente"}
          </button>
        </div>
      </form>
    </div>
  );
}

export default ClientForm;