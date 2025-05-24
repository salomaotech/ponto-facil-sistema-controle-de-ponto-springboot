package com.ana.coutinho.ponto.controller;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.ana.coutinho.ponto.model.Funcionarios;
import com.ana.coutinho.ponto.model.Justificativa;
import com.ana.coutinho.ponto.model.Ponto;
import com.ana.coutinho.ponto.model.Turnos;
import com.ana.coutinho.ponto.model.Usuario;
import com.ana.coutinho.ponto.repository.FuncionariosRepository;
import com.ana.coutinho.ponto.repository.JustificativaRepository;
import com.ana.coutinho.ponto.repository.PontoRepository;
import com.ana.coutinho.ponto.repository.TurnosRepository;
import com.ana.coutinho.ponto.services.CalculaHoras;
import com.ana.coutinho.ponto.services.FileUploadService;
import com.ana.coutinho.ponto.services.RelatorioHorasTrabalhadasPdf;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/tela")
public class ViewController {

    @Autowired
    private FuncionariosRepository funcionariosRepository;

    @Autowired
    private TurnosRepository turnosRepository;

    @Autowired
    private JustificativaRepository justificativaRepository;

    @Autowired
    private PontoRepository pontoRepository;

    @Autowired
    private FileUploadService fileUploadService;

    @GetMapping("/home")
    public ModelAndView home(HttpSession session) {

        if (session.getAttribute("usuarioLogado") == null) {

            // Usuário NÃO logado
            return new ModelAndView("redirect:/tela/login");

        } else {

            // Usuário logado
            return new ModelAndView("redirect:/tela/painel_pesquisa_ponto");

        }

    }

    @GetMapping("/cadastro_funcionario")
    public ModelAndView cadastro() {

        ModelAndView mv = new ModelAndView("cadastro_funcionario");
        mv.addObject("funcionario", new Funcionarios());
        return mv;

    }

    @GetMapping("/cadastro_funcionario/{id}")
    public ModelAndView abreCadastro(@PathVariable("id") long id) {

        Optional<Funcionarios> cadastro = funcionariosRepository.findById(id);
        ModelAndView mv = new ModelAndView("cadastro_funcionario");

        if (cadastro.isEmpty()) {

            mv.addObject("funcionario", new Funcionarios());

        } else {

            mv.addObject("funcionario", cadastro.get());

        }

        return mv;

    }

    @GetMapping("/pesquisa_funcionario")
    public ModelAndView pesquisar(@RequestParam(value = "cpf", required = false) String cpf) {

        ModelAndView mv = new ModelAndView("pesquisa_funcionario");

        if (cpf != null && !cpf.isEmpty()) {

            // Usando o método com LIKE
            List<Funcionarios> funcionarios = funcionariosRepository.findByCpfContaining(cpf);

            if (!funcionarios.isEmpty()) {

                mv.addObject("resultados", funcionarios);

            } else {

                mv.addObject("resultados", List.of()); // Se não encontrar, retorna lista vazia

            }

        } else {

            mv.addObject("resultados", funcionariosRepository.findAll());

        }

        return mv;

    }

    @GetMapping("/cadastro_turno")
    public ModelAndView cadastroTurno() {

        ModelAndView mv = new ModelAndView("cadastro_turno");
        mv.addObject("turno", new Turnos());
        return mv;

    }

    @GetMapping("/cadastro_turno/{id}")
    public ModelAndView abreCadastroTurno(@PathVariable("id") long id) {

        Optional<Turnos> cadastro = turnosRepository.findById(id);
        ModelAndView mv = new ModelAndView("cadastro_turno");

        if (cadastro.isEmpty()) {

            mv.addObject("turno", new Turnos());

        } else {

            mv.addObject("turno", cadastro.get());

        }

        return mv;

    }

    @GetMapping("/pesquisa_turno")
    public ModelAndView pesquisarTurno() {

        ModelAndView mv = new ModelAndView("pesquisa_turno");
        mv.addObject("turnos", turnosRepository.findAll());
        return mv;

    }

    @GetMapping("/cadastro_justificativa")
    public ModelAndView cadastroJustificativa() {

        ModelAndView mv = new ModelAndView("cadastro_justificativa");
        mv.addObject("justificativa", new Justificativa());
        mv.addObject("listaFuncionarios", funcionariosRepository.findAll());
        return mv;

    }

    @GetMapping("/cadastro_justificativa/{id}")
    public ModelAndView abreCadastroJustificativa(@PathVariable("id") long id) {

        Optional<Justificativa> cadastro = justificativaRepository.findById(id);
        ModelAndView mv = new ModelAndView("cadastro_justificativa");

        // Verificar se a justificativa existe
        if (cadastro.isPresent()) {

            mv.addObject("justificativa", cadastro.get());

        } else {

            mv.addObject("justificativa", new Justificativa());

        }

        // Passar a lista de funcionários para o select
        mv.addObject("listaFuncionarios", funcionariosRepository.findAll());

        return mv;

    }

    @GetMapping("/pesquisa_justificativa")
    public ModelAndView pesquisarJustificativa(
            @RequestParam(value = "idFuncionario", required = false) Long idFuncionario,
            @RequestParam(value = "dataInicio", required = false) String dataInicioStr,
            @RequestParam(value = "dataFim", required = false) String dataFimStr) {

        ModelAndView mv = new ModelAndView("pesquisa_justificativa");
        List<Justificativa> justificativas = new ArrayList<>();

        mv.addObject("listaFuncionarios", funcionariosRepository.findAll());

        LocalDate dataInicio = null;
        LocalDate dataFim = null;

        try {

            if (dataInicioStr != null && !dataInicioStr.isEmpty()) {
                dataInicio = LocalDate.parse(dataInicioStr);
            }

            if (dataFimStr != null && !dataFimStr.isEmpty()) {
                dataFim = LocalDate.parse(dataFimStr);
            }

            // Se data final for nula, usa a data inicial
            if (dataInicio != null && dataFim == null) {
                dataFim = dataInicio;
            }

        } catch (Exception ignored) {

        }

        // Isto evita consultar todos os registros
        if (idFuncionario != null) {

            justificativas = justificativaRepository.buscarPorFiltros(idFuncionario, dataInicio, dataFim);

        }

        mv.addObject("justificativas", justificativas);
        mv.addObject("idFuncionario", idFuncionario);
        mv.addObject("dataInicio", dataInicio);
        mv.addObject("dataFim", dataFim);

        return mv;

    }

    @GetMapping("/cadastro_ponto")
    public ModelAndView cadastroPonto() {

        ModelAndView mv = new ModelAndView("cadastro_ponto");
        mv.addObject("ponto", new Ponto());
        mv.addObject("listaFuncionarios", funcionariosRepository.findAll());
        return mv;

    }

    @GetMapping("/cadastro_ponto/{id}")
    public ModelAndView abreCadastroPonto(@PathVariable("id") long id) {

        Optional<Ponto> cadastro = pontoRepository.findById(id);
        ModelAndView mv = new ModelAndView("cadastro_ponto");

        if (cadastro.isPresent()) {

            mv.addObject("ponto", cadastro.get());

        } else {

            mv.addObject("ponto", new Ponto());

        }

        mv.addObject("listaFuncionarios", funcionariosRepository.findAll());
        return mv;

    }

    @GetMapping("/pesquisa_ponto")
    public ModelAndView pesquisarPonto(
            @RequestParam(value = "idFuncionario", required = false) Long idFuncionario,
            @RequestParam(value = "dataInicio", required = false) String dataInicioStr,
            @RequestParam(value = "dataFim", required = false) String dataFimStr) {

        ModelAndView mv = new ModelAndView("pesquisa_ponto");
        List<Ponto> pontos = new ArrayList<>();

        mv.addObject("listaFuncionarios", funcionariosRepository.findAll());

        LocalDate dataInicio = null;
        LocalDate dataFim = null;

        try {
            if (dataInicioStr != null && !dataInicioStr.isEmpty()) {
                dataInicio = LocalDate.parse(dataInicioStr);
            }
            if (dataFimStr != null && !dataFimStr.isEmpty()) {
                dataFim = LocalDate.parse(dataFimStr);
            }

            // Se data final for nula, use a mesma da data inicial
            if (dataInicio != null && dataFim == null) {
                dataFim = dataInicio;
            }

        } catch (Exception ignored) {
        }

        // Isto evita consultar todos os registros
        if (idFuncionario != null) {

            pontos = pontoRepository.buscarPorFiltros(idFuncionario, dataInicio, dataFim);

        }

        mv.addObject("pontos", pontos);
        mv.addObject("idFuncionario", idFuncionario);
        mv.addObject("dataInicio", dataInicio);
        mv.addObject("dataFim", dataFim);

        return mv;

    }

    @GetMapping("/painel_pesquisa_ponto")
    public ModelAndView pesquisarPainelPonto(
            @RequestParam(value = "idFuncionario", required = false) Long idFuncionario,
            @RequestParam(value = "dataInicio", required = false) String dataInicioStr,
            @RequestParam(value = "dataFim", required = false) String dataFimStr) {

        ModelAndView mv = new ModelAndView("painel_pesquisa_ponto");
        List<Object[]> resultados = new ArrayList<>();

        // Parse das datas
        LocalDate dataInicio = null;
        LocalDate dataFim = null;

        try {

            // Se dataInicio não for informada, pega a data de hoje
            if (dataInicioStr != null && !dataInicioStr.isEmpty()) {

                dataInicio = LocalDate.parse(dataInicioStr);

            } else {

                // Atribui a data de hoje
                dataInicio = LocalDate.now();

            }

            if (dataFimStr != null && !dataFimStr.isEmpty()) {

                dataFim = LocalDate.parse(dataFimStr);

            }

            // Se data final for nula, use a mesma da data inicial
            if (dataInicio != null && dataFim == null) {

                dataFim = dataInicio;

            }

        } catch (Exception ignored) {

        }

        // Buscar funcionários e pontos, usando LEFT JOIN
        resultados = pontoRepository.buscarFuncionariosComPontos(dataInicio, dataFim);

        mv.addObject("funcionariosComPonto", resultados);
        mv.addObject("idFuncionario", idFuncionario);
        mv.addObject("dataInicio", dataInicio);
        mv.addObject("dataFim", dataFim);

        return mv;

    }

    @GetMapping("/login")
    public ModelAndView login(HttpSession session) {

        ModelAndView mv;

        if (session.getAttribute("usuarioLogado") == null) {

            // Usuário NÃO logado
            mv = new ModelAndView("login");
            mv.addObject("mensagemLogin", session.getAttribute("mensagemLogin"));
            mv.addObject("usuario", new Usuario());

        } else {

            // Usuário logado
            mv = new ModelAndView("redirect:/tela/home");

        }

        return mv;

    }

    @GetMapping("/recupera_senha_usuario")
    public ModelAndView recuperarSenha() {

        ModelAndView mv = new ModelAndView("recupera_senha_usuario");
        mv.addObject("usuario", new Usuario());
        return mv;

    }

    @GetMapping("/cadastro_usuario")
    public ModelAndView cadastroUsuario(HttpSession session) {

        ModelAndView mv = new ModelAndView("cadastro_usuario");
        mv.addObject("mensagemUsuarioExiste", session.getAttribute("mensagemUsuarioExiste"));

        if (session.getAttribute("usuarioLogado") == null) {

            // Novo cadastro
            mv.addObject("usuario", new Usuario());

        } else {

            // Recupera o usuário logado
            mv.addObject("usuario", session.getAttribute("usuarioLogado"));

        }

        return mv;

    }

    /* baixa o relatório aqui */
    @GetMapping("/baixar-relatorio")
    public ResponseEntity<Resource> baixarRelatorio() {

        String caminho = fileUploadService.getUploadDirectory() + "/relatorio_horas_trabalhadas.pdf";
        Path path = Paths.get(caminho);

        if (!Files.exists(path)) {

            return ResponseEntity.notFound().build();

        }

        try {

            ByteArrayResource resource = new ByteArrayResource(Files.readAllBytes(path));

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=relatorio_horas_trabalhadas.pdf")
                    .contentType(MediaType.APPLICATION_PDF)
                    .contentLength(Files.size(path))
                    .body(resource);

        } catch (Exception ex) {

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();

        }

    }

    /* Relacionado ao relatório */
    @GetMapping("/pesquisa_relatorio")
    public ModelAndView pesquisa_relatorio(
            @RequestParam(value = "idFuncionario", required = false) Long idFuncionario,
            @RequestParam(value = "dataInicio", required = false) String dataInicioStr,
            @RequestParam(value = "dataFim", required = false) String dataFimStr) {

        ModelAndView mv = new ModelAndView("pesquisa_relatorio");
        List<Ponto> pontos = new ArrayList<>();
        Turnos turno;

        try {

            turno = turnosRepository.findAll().get(0);

        } catch (Exception ex) {

            turno = new Turnos();

        }

        BigDecimal totalHoras = new BigDecimal(0);
        BigDecimal totalHorasExtras = new BigDecimal(0);

        mv.addObject("listaFuncionarios", funcionariosRepository.findAll());

        LocalDate dataInicio = null;
        LocalDate dataFim = null;

        try {

            if (dataInicioStr != null && !dataInicioStr.isEmpty()) {

                dataInicio = LocalDate.parse(dataInicioStr);

            }

            if (dataFimStr != null && !dataFimStr.isEmpty()) {

                dataFim = LocalDate.parse(dataFimStr);

            }

            // Se data final for nula, use a mesma da data inicial
            if (dataInicio != null && dataFim == null) {

                dataFim = dataInicio;

            }

        } catch (Exception ignored) {

        }

        // Isto evita consultar todos os registros
        if (idFuncionario != null) {

            pontos = pontoRepository.buscarPorFiltros(idFuncionario, dataInicio, dataFim);

        }

        // Calcula as horas que a empresa deve trabalhar diariamente
        double horasDeveTrabalhar = CalculaHoras.calcularHorasTrabalhadas(turno.getEntradaPadrao(),
                turno.getPausaPadrao(),
                turno.getRetornoPadrao(), turno.getSaidaPadrao());

        // Lista os pontos batidos pelo funcionario
        for (Ponto p : pontos) {

            // Calcula as horas reais trabalhadas pelo funcionário
            double horasTrabalhadas = CalculaHoras.calcularHorasTrabalhadasFuncionario(
                    p.getHorarioEntrada(),
                    p.getHorarioPausa(),
                    p.getHorarioRetorno(),
                    p.getHorarioSaida());

            // Arredonda horas trabalhadas
            horasTrabalhadas = new BigDecimal(horasTrabalhadas).setScale(2, RoundingMode.HALF_UP).doubleValue();

            // Calcula o saldo (horas extras)
            double saldoHoras = horasTrabalhadas - horasDeveTrabalhar;

            // Arredonda saldo
            saldoHoras = new BigDecimal(saldoHoras).setScale(2, RoundingMode.HALF_UP).doubleValue();

            // Popula as horas
            p.setHorasTrabalhadas(horasTrabalhadas);
            p.setHorasExtras(saldoHoras);

            // Atualiza o saldo
            totalHoras = totalHoras.add(new BigDecimal(horasTrabalhadas));
            totalHorasExtras = totalHorasExtras.add(new BigDecimal(saldoHoras));

            // Arredonda horas
            totalHoras = totalHoras.setScale(2, RoundingMode.HALF_UP);
            totalHorasExtras = totalHorasExtras.setScale(2, RoundingMode.HALF_UP);

        }

        mv.addObject("pontos", pontos);
        mv.addObject("idFuncionario", idFuncionario);
        mv.addObject("dataInicio", dataInicio);
        mv.addObject("dataFim", dataFim);
        mv.addObject("totalHoras", totalHoras);
        mv.addObject("totalHorasExtras", totalHorasExtras);

        if (idFuncionario != null) {

            // Gera o relatório
            Optional<Funcionarios> funcionarOptional = funcionariosRepository.findById(idFuncionario);

            if (funcionarOptional.isPresent()) {

                String pathPdf = fileUploadService.getUploadDirectory() + "relatorio_horas_trabalhadas";
                RelatorioHorasTrabalhadasPdf relatorio = new RelatorioHorasTrabalhadasPdf(funcionarOptional.get());
                boolean isGerouRelatorio = relatorio.gerarRelatorioPdf(pontos, totalHoras, totalHorasExtras, pathPdf);

                if (isGerouRelatorio) {

                    mv.addObject("pdfDownloadLink", "/tela/baixar-relatorio");

                } else {

                    mv.addObject("pdfDownloadLink", null);

                }

            }

        }

        return mv;

    }

}
