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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.ana.coutinho.ponto.model.Funcionarios;
import com.ana.coutinho.ponto.model.Ponto;
import com.ana.coutinho.ponto.model.Turnos;
import com.ana.coutinho.ponto.repository.FuncionariosRepository;
import com.ana.coutinho.ponto.repository.PontoRepository;
import com.ana.coutinho.ponto.repository.TurnosRepository;
import com.ana.coutinho.ponto.services.CalculaHoras;
import com.ana.coutinho.ponto.services.FileUploadService;
import com.ana.coutinho.ponto.services.RelatorioHorasTrabalhadasPdf;

@Controller
public class RelatorioController {

    @Autowired
    private FuncionariosRepository funcionariosRepository;

    @Autowired
    private TurnosRepository turnosRepository;

    @Autowired
    private PontoRepository pontoRepository;

    @Autowired
    private FileUploadService fileUploadService;

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

                    mv.addObject("pdfDownloadLink", "/baixar-relatorio");

                } else {

                    mv.addObject("pdfDownloadLink", null);

                }

            }

        }

        return mv;

    }

}
