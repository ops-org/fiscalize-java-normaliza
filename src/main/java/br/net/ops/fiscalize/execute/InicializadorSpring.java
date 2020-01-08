package br.net.ops.fiscalize.execute;

import br.net.ops.fiscalize.domain.SingletonParametros;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class InicializadorSpring {

    private static final String CONFIG_PATH = "classpath*:application-config.xml";

    // Teste a partir dessa classe. Ao iniciar o spring, o service anotado com @PostConstruct vai iniciar também.
    public static void main(String[] args) {

        if (args.length >= 1) {

            int i = 0;
            String strCaminhoLog = args[i++];

            // Singleton
            SingletonParametros sParametros = SingletonParametros.getInstance();
            sParametros.setCaminhoLog(strCaminhoLog);

            if (args.length == 2) {
                String strCaminhoImagens = args[i++];
                sParametros.setCaminhoImagens(strCaminhoImagens);
            }

            new ClassPathXmlApplicationContext(CONFIG_PATH);

        } else {
            printUsage();
        }

    }

    public static void printUsage() {
        System.out.println("Impossível iniciar sincronizador! Passe 2 parâmetros: "
                + "\n1) Caminho completo do arquivo de log. "
                + "\n2) (Opcional) Caminho do diretório para salvar as imagens de fotos deputados e partidos. "
                + "\n Exemplo: java -jar normalizacotas.jar c:\\log.xml c:\\imagens\\");
    }

}
