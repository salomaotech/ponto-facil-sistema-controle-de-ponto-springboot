function criarHeader() {
    const nav = document.createElement("nav");
    nav.className = "nav-bar";

    // Só flexbox para posicionar e aumentar fonte do relógio, sem mudar fundo
    nav.style.display = "flex";
    nav.style.alignItems = "center";
    nav.style.justifyContent = "space-between";
    nav.style.padding = "10px 20px";

    nav.innerHTML = `
        <div class="logo">
            <a href="/"><img src="/img/logotipo.png" class="logo-topo"></a>
        </div>
        <div id="relogio" style="color: white; font-weight: bold; font-family: monospace; font-size: 24px;"></div>
    `;

    const header = document.querySelector("header");

    if (header) {
        header.appendChild(nav);

        const relogio = document.getElementById("relogio");

        function atualizarRelogio() {
            const agora = new Date();
            const horas = String(agora.getHours()).padStart(2, '0');
            const minutos = String(agora.getMinutes()).padStart(2, '0');
            const segundos = String(agora.getSeconds()).padStart(2, '0');
            relogio.textContent = `${horas}:${minutos}:${segundos}`;
        }

        atualizarRelogio();
        setInterval(atualizarRelogio, 1000);
    } else {
        console.error("Elemento <header> não encontrado.");
    }
}

document.addEventListener("DOMContentLoaded", criarHeader);
