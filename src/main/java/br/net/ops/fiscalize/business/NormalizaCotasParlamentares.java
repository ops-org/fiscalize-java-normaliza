package br.net.ops.fiscalize.business;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.hibernate.HibernateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import br.net.ops.fiscalize.dao.CotaDao;
import br.net.ops.fiscalize.dao.DespesaDao;
import br.net.ops.fiscalize.dao.NotaFiscalDao;
import br.net.ops.fiscalize.dao.ParlamentarDao;
import br.net.ops.fiscalize.dao.PartidoDao;
import br.net.ops.fiscalize.dao.UfDao;
import br.net.ops.fiscalize.domain.Cota;
import br.net.ops.fiscalize.domain.Despesa;
import br.net.ops.fiscalize.domain.NotaFiscal;
import br.net.ops.fiscalize.domain.Parlamentar;
import br.net.ops.fiscalize.domain.Partido;
import br.net.ops.fiscalize.domain.Uf;
import br.net.ops.fiscalize.exception.ExceptionBase;
import br.net.ops.fiscalize.util.Utilidade;

@Component
public class NormalizaCotasParlamentares {

	private Logger logger;
	
	@Autowired
	private DespesaDao despesaDao;
	
	@Autowired
	private UfDao ufDao;
	
	@Autowired
	private CotaDao cotaDao;
	
	@Autowired
	private PartidoDao partidoDao;
	
	@Autowired
	private ParlamentarDao parlamentarDao;
	
	@Autowired
	private NotaFiscalDao notaFiscalDao;
	
	public NormalizaCotasParlamentares() {
		this.logger = Utilidade.getLogger();
	}
	
	@Transactional
	public void normalizarBancoDados() {
		logger.log(Level.INFO, "Iniciando normalização do banco de dados...");

		long inicio = System.currentTimeMillis();
		
		List<Uf> ufs = normalizarUfs();
		List<Cota> cotas = normalizarCotas();
		List<Partido> partidos = normalizarPartidos();
		List<Parlamentar> parlamentares = normalizarParlamentares(partidos);
		
		normalizarNotasFiscais(ufs, cotas, parlamentares);
		
		logger.log(Level.INFO, "Tempo total da normalização: " + (System.currentTimeMillis()-inicio)/1000 + " segundos");
		
	}
	
	@Transactional
	private List<NotaFiscal> normalizarNotasFiscais(List<Uf> ufs, List<Cota> cotas, List<Parlamentar> parlamentares) {
		logger.log(Level.INFO, "Iniciando normalização das notas fiscais...");
		
		long ultimoLog = System.currentTimeMillis();
		int lidos = 0;
		int validos = 0;
		int iteracao = 0;
		long inicio = System.currentTimeMillis();
		
		// Recuperando do banco desnormalizado
		logger.log(Level.INFO, "Consultando despesas no banco de dados. Esta operação pode levar alguns minutos...");
		List<Despesa> despesas = despesaDao.list();
		logger.log(Level.INFO, "Despesas resgatadas do banco!");

		// Montando objetos do banco normalizado
		List<NotaFiscal> notasFiscais = new ArrayList<NotaFiscal>();
		for(Despesa despesa:despesas) {
			NotaFiscal notaFiscal = new NotaFiscal();
			
			try {
				notaFiscal.setDataEmissao(NotaFiscal.retornarDataEmissao(despesa.getDatEmissao()));
				notaFiscal.setTipoDocumentoFiscal(NotaFiscal.retornarTipoDocumento(despesa.getIndTipoDocumento()));
				notaFiscal.setAno(NotaFiscal.retornarAno(despesa.getNumAno()));
				notaFiscal.setMes(NotaFiscal.retornarMes(despesa.getNumMes()));
				notaFiscal.setParcela(NotaFiscal.retornarParcela(despesa.getNumParcela()));
				notaFiscal.setCpfCnpj(despesa.getTxtCNPJCPF());
				notaFiscal.setDescricao(despesa.getTxtDescricao());
				notaFiscal.setDescricaoSubCota(despesa.getTxtDescricaoEspecificacao());
				notaFiscal.setBeneficiario(despesa.getTxtBeneficiario());
				notaFiscal.setNumeroDocumento(despesa.getTxtNumero());
				notaFiscal.setNomePassageiro(despesa.getTxtPassageiro());
				notaFiscal.setTrechoViagem(despesa.getTxtTrecho());
				notaFiscal.setValor(NotaFiscal.retornarValor(despesa.getVlrDocumento()));
				notaFiscal.setValor(NotaFiscal.retornarValorGlosa(despesa.getVlrGlosa()));
				notaFiscal.setValor(NotaFiscal.retornarValorLiquido(despesa.getVlrLiquido()));
				
				for(Uf uf:ufs) {
					if(uf.getSigla().equalsIgnoreCase(Uf.retornarUfNotNull(despesa.getSgUF()))) {
						notaFiscal.setUf(uf);
						break;
					}
				}
				
				for(Parlamentar parlamentar:parlamentares) {
					if(parlamentar.getNome().equalsIgnoreCase(despesa.getTxNomeParlamentar())) {
						if(parlamentar.getPartido().getSigla().equalsIgnoreCase(Partido.retornarPartidoNotNull(despesa.getSgPartido()))) {
							notaFiscal.setParlamentar(parlamentar);
						}
						break;
					}
				}
				
				for(Cota cota:cotas) {
					if(cota.getNome().equalsIgnoreCase(despesa.getTxtDescricao())) {
						notaFiscal.setCota(cota);
						break;
					}
				}
				
				if(notaFiscal.getValor().intValue()>=0) {
					if(isLegislaturaAtual(notaFiscal.getAno(), notaFiscal.getMes())) {
						notasFiscais.add(notaFiscal);	
					}
				} // else: notas com valor < 0 sao creditos, nao reembolso. Nao devem ser inseridas!
				
				lidos++;
				
				ultimoLog = imprimirLog(ultimoLog, "Normalizando Nota Fiscal (Objeto Java): " + lidos);
				
			} catch(ExceptionBase e) {
				logger.log(Level.WARNING, "Continuando a carga... (" + lidos + ")");
			}
		}
		
		logger.log(Level.INFO, "Preparando para salvar " + lidos + " notas fiscais no banco de dados!");
		
		// Salvando objetos normalizados
		for(NotaFiscal notaFiscal:notasFiscais) {
			try {
				List<NotaFiscal> notasExistentes = notaFiscalDao.findByExample(notaFiscal);
				if(notasExistentes.size()==0) {
					notaFiscal.setDataInclusao(new Date(System.currentTimeMillis()));
					notaFiscalDao.save(notaFiscal);
					validos++;
					ultimoLog = imprimirLog(ultimoLog, "Salvando em Banco Nota Fiscal: " + iteracao);
				} else {
					ultimoLog = imprimirLog(ultimoLog, "Nota Fiscal repetida: " + iteracao);
				}
				iteracao++;
			} catch(HibernateException e) {
				logger.log(Level.SEVERE, "Problemas ao salvar nota fiscal (iteracao: " + iteracao + ") - " + e.getMessage());
				throw new RuntimeException();
			}
		}
		
		logger.log(Level.INFO, "Tempo de salvamento das notas fiscais: " + (System.currentTimeMillis()-inicio)/1000 + " segundos");
        logger.log(Level.INFO, "Registros lidos: " + lidos);
        logger.log(Level.INFO, "Registros válidos: " + validos);
		
		return notasFiscais;
		
	}
	
	// 01/02/2015 a 31/01/2019 - Garantir apenas esta legislatura
	private boolean isLegislaturaAtual(int ano, int mes) {
		boolean retorno = true;
		if(ano<2015 || (ano==2015 && mes<2)) { // se menor que 02/2015
			retorno = false;
		}
		if(ano>2019 || (ano==2019 && mes>1)) { // se maior que 01/2019 
			retorno = false;
		}
		return retorno;
	}
	
	private long imprimirLog(long ultimoLog, String mensagem) {
		if(System.currentTimeMillis() - ultimoLog > Utilidade.LOG_INTERVAL) {
        	logger.log(Level.INFO, mensagem);
			return System.currentTimeMillis();
		}
		return ultimoLog;
	}
	
	@Transactional
	private List<Uf> normalizarUfs() {
		logger.log(Level.INFO, "Iniciando normalização das ufs...");
		
		int lidos = 0;
		int validos = 0;
		int iteracao = 0;
		long inicio = System.currentTimeMillis();
		
		// Recuperando do banco desnormalizado
		List<String> resultados = despesaDao.selecionarUfs();

		// Montando objetos do banco normalizado
		List<Uf> ufs = new ArrayList<Uf>();
		for(String resultado:resultados) {
			Uf uf = new Uf();
			
			String sigla = Uf.retornarUfNotNull(resultado);
			
			uf.setSigla(sigla);
			ufs.add(uf);
			
			lidos++;
		}
		
		// Salvando objetos normalizados
		for(Uf uf:ufs) {
			try {
				List<Uf> ufsExistentes = ufDao.findByExample(uf);
				if(ufsExistentes.size()==0) {
					ufDao.save(uf);
					validos++;
				}
				iteracao++;
			} catch(HibernateException e) {
				logger.log(Level.SEVERE, "Problemas ao salvar uf (iteracao: " + iteracao + ") - " + e.getMessage());
				throw new RuntimeException();
			}
		}
		
		logger.log(Level.INFO, "Tempo de salvamento das ufs: " + (System.currentTimeMillis()-inicio)/1000 + " segundos");
        logger.log(Level.INFO, "Registros lidos: " + lidos);
        logger.log(Level.INFO, "Registros válidos: " + validos);
		
		return ufs;
	}
	
	@Transactional
	private List<Cota> normalizarCotas() {
		logger.log(Level.INFO, "Iniciando normalização dos tipos de cotas...");
		
		int lidos = 0;
		int validos = 0;
		int iteracao = 0;
		long inicio = System.currentTimeMillis();
		
		// Recuperando do banco desnormalizado
		List<String> resultados = despesaDao.selecionarCotas();

		// Montando objetos do banco normalizado
		List<Cota> cotas = new ArrayList<Cota>();
		for(String resultado:resultados) {
			Cota cota = new Cota();
			
			String nome = Cota.retornarCotaNotNull(resultado);
			
			cota.setNome(nome);
			cotas.add(cota);
			
			lidos++;
		}
		
		// Salvando objetos normalizados
		for(Cota cota:cotas) {
			try {
				List<Cota> cotasExistentes = cotaDao.findByExample(cota);
				if(cotasExistentes.size()==0) {
					cotaDao.save(cota);
					validos++;
				}
				iteracao++;
			} catch(HibernateException e) {
				logger.log(Level.SEVERE, "Problemas ao salvar tipo de cota (iteracao: " + iteracao + ") - " + e.getMessage());
				throw new RuntimeException();
			}
		}
		
		logger.log(Level.INFO, "Tempo de salvamento dos tipos de cotas: " + (System.currentTimeMillis()-inicio)/1000 + " segundos");
        logger.log(Level.INFO, "Registros lidos: " + lidos);
        logger.log(Level.INFO, "Registros válidos: " + validos);
		
		return cotas;
	}
	
	@Transactional
	private List<Partido> normalizarPartidos() {
		logger.log(Level.INFO, "Iniciando normalização dos partidos...");
		
		int lidos = 0;
		int validos = 0;
		int iteracao = 0;
		long inicio = System.currentTimeMillis();
		
		// Recuperando do banco desnormalizado
		List<String> resultados = despesaDao.selecionarPartidos();

		// Montando objetos do banco normalizado
		List<Partido> partidos = new ArrayList<Partido>();
		for(String resultado:resultados) {
			Partido partido = new Partido();
			
			String sigla = Partido.retornarPartidoNotNull(resultado);
			
			partido.setSigla(sigla);
			partidos.add(partido);
			
			lidos++;
		}
		
		// Salvando objetos normalizados
		for(Partido partido:partidos) {
			try {
				List<Partido> partidosExistentes = partidoDao.findByExample(partido);
				if(partidosExistentes.size()==0) {
					partidoDao.save(partido);
					validos++;
				}
				iteracao++;
			} catch(HibernateException e) {
				logger.log(Level.SEVERE, "Problemas ao salvar partido (iteracao: " + iteracao + ") - " + e.getMessage());
				throw new RuntimeException();
			}
		}
		
		logger.log(Level.INFO, "Tempo de salvamento dos partidos: " + (System.currentTimeMillis()-inicio)/1000 + " segundos");
        logger.log(Level.INFO, "Registros lidos: " + lidos);
        logger.log(Level.INFO, "Registros válidos: " + validos);
		
		return partidos;
	}
	
	@Transactional
	private List<Parlamentar> normalizarParlamentares(List<Partido> partidos) {
		logger.log(Level.INFO, "Iniciando normalização dos parlamentares...");
		
		int lidos = 0;
		int validos = 0;
		int iteracao = 0;
		long inicio = System.currentTimeMillis();
		
		// Recuperando do banco desnormalizado
		List<Object[]> resultados = despesaDao.selecionarParlamentares();

		// Montando objetos do banco normalizado
		List<Parlamentar> parlamentares = new ArrayList<Parlamentar>();
		for(Object[] resultado:resultados) {
			
			String nomeParlamentar = resultado[0].toString();
			String siglaPartido = Partido.retornarPartidoNotNull(resultado[1]);
			Integer ideCadastro = Parlamentar.retornarIdeCadastro(resultado[2]);
			
			Parlamentar parlamentar = new Parlamentar();
			parlamentar.setNome(nomeParlamentar);
			parlamentar.setIdeCadastro(ideCadastro);
			
			// Selecionando partido do banco normalizado
			
			for(Partido partido:partidos) {
				if(partido.getSigla().equals(siglaPartido)) {
					parlamentar.setPartido(partido);
				}
			}
			parlamentares.add(parlamentar);
			
			lidos++;
		}
		
		// Salvando objetos normalizados
		for(Parlamentar parlamentar:parlamentares) {
			try {
				List<Parlamentar> parlamentaresExistentes = parlamentarDao.findByExample(parlamentar);
				if(parlamentaresExistentes.size()==0) {
					parlamentarDao.save(parlamentar);
					validos++;
				}
				iteracao++;
			} catch(HibernateException e) {
				logger.log(Level.SEVERE, "Problemas ao salvar parlamentar (iteracao: " + iteracao + ") - " + e.getMessage());
				throw new RuntimeException();
			}
		}
		
		logger.log(Level.INFO, "Tempo de salvamento dos parlamentares: " + (System.currentTimeMillis()-inicio)/1000 + " segundos");
        logger.log(Level.INFO, "Registros lidos: " + lidos);
        logger.log(Level.INFO, "Registros válidos: " + validos);
		
		return parlamentares;
	}
	
}