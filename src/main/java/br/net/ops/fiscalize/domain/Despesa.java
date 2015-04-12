package br.net.ops.fiscalize.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.hibernate.annotations.NamedQueries;
import org.hibernate.annotations.NamedQuery;

@NamedQueries({
	@NamedQuery(
	name = "findAllDespesas",
	query = "FROM Despesa despesa ORDER BY despesa.despesaId"
	)
})

@Entity
public class Despesa {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Integer despesaId;

	public String txNomeParlamentar;
	public String ideCadastro;
	public String nuCarteiraParlamentar;
	public String nuLegislatura;
	public String sgUF;
	public String sgPartido;
	public String codLegislatura;
	public String numSubCota;
	public String txtDescricao;
	public String numEspecificacaoSubCota;
	public String txtDescricaoEspecificacao;
	public String txtFornecedor;
	public String txtCNPJCPF;
	public String txtNumero;
	public String indTipoDocumento;
	public String datEmissao;
	public String vlrDocumento;
	public String vlrGlosa;
	public String vlrLiquido;
	public String numMes;
	public String numAno;
	public String numParcela;
	public String txtPassageiro;
	public String txtTrecho;
	public String numLote;
	public String numRessarcimento;
	
	public String toString() {
		return txNomeParlamentar + "(" + sgPartido + "/" + sgUF + ")";
	}
	
	public String getTxNomeParlamentar() {
		return txNomeParlamentar;
	}
	public void setTxNomeParlamentar(String txNomeParlamentar) {
		this.txNomeParlamentar = txNomeParlamentar;
	}
	public String getIdeCadastro() {
		return ideCadastro;
	}
	public void setIdeCadastro(String ideCadastro) {
		this.ideCadastro = ideCadastro;
	}
	public String getNuCarteiraParlamentar() {
		return nuCarteiraParlamentar;
	}
	public void setNuCarteiraParlamentar(String nuCarteiraParlamentar) {
		this.nuCarteiraParlamentar = nuCarteiraParlamentar;
	}
	public String getNuLegislatura() {
		return nuLegislatura;
	}
	public void setNuLegislatura(String nuLegislatura) {
		this.nuLegislatura = nuLegislatura;
	}
	public String getSgUF() {
		return sgUF;
	}
	public void setSgUF(String sgUF) {
		this.sgUF = sgUF;
	}
	public String getSgPartido() {
		return sgPartido;
	}
	public void setSgPartido(String sgPartido) {
		this.sgPartido = sgPartido;
	}
	public String getCodLegislatura() {
		return codLegislatura;
	}
	public void setCodLegislatura(String codLegislatura) {
		this.codLegislatura = codLegislatura;
	}
	public String getNumSubCota() {
		return numSubCota;
	}
	public void setNumSubCota(String numSubCota) {
		this.numSubCota = numSubCota;
	}
	public String getTxtDescricao() {
		return txtDescricao;
	}
	public void setTxtDescricao(String txtDescricao) {
		this.txtDescricao = txtDescricao;
	}
	public String getNumEspecificacaoSubCota() {
		return numEspecificacaoSubCota;
	}
	public void setNumEspecificacaoSubCota(String numEspecificacaoSubCota) {
		this.numEspecificacaoSubCota = numEspecificacaoSubCota;
	}
	public String getTxtDescricaoEspecificacao() {
		return txtDescricaoEspecificacao;
	}
	public void setTxtDescricaoEspecificacao(String txtDescricaoEspecificacao) {
		this.txtDescricaoEspecificacao = txtDescricaoEspecificacao;
	}
	public String getTxtFornecedor() {
		return txtFornecedor;
	}
	public void setTxtFornecedor(String txtFornecedor) {
		this.txtFornecedor = txtFornecedor;
	}
	public String getTxtCNPJCPF() {
		return txtCNPJCPF;
	}
	public void setTxtCNPJCPF(String txtCNPJCPF) {
		this.txtCNPJCPF = txtCNPJCPF;
	}
	public String getTxtNumero() {
		return txtNumero;
	}
	public void setTxtNumero(String txtNumero) {
		this.txtNumero = txtNumero;
	}
	public String getIndTipoDocumento() {
		return indTipoDocumento;
	}
	public void setIndTipoDocumento(String indTipoDocumento) {
		this.indTipoDocumento = indTipoDocumento;
	}
	public String getDatEmissao() {
		return datEmissao;
	}
	public void setDatEmissao(String datEmissao) {
		this.datEmissao = datEmissao;
	}
	public String getVlrDocumento() {
		return vlrDocumento;
	}
	public void setVlrDocumento(String vlrDocumento) {
		this.vlrDocumento = vlrDocumento;
	}
	public String getVlrGlosa() {
		return vlrGlosa;
	}
	public void setVlrGlosa(String vlrGlosa) {
		this.vlrGlosa = vlrGlosa;
	}
	public String getVlrLiquido() {
		return vlrLiquido;
	}
	public void setVlrLiquido(String vlrLiquido) {
		this.vlrLiquido = vlrLiquido;
	}
	public String getNumMes() {
		return numMes;
	}
	public void setNumMes(String numMes) {
		this.numMes = numMes;
	}
	public String getNumAno() {
		return numAno;
	}
	public void setNumAno(String numAno) {
		this.numAno = numAno;
	}
	public String getNumParcela() {
		return numParcela;
	}
	public void setNumParcela(String numParcela) {
		this.numParcela = numParcela;
	}
	public String getTxtPassageiro() {
		return txtPassageiro;
	}
	public void setTxtPassageiro(String txtPassageiro) {
		this.txtPassageiro = txtPassageiro;
	}
	public String getTxtTrecho() {
		return txtTrecho;
	}
	public void setTxtTrecho(String txtTrecho) {
		this.txtTrecho = txtTrecho;
	}
	public String getNumLote() {
		return numLote;
	}
	public void setNumLote(String numLote) {
		this.numLote = numLote;
	}
	public String getNumRessarcimento() {
		return numRessarcimento;
	}
	public void setNumRessarcimento(String numRessarcimento) {
		this.numRessarcimento = numRessarcimento;
	}
	
}
