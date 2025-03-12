async function getAccount() {
    let id = document.getElementById("accountId").value;
    let response = await fetch(`/api/accounts/${id}`);
    let data = await response.json();
    document.getElementById("balance").innerText = `Баланс: ${data.balance}`;
}

async function deposit() {
    let id = document.getElementById("accountId").value;
    let amount = document.getElementById("amount").value;
    await fetch(`/api/accounts/${id}/deposit?amount=${amount}`, { method: "POST" });
    getAccount();
}

async function withdraw() {
    let id = document.getElementById("accountId").value;
    let amount = document.getElementById("amount").value;
    await fetch(`/api/accounts/${id}/withdraw?amount=${amount}`, { method: "POST" });
    getAccount();
}
