const API_URL = "http://localhost:8080/api";

export async function getClientSummary() {
  const response = await fetch(`${API_URL}/clients/summary`);

  if (!response.ok) {
    throw new Error("No se pudo obtener el resumen de clientes");
  }

  return response.json();
}

export async function createClient(client) {
  const response = await fetch(`${API_URL}/clients`, {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify(client),
  });

  if (!response.ok) {
    const message = await response.text();
    throw new Error(message || "No se pudo crear el cliente");
  }

  return response.json();
}

export async function getClientByIdentificationNumber(
  identificationNumber
) {
  const response = await fetch(
    `${API_URL}/clients/${identificationNumber}`
  );

  if (!response.ok) {
    const message = await response.text();
    throw new Error(message || "No se pudo encontrar el cliente");
  }

  return response.json();
}

export async function updateClient(
  identificationNumber,
  client
) {
  const response = await fetch(
    `${API_URL}/clients/${identificationNumber}`,
    {
      method: "PUT",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify(client),
    }
  );

  if (!response.ok) {
    const message = await response.text();
    throw new Error(message || "No se pudo actualizar el cliente");
  }

  return response.json();
}

export async function deleteClient(identificationNumber) {
  const response = await fetch(
    `${API_URL}/clients/${identificationNumber}`,
    {
      method: "DELETE",
    }
  );

  if (!response.ok) {
    const message = await response.text();
    throw new Error(message || "No se pudo eliminar el cliente");
  }
}

export async function createAccount(account) {
  const response = await fetch(`${API_URL}/accounts`, {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify(account),
  });

  if (!response.ok) {
    const message = await response.text();
    throw new Error(message || "No se pudo crear la cuenta");
  }

  return response.json();
}

export async function getAccountByNumber(accountNumber) {
  const response = await fetch(
    `${API_URL}/accounts/${accountNumber}`
  );

  if (!response.ok) {
    const message = await response.text();
    throw new Error(message || "No se pudo encontrar la cuenta");
  }

  return response.json();
}

export async function getAccountsByClient(
  identificationNumber
) {
  const response = await fetch(
    `${API_URL}/accounts/client/${identificationNumber}`
  );

  if (!response.ok) {
    const message = await response.text();
    throw new Error(
      message || "No se pudieron obtener las cuentas del cliente"
    );
  }

  return response.json();
}

export async function changeAccountStatus(
  accountNumber,
  status
) {
  const response = await fetch(
    `${API_URL}/accounts/${accountNumber}/status?status=${encodeURIComponent(
      status
    )}`,
    {
      method: "PATCH",
    }
  );

  if (!response.ok) {
    const message = await response.text();
    throw new Error(
      message || "No se pudo cambiar el estado de la cuenta"
    );
  }

  return response.json();
}

export async function cancelAccount(accountNumber) {
  const response = await fetch(
    `${API_URL}/accounts/${accountNumber}`,
    {
      method: "DELETE",
    }
  );

  if (!response.ok) {
    const message = await response.text();
    throw new Error(message || "No se pudo cancelar la cuenta");
  }

  return response.json();
}

export async function createTransaction(transaction) {
  const response = await fetch(`${API_URL}/transactions`, {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify(transaction),
  });

  if (!response.ok) {
    const message = await response.text();
    throw new Error(message || "No se pudo realizar la transacción");
  }

  return response.json();
}

export async function transferMoney(transfer) {
  const response = await fetch(
    `${API_URL}/transactions/transfer`,
    {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify(transfer),
    }
  );

  if (!response.ok) {
    const message = await response.text();
    throw new Error(
      message || "No se pudo realizar la transferencia"
    );
  }

  return response.json();
}

export async function getTransactionsByAccount(
  accountNumber
) {
  const response = await fetch(
    `${API_URL}/transactions/account/${accountNumber}`
  );

  if (!response.ok) {
    const message = await response.text();
    throw new Error(
      message || "No se pudieron obtener los movimientos"
    );
  }

  return response.json();
}