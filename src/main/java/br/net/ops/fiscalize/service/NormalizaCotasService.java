package br.net.ops.fiscalize.service;

import br.net.ops.fiscalize.business.NormalizaCotasParlamentares;
import br.net.ops.fiscalize.business.SalvarImagens;
import br.net.ops.fiscalize.domain.SingletonParametros;
import br.net.ops.fiscalize.util.Utilidade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class NormalizaCotasService {

    private Logger logger = Utilidade.getLogger();

    @Autowired
    private NormalizaCotasParlamentares normalizaCotasParlamentares;

    @Autowired
    private SalvarImagens salvarImagens;

    @PostConstruct
    public void init() {
        // Normaliza banco de dados para o site cotas parlamentares
        normalizaCotasParlamentares.normalizarBancoDados();

        String diretorioImagens = SingletonParametros.getInstance().getCaminhoImagens();
        if (diretorioImagens != null) {
            salvarImagens.salvar(diretorioImagens);
        }

        logger.log(Level.INFO, "Fim do processo! Normalização gerada com sucesso!");
    }
}
