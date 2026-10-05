const messages = document.getElementById("messages");
const form = document.getElementById("composer");
const input = document.getElementById("body");
const online = document.getElementById("online");
const socket = new WebSocket((location.protocol === "https:" ? "wss://" : "ws://") + location.host + "/chat");

async function refreshOnline() {
    const response = await fetch("/online");
    if (response.ok) {
        online.textContent = await response.text();
    }
}

refreshOnline();
setInterval(refreshOnline, 2000);

socket.addEventListener("message", (event) => {
    const empty = document.getElementById("empty");
    if (empty) {
        empty.remove();
    }
    const line = document.createElement("p");
    line.className = "mb-2";
    line.textContent = event.data;
    messages.appendChild(line);
});

form.addEventListener("submit", (event) => {
    event.preventDefault();
    const body = input.value.trim();
    if (!body || socket.readyState !== WebSocket.OPEN) {
        return;
    }
    socket.send(body);
    input.value = "";
});
