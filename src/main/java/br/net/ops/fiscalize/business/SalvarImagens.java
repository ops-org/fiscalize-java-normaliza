package br.net.ops.fiscalize.business;

import br.net.ops.fiscalize.dao.ParlamentarDao;
import br.net.ops.fiscalize.dao.PartidoDao;
import br.net.ops.fiscalize.domain.Parlamentar;
import br.net.ops.fiscalize.domain.Partido;
import br.net.ops.fiscalize.util.Utilidade;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Component
public class SalvarImagens {

    private static final String URL_PARLAMENTAR = "https://www.camara.leg.br/internet/deputado/bandep/";
    private static final String URL_PARTIDO = "https://www.camara.leg.br/Internet/Deputado/img/partidos/";

    private static final String EXTENSAO_PARLAMENTAR = ".jpg";
    private static final String EXTENSAO_PARTIDO = ".gif";

    private static final String DIRETORIO_PARLAMENTAR = "parlamentar";
    private static final String DIRETORIO_PARTIDO = "partido";

    private Logger logger;

    @Autowired
    private PartidoDao partidoDao;

    @Autowired
    private ParlamentarDao parlamentarDao;

    public SalvarImagens() {
        this.logger = Utilidade.getLogger();
    }

    @Transactional
    public void salvar(String diretorioImagens) {
        logger.log(Level.INFO, "Iniciando salvamento de imagens...");

        long inicio = System.currentTimeMillis();

        salvarImagensPartido(diretorioImagens);
        salvarImagensParlamentar(diretorioImagens);

        logger.log(Level.INFO, "Tempo total do salvamento de imagens: " + (System.currentTimeMillis() - inicio) / 1000 + " segundos");

    }

    public void salvarImagensPartido(String diretorioImagens) {
        logger.log(Level.INFO, "Iniciando salvamento de imagens de partidos...");

        List<Partido> partidos = partidoDao.list();

        long inicio = System.currentTimeMillis();
        int lidos = 0;
        int validos = 0;

        diretorioImagens = diretorioImagens + File.separator + DIRETORIO_PARTIDO + File.separator;
        File diretorioPartido = new File(diretorioImagens);
        if (!diretorioPartido.exists()) {
            if (!diretorioPartido.mkdirs()) {
                logger.log(Level.SEVERE, "Impossível criar diretório do partido! Verifique seu disco!");
                throw new RuntimeException();
            }
        }

        long ultimoLog = System.currentTimeMillis();
        for (Partido partido : partidos) {
            lidos++;

            String sigla = partido.getSigla();

            if (System.currentTimeMillis() - ultimoLog > Utilidade.LOG_INTERVAL) {
                logger.log(Level.INFO, "Salvando imagem do partido (" + lidos + "): " + sigla);
                ultimoLog = System.currentTimeMillis();
            }

            String nomeArquivo = sigla + EXTENSAO_PARTIDO;

            String url = URL_PARTIDO + nomeArquivo;
            String caminho = diretorioImagens + File.separator + nomeArquivo;

            try {
                FileUtils.copyURLToFile(new URL(url), new File(caminho));
                validos++;
            } catch (IOException e) {
                logger.log(Level.WARNING, "Erro ao salvar partido (" + lidos + "): " + sigla + ". " + e.getMessage());
            }

        }

        logger.log(Level.INFO, "Tempo de salvamento das imagens dos partidos: " + (System.currentTimeMillis() - inicio) / 1000 + " segundos");
        logger.log(Level.INFO, "Imagens lidas: " + lidos);
        logger.log(Level.INFO, "Imagens válidas: " + validos);

    }

    public void salvarImagensParlamentar(String diretorioImagens) {
        logger.log(Level.INFO, "Iniciando salvamento de fotos dos parlamentares...");

        List<Parlamentar> parlamentares = parlamentarDao.list();

        long inicio = System.currentTimeMillis();
        int lidos = 0;
        int validos = 0;
        int semImagem = 0;

        diretorioImagens = diretorioImagens + File.separator + DIRETORIO_PARLAMENTAR + File.separator;
        File diretorioParlamentar = new File(diretorioImagens);
        if (!diretorioParlamentar.exists()) {
            if (!diretorioParlamentar.mkdirs()) {
                logger.log(Level.SEVERE, "Impossível criar diretório do parlamentar! Verifique seu disco!");
                throw new RuntimeException();
            }
        }

        for (Parlamentar parlamentar : parlamentares) {
            lidos++;

            Integer ideCadastro = parlamentar.getIdeCadastro();

            if (ideCadastro != null) {

                logger.log(Level.INFO, "Salvando imagem do parlamentar (" + lidos + "): " + ideCadastro + " (" + parlamentar.getNome() + ")");

                String nomeArquivo = ideCadastro + EXTENSAO_PARLAMENTAR;

                String url = URL_PARLAMENTAR + nomeArquivo;
                String caminho = diretorioImagens + File.separator + nomeArquivo;

                try {
                    FileUtils.copyURLToFile(new URL(url), new File(caminho));
                    validos++;
                } catch (IOException e) {
                    logger.log(Level.WARNING, "Erro ao salvar parlamentar (" + lidos + "): " + ideCadastro + ". " + e.getMessage());
                }
            } else {
                semImagem++;
            }

        }

        logger.log(Level.INFO, "Tempo de salvamento das imagens dos parlamentares: " + (System.currentTimeMillis() - inicio) / 1000 + " segundos");
        logger.log(Level.INFO, "Registros lidos: " + lidos);
        logger.log(Level.INFO, "Registros válidos: " + validos);
        logger.log(Level.INFO, "Parlamentar sem imagem: " + semImagem);

    }

}