function criarFooter() {

    const footer = document.querySelector("footer");
    if (footer) {

        footer.textContent = "versão 0.0.1";

    } else {

        console.error("Elemento <footer> não encontrado.");

    }

}

document.addEventListener("DOMContentLoaded", criarFooter);