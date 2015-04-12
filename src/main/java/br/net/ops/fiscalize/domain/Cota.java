package br.net.ops.fiscalize.domain;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class Cota {

	public static final String SEM_COTA = "NENHUMA COTA";
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Integer cotaId;
	private String nome;
	
	@OneToMany(mappedBy="cota", fetch = FetchType.LAZY)
	private List<NotaFiscal> notasFiscais = null;

	public static String retornarCotaNotNull(String cota) {
		if(cota==null || cota.equals("")) {
			cota = Cota.SEM_COTA;
		}
		return cota;
	}
	
	public String toString() {
		return String.valueOf(nome);
	}
	
	public Integer getCotaId() {
		return cotaId;
	}
	
	public void setCotaId(Integer cotaId) {
		this.cotaId = cotaId;
	}
	
	public String getNome() {
		return nome;
	}
	
	public void setNome(String nome) {
		this.nome = nome;
	}
	
}
