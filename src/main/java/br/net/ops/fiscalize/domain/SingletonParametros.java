package br.net.ops.fiscalize.domain;

public class SingletonParametros {

	private String caminhoLog;
	private String caminhoImagens;
	
	private static SingletonParametros singleton = new SingletonParametros();
	
	private SingletonParametros() { 
		caminhoLog = null;
		caminhoImagens = null;
	}
	   
	public static SingletonParametros getInstance() {
		return singleton;
	}

	public String getCaminhoLog() {
		return caminhoLog;
	}

	public void setCaminhoLog(String caminhoLog) {
		this.caminhoLog = caminhoLog;
	}

	public String getCaminhoImagens() {
		return caminhoImagens;
	}

	public void setCaminhoImagens(String caminhoImagens) {
		this.caminhoImagens = caminhoImagens;
	}

}
