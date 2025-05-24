function criarMenu() {
    const aside = document.createElement("aside");
    aside.className = "side-menu";

    aside.innerHTML = `
        <h2>Menu</h2>
        <ul>
            <li>
               
                <a href="#">⚙️ Gerenciamento</a>
                
                <ul>
                    <li><a href="/tela/painel_pesquisa_ponto">🖥️ Painel de Pontos</a></li>
                </ul>
                
                <ul>
                    <li><a href="/tela/cadastro_funcionario">👥 Cadastro de Funcionários</a></li>
                </ul>

                <ul>
                    <li><a href="/tela/cadastro_ponto">📌 Cadastro de Pontos</a></li>
                </ul>

                <ul>
                    <li><a href="/tela/cadastro_justificativa">📝 Cadastro de Justificativas</a></li>
                </ul>
        
                <ul>
                   <li><a href="/tela/pesquisa_relatorio">📄 Gerar Relatório de Pontos</a></li>
                </ul>

            </li>
   
            <li><a href="/tela/cadastro_turno">🕒 Cadastro de Turnos</a></li>

            <li>
                <a href="#">🔍 Consultas</a>

                <ul>
                    <li><a href="/tela/pesquisa_funcionario">🔎 Pesquisa de Funcionários</a></li>
                </ul>

                <ul>
                    <li><a href="/tela/pesquisa_ponto">🔎 Pesquisa de Pontos</a></li>
                </ul>

                <ul>
                    <li><a href="/tela/pesquisa_turno">🔎 Pesquisa de Turnos</a></li>
                </ul>

                <ul>
                    <li><a href="/tela/pesquisa_justificativa">🔎 Pesquisa de Justificativas</a></li>
                </ul>
         
            </li>
                 
            <li><a href="/tela/cadastro_usuario">🔑 Alterar Senha</a></li>
            <li><a href="/logout">🚪 Logout</a></li>

        </ul>
    `;

    const menuLateralDiv = document.getElementById("menu-lateral");

    if (menuLateralDiv) {
        menuLateralDiv.appendChild(aside);
    } else {
        console.error("Elemento <div id='menu-lateral'> não encontrado.");
    }
}

document.addEventListener("DOMContentLoaded", criarMenu);
