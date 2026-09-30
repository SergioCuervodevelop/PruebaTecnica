import { useEffect, useRef, useState } from "react";

import {
  getClientSummary,
  getClientByIdentification,
  getAccountsByClient,
  updateClient,
  deleteClient,
  restoreClient,
} from "../services/api";

function ClientSearch({ refreshClients }) {
  const [clients, setClients] = useState([]);
  const [selectedClient, setSelectedClient] = useState(null);
  const [clientAccounts, setClientAccounts] = useState([]);

  const [editData, setEditData] = useState({
    firstName: "",
    lastName: "",
    email: "",
  });

  const [editing, setEditing] = useState(false);
  const [loading, setLoading] = useState(true);

  const [loadingIdentification, setLoadingIdentification] =
    useState(null);

  const [restoringIdentification, setRestoringIdentification] =
    useState(null);

  const [message, setMessage] = useState("");
  const [messageType, setMessageType] = useState("");

  const clientDetailRef = useRef(null);

  const loadClients = async () => {
    try {
      setLoading(true);

      const data = await getClientSummary();

      setClients(data.clients || []);
    } catch (error) {
      console.error(error);

      setMessage(
        error.message ||
          "No se pudieron cargar los clientes."
      );

      setMessageType("error");
    } finally {
      setLoading(false);
    }
  };

  // Se ejecuta al cargar el componente
  // y también cuando se crea un cliente nuevo.
  useEffect(() => {
    loadClients();
  }, [refreshClients]);

  useEffect(() => {
    if (selectedClient && clientDetailRef.current) {
      clientDetailRef.current.scrollIntoView({
        behavior: "smooth",
        block: "start",
      });
    }
  }, [selectedClient]);

  const handleView = async (
    identificationType,
    identificationNumber
  ) => {
    try {
      setMessage("");
      setMessageType("");
      setEditing(false);

      const identificationKey =
        `${identificationType}-${identificationNumber}`;

      setLoadingIdentification(identificationKey);

      setClientAccounts([]);

      const [client, accounts] = await Promise.all([
        getClientByIdentification(
          identificationType,
          identificationNumber
        ),

        getAccountsByClient(
          identificationType,
          identificationNumber
        ),
      ]);

      setSelectedClient(client);

      setClientAccounts(
        Array.isArray(accounts) ? accounts : []
      );

      setEditData({
        firstName: client.firstName || "",
        lastName: client.lastName || "",
        email: client.email || "",
      });
    } catch (error) {
      console.error(error);

      setSelectedClient(null);
      setClientAccounts([]);

      setMessage(
        error.message ||
          "No se pudo consultar el cliente."
      );

      setMessageType("error");
    } finally {
      setLoadingIdentification(null);
    }
  };

  const handleEditChange = (event) => {
    const { name, value } = event.target;

    setEditData((previousData) => ({
      ...previousData,
      [name]: value,
    }));
  };

  const handleUpdate = async (event) => {
    event.preventDefault();

    if (!selectedClient) {
      return;
    }

    try {
      setMessage("");
      setMessageType("");

      const updatedClient = await updateClient(
        selectedClient.identificationType,
        selectedClient.identificationNumber,
        editData
      );

      setSelectedClient(updatedClient);
      setEditing(false);

      setMessage(
        "Cliente actualizado correctamente."
      );

      setMessageType("success");

      await loadClients();
    } catch (error) {
      console.error(error);

      setMessage(
        error.message ||
          "No se pudo actualizar el cliente."
      );

      setMessageType("error");
    }
  };

  const handleDelete = async () => {
    if (!selectedClient) {
      return;
    }

    const confirmed = window.confirm(
      `¿Seguro que deseas eliminar a ${selectedClient.firstName} ${selectedClient.lastName}?`
    );

    if (!confirmed) {
      return;
    }

    try {
      setMessage("");
      setMessageType("");

      await deleteClient(
        selectedClient.identificationType,
        selectedClient.identificationNumber
      );

      setSelectedClient(null);
      setClientAccounts([]);
      setEditing(false);

      setMessage(
        "Cliente eliminado correctamente. Puedes restaurarlo posteriormente."
      );

      setMessageType("success");

      await loadClients();
    } catch (error) {
      console.error(error);

      setMessage(
        error.message ||
          "No se pudo eliminar el cliente. Verifica si tiene cuentas asociadas."
      );

      setMessageType("error");
    }
  };

  const handleRestore = async (client) => {
    if (!client) {
      return;
    }

    const confirmed = window.confirm(
      `¿Deseas restaurar a ${client.firstName} ${client.lastName}?`
    );

    if (!confirmed) {
      return;
    }

    const identificationKey =
      `${client.identificationType}-${client.identificationNumber}`;

    try {
      setMessage("");
      setMessageType("");
      setRestoringIdentification(identificationKey);

      const restoredClient = await restoreClient(
        client.identificationType,
        client.identificationNumber
      );

      setMessage(
        "Cliente restaurado correctamente."
      );

      setMessageType("success");

      if (
        selectedClient &&
        selectedClient.identificationType ===
          client.identificationType &&
        selectedClient.identificationNumber ===
          client.identificationNumber
      ) {
        setSelectedClient(restoredClient);

        setEditData({
          firstName: restoredClient.firstName || "",
          lastName: restoredClient.lastName || "",
          email: restoredClient.email || "",
        });
      }

      await loadClients();
    } catch (error) {
      console.error(error);

      setMessage(
        error.message ||
          "No se pudo restaurar el cliente."
      );

      setMessageType("error");
    } finally {
      setRestoringIdentification(null);
    }
  };

  const handleCancelEdit = () => {
    if (!selectedClient) {
      return;
    }

    setEditing(false);

    setEditData({
      firstName: selectedClient.firstName || "",
      lastName: selectedClient.lastName || "",
      email: selectedClient.email || "",
    });
  };

  const handleCloseDetail = () => {
    setSelectedClient(null);
    setClientAccounts([]);
    setEditing(false);
    setMessage("");
    setMessageType("");
  };

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

  const getAccountTypeName = (type) => {
    if (type === "SAVINGS") {
      return "Ahorros";
    }

    if (type === "CHECKING") {
      return "Corriente";
    }

    return type || "-";
  };

  const getAccountStatusName = (status) => {
    if (status === "ACTIVE") {
      return "Activa";
    }

    if (status === "INACTIVE") {
      return "Inactiva";
    }

    if (status === "CANCELLED") {
      return "Cancelada";
    }

    return status || "-";
  };

  const getClientStatusName = (status) => {
    if (status === "ACTIVE") {
      return "Activo";
    }

    if (status === "DELETED") {
      return "Eliminado";
    }

    return status || "-";
  };

  return (
    <div className="client-search-container">
      <div className="form-header">
        <h2>Clientes registrados</h2>

        <p>
          Selecciona un cliente para consultar su
          información completa.
        </p>
      </div>

      {message && (
        <div
          className={`form-message ${messageType}`}
        >
          {message}
        </div>
      )}

      {loading ? (
        <p>Cargando clientes...</p>
      ) : clients.length === 0 ? (
        <p>No hay clientes registrados.</p>
      ) : (
        <div className="table-container">
          <table className="clients-table">
            <thead>
              <tr>
                <th>Identificación</th>
                <th>Cliente</th>
                <th>Cuentas</th>
                <th>Estado</th>
                <th>Acción</th>
              </tr>
            </thead>

            <tbody>
              {clients.map((client) => {
                const identificationKey =
                  `${client.identificationType}-${client.identificationNumber}`;

                const isDeleted =
                  client.status === "DELETED";

                return (
                  <tr key={identificationKey}>
                    <td>
                      <strong>
                        {client.identificationType}
                      </strong>{" "}
                      {client.identificationNumber}
                    </td>

                    <td>
                      {client.firstName}{" "}
                      {client.lastName}
                    </td>

                    <td>
                      {client.accountCount}
                    </td>

                    <td>
                      <span
                        className={`account-status ${
                          isDeleted
                            ? "cancelled"
                            : "active"
                        }`}
                      >
                        {getClientStatusName(
                          client.status
                        )}
                      </span>
                    </td>

                    <td>
                      <div className="client-actions">
                        <button
                          type="button"
                          className="table-action-button"
                          onClick={() =>
                            handleView(
                              client.identificationType,
                              client.identificationNumber
                            )
                          }
                          disabled={
                            loadingIdentification ===
                            identificationKey
                          }
                        >
                          {loadingIdentification ===
                          identificationKey
                            ? "Abriendo..."
                            : "Ver"}
                        </button>

                        {isDeleted && (
                          <button
                            type="button"
                            className="primary-button"
                            onClick={() =>
                              handleRestore(client)
                            }
                            disabled={
                              restoringIdentification ===
                              identificationKey
                            }
                          >
                            {restoringIdentification ===
                            identificationKey
                              ? "Restaurando..."
                              : "Restaurar"}
                          </button>
                        )}
                      </div>
                    </td>
                  </tr>
                );
              })}
            </tbody>
          </table>
        </div>
      )}

      {selectedClient && (
        <div
          className="client-result"
          ref={clientDetailRef}
        >
          <div className="client-result-header">
            <div>
              <span className="client-id">
                Identificación:{" "}
                {selectedClient.identificationType}{" "}
                {selectedClient.identificationNumber}
              </span>

              <h3>
                {selectedClient.firstName}{" "}
                {selectedClient.lastName}
              </h3>
            </div>

            <span
              className={`status-badge ${
                selectedClient.status === "DELETED"
                  ? "status-cancelled"
                  : ""
              }`}
            >
              {getClientStatusName(
                selectedClient.status
              )}
            </span>
          </div>

          {!editing ? (
            <>
              <div className="client-details">
                <div>
                  <span>
                    Tipo de identificación
                  </span>

                  <strong>
                    {
                      selectedClient.identificationType
                    }
                  </strong>
                </div>

                <div>
                  <span>
                    Número de identificación
                  </span>

                  <strong>
                    {
                      selectedClient.identificationNumber
                    }
                  </strong>
                </div>

                <div>
                  <span>Nombre</span>

                  <strong>
                    {selectedClient.firstName}
                  </strong>
                </div>

                <div>
                  <span>Apellido</span>

                  <strong>
                    {selectedClient.lastName}
                  </strong>
                </div>

                <div>
                  <span>
                    Correo electrónico
                  </span>

                  <strong>
                    {selectedClient.email ||
                      "No registrado"}
                  </strong>
                </div>

                <div>
                  <span>
                    Fecha de nacimiento
                  </span>

                  <strong>
                    {selectedClient.birthDate}
                  </strong>
                </div>

                <div>
                  <span>Estado</span>

                  <strong>
                    {getClientStatusName(
                      selectedClient.status
                    )}
                  </strong>
                </div>
              </div>

              <div className="client-accounts-section">
                <div className="form-header">
                  <h3>
                    Cuentas asociadas (
                    {clientAccounts.length})
                  </h3>

                  <p>
                    Productos financieros
                    pertenecientes a este cliente.
                  </p>
                </div>

                {clientAccounts.length === 0 ? (
                  <div className="empty-history">
                    <strong>
                      Sin cuentas asociadas
                    </strong>

                    <p>
                      Este cliente todavía no tiene
                      productos financieros
                      registrados.
                    </p>
                  </div>
                ) : (
                  <div className="table-container">
                    <table className="clients-table">
                      <thead>
                        <tr>
                          <th>
                            Número de cuenta
                          </th>

                          <th>Tipo</th>
                          <th>Saldo</th>
                          <th>Disponible</th>
                          <th>Estado</th>
                        </tr>
                      </thead>

                      <tbody>
                        {clientAccounts.map(
                          (account) => (
                            <tr
                              key={
                                account.accountNumber
                              }
                            >
                              <td>
                                <strong>
                                  {
                                    account.accountNumber
                                  }
                                </strong>
                              </td>

                              <td>
                                {getAccountTypeName(
                                  account.accountType
                                )}
                              </td>

                              <td>
                                {formatMoney(
                                  account.balance
                                )}
                              </td>

                              <td>
                                {formatMoney(
                                  account.availableBalance
                                )}
                              </td>

                              <td>
                                <span
                                  className={`account-status ${account.status?.toLowerCase()}`}
                                >
                                  {getAccountStatusName(
                                    account.status
                                  )}
                                </span>
                              </td>
                            </tr>
                          )
                        )}
                      </tbody>
                    </table>
                  </div>
                )}
              </div>

              <div className="client-actions">
                <button
                  type="button"
                  className="secondary-button"
                  onClick={handleCloseDetail}
                >
                  Cerrar
                </button>

                {selectedClient.status ===
                "ACTIVE" ? (
                  <>
                    <button
                      type="button"
                      className="secondary-button"
                      onClick={() =>
                        setEditing(true)
                      }
                    >
                      Editar
                    </button>

                    <button
                      type="button"
                      className="danger-button"
                      onClick={handleDelete}
                    >
                      Eliminar
                    </button>
                  </>
                ) : (
                  <button
                    type="button"
                    className="primary-button"
                    onClick={() =>
                      handleRestore(
                        selectedClient
                      )
                    }
                    disabled={
                      restoringIdentification ===
                      `${selectedClient.identificationType}-${selectedClient.identificationNumber}`
                    }
                  >
                    {restoringIdentification ===
                    `${selectedClient.identificationType}-${selectedClient.identificationNumber}`
                      ? "Restaurando..."
                      : "Restaurar cliente"}
                  </button>
                )}
              </div>
            </>
          ) : (
            <form
              className="client-form edit-client-form"
              onSubmit={handleUpdate}
            >
              <div className="form-grid">
                <div className="form-group">
                  <label htmlFor="editFirstName">
                    Nombre
                  </label>

                  <input
                    id="editFirstName"
                    name="firstName"
                    type="text"
                    value={
                      editData.firstName
                    }
                    onChange={
                      handleEditChange
                    }
                    minLength="2"
                    required
                  />
                </div>

                <div className="form-group">
                  <label htmlFor="editLastName">
                    Apellido
                  </label>

                  <input
                    id="editLastName"
                    name="lastName"
                    type="text"
                    value={
                      editData.lastName
                    }
                    onChange={
                      handleEditChange
                    }
                    minLength="2"
                    required
                  />
                </div>

                <div className="form-group">
                  <label htmlFor="editEmail">
                    Correo electrónico
                  </label>

                  <input
                    id="editEmail"
                    name="email"
                    type="email"
                    value={editData.email}
                    onChange={
                      handleEditChange
                    }
                  />
                </div>
              </div>

              <div className="client-actions">
                <button
                  type="button"
                  className="secondary-button"
                  onClick={handleCancelEdit}
                >
                  Cancelar
                </button>

                <button
                  type="submit"
                  className="primary-button"
                >
                  Guardar cambios
                </button>
              </div>
            </form>
          )}
        </div>
      )}
    </div>
  );
}

export default ClientSearch;