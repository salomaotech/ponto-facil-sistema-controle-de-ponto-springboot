function criarHeader() {

    const nav = document.createElement("nav");
    nav.className = "nav-bar";

    nav.innerHTML = `
        <div class="logo">
            <a href="/tela/home"><img src="/img/logotipo.png" class="logo-topo"></a>
        </div>
    `;

    const header = document.querySelector("header");

    if (header) {
        header.appendChild(nav);
    } else {
        console.error("Elemento <header> não encontrado.");
    }

}

document.addEventListener("DOMContentLoaded", criarHeader);
