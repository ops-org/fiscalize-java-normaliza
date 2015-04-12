package br.net.ops.fiscalize.domain;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class Uf {

	public static final String SEM_SIGLA_UF = "XX";
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Integer ufId;
	private String sigla;
	private String nome;
	
	@OneToMany(mappedBy="cota", fetch = FetchType.LAZY)
	private List<NotaFiscal> notasFiscais = null;

	public String toString() {
		return String.valueOf(nome) + " - " + String.valueOf(sigla);
	}
	
	public static String retornarUfNotNull(String uf) {
		if(uf==null || uf.equals("")) {
			uf = Uf.SEM_SIGLA_UF;
		}
		return uf;
	}

	public Integer getUfId() {
		return ufId;
	}

	public void setUfId(Integer ufId) {
		this.ufId = ufId;
	}

	public String getSigla() {
		return sigla;
	}

	public void setSigla(String sigla) {
		this.sigla = sigla;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public List<NotaFiscal> getNotasFiscais() {
		return notasFiscais;
	}

	public void setNotasFiscais(List<NotaFiscal> notasFiscais) {
		this.notasFiscais = notasFiscais;
	}
	
}
