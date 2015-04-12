package br.net.ops.fiscalize.domain;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class Partido {

	public static final String SEM_PARTIDO = "S.PARTIDO";
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Integer partidoId;
	private String sigla;
	private String nome;
	
	@OneToMany(mappedBy="partido", fetch = FetchType.LAZY)
	private List<Parlamentar> parlamentares = null;

	public String toString() {
		return String.valueOf(nome) + " - " + String.valueOf(sigla);
	}
	
	// Object = objeto retornado do SQLQuery
	public static String retornarPartidoNotNull(Object object) {
		String partido;
		if(object==null) {
			partido = null;
		} else {
			partido = object.toString();
		}
		return retornarPartidoNotNull(partido);
	}
	
	public static String retornarPartidoNotNull(String partido) {
		if(partido==null || partido.equals("")) {
			partido = Partido.SEM_PARTIDO;
		}
		return partido;
	}
	
	public Integer getPartidoId() {
		return partidoId;
	}

	public void setPartidoId(Integer partidoId) {
		this.partidoId = partidoId;
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

	public List<Parlamentar> getParlamentares() {
		return parlamentares;
	}

	public void setParlamentares(List<Parlamentar> parlamentares) {
		this.parlamentares = parlamentares;
	}
	
}
