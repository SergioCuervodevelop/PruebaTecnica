import { useEffect, useState } from "react";
import { getClientSummary } from "./services/api";

import ClientForm from "./components/ClientForm";
import ClientSearch from "./components/ClientSearch";
import AccountForm from "./components/AccountForm";
import AccountSearch from "./components/AccountSearch";
import TransactionForm from "./components/TransactionForm";
import TransactionHistory from "./components/TransactionHistory";

import "./App.css";

function App() {
  const [section, setSection] = useState("home");

  const [summary, setSummary] = useState({
    totalClients: 0,
    totalAccounts: 0,
    clients: [],
  });

  const [apiOnline, setApiOnline] = useState(false);

  const loadSummary = async () => {
    try {
      const data = await getClientSummary();

      setSummary(data);
      setApiOnline(true);
    } catch (error) {
      console.error("Error conectando con la API:", error);
      setApiOnline(false);
    }
  };

  useEffect(() => {
    loadSummary();
  }, []);

  return (
    <div className="app">
      <aside className="sidebar">
        <div className="logo">
          BankAPI
        </div>

        <div className="logo-subtitle">
          Gestión financiera
        </div>

        <nav className="navigation">
          <button
            className={section === "home" ? "active" : ""}
            onClick={() => setSection("home")}
          >
            Inicio
          </button>

          <button
            className={section === "clients" ? "active" : ""}
            onClick={() => setSection("clients")}
          >
            Clientes
          </button>

          <button
            className={section === "accounts" ? "active" : ""}
            onClick={() => setSection("accounts")}
          >
            Cuentas
          </button>

          <button
            className={section === "transactions" ? "active" : ""}
            onClick={() => setSection("transactions")}
          >
            Transacciones
          </button>
        </nav>
      </aside>

      <main className="content">
        {section === "home" && (
          <>
            <div className="page-header">
              <h1>Panel principal</h1>

              <p>
                Resumen general del sistema financiero.
              </p>
            </div>

            <div className="cards">
              <div className="card">
                <div className="card-title">
                  Clientes registrados
                </div>

                <div className="card-value">
                  {summary.totalClients}
                </div>
              </div>

              <div className="card">
                <div className="card-title">
                  Cuentas registradas
                </div>

                <div className="card-value">
                  {summary.totalAccounts}
                </div>
              </div>

              <div className="card">
                <div className="card-title">
                  Estado de la API
                </div>

                <div
                  className={`card-value api-status ${
                    apiOnline ? "online" : "offline"
                  }`}
                >
                  {apiOnline ? "Online" : "Offline"}
                </div>
              </div>
            </div>

            <div className="panel">
              <h2>BankAPI</h2>

              <p>
                Desde este panel puedes administrar clientes,
                productos financieros y transacciones.
              </p>
            </div>
          </>
        )}

        {section === "clients" && (
          <>
            <div className="page-header">
              <h1>Clientes</h1>

              <p>
                Registra, consulta, modifica y elimina clientes.
              </p>
            </div>

            <div className="panel">
              <ClientForm onClientCreated={loadSummary} />
            </div>

            <div className="panel">
              <ClientSearch />
            </div>
          </>
        )}

        {section === "accounts" && (
          <>
            <div className="page-header">
              <h1>Cuentas</h1>

              <p>
                Crea y administra los productos financieros
                de los clientes.
              </p>
            </div>

            <div className="panel">
              <AccountForm onAccountCreated={loadSummary} />
            </div>

            <div className="panel">
              <AccountSearch />
            </div>
          </>
        )}

        {section === "transactions" && (
          <>
            <div className="page-header">
              <h1>Transacciones</h1>

              <p>
                Realiza depósitos, retiros y transferencias
                entre cuentas.
              </p>
            </div>

            <div className="panel">
              <TransactionForm />
            </div>

            <div className="panel">
              <TransactionHistory />
            </div>
          </>
        )}
      </main>
    </div>
  );
}

export default App;