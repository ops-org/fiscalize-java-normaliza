package br.net.ops.fiscalize.domain;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@Entity
public class Parlamentar {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Integer parlamentarId;
	private String nome;
	private String nomeCivil;
	private String email;
	private String profissao;
	private Integer ideCadastro;
	
	@ManyToOne
	@JoinColumn(name="partidoId")
	private Partido partido;

	@OneToMany(mappedBy="parlamentar", fetch = FetchType.LAZY)
	private List<NotaFiscal> notasFiscais = null;
	
	public static Integer retornarIdeCadastro(Object object) {
		Integer retorno;
		try {
			String ideCadastro = object.toString();
			retorno = Integer.parseInt(ideCadastro);
		} catch(NumberFormatException | NullPointerException e) {
			retorno = null;
		}
		return retorno;
	}
	
	public Integer getParlamentarId() {
		return parlamentarId;
	}

	public String toString() {
		return String.valueOf(nome);
	}
	
	public void setParlamentarId(Integer parlamentarId) {
		this.parlamentarId = parlamentarId;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getNomeCivil() {
		return nomeCivil;
	}

	public void setNomeCivil(String nomeCivil) {
		this.nomeCivil = nomeCivil;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getProfissao() {
		return profissao;
	}

	public void setProfissao(String profissao) {
		this.profissao = profissao;
	}

	public Partido getPartido() {
		return partido;
	}

	public void setPartido(Partido partido) {
		this.partido = partido;
	}

	public Integer getIdeCadastro() {
		return ideCadastro;
	}

	public void setIdeCadastro(Integer ideCadastro) {
		this.ideCadastro = ideCadastro;
	}
	
}
