package br.unirio.transparencia.model;

import java.io.Serializable;
import java.util.Date;


import com.googlecode.objectify.Key;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;

/**
 * Classe de modelo que representa uma organização. A organização é um objeto persistido, por isso utilizamos o nome entidade.
 * 
 * <p>As funcionalidades desse sistema demonstração são concentradas no cadastro (CRUD) de organizações.</p>
 * 
 * <p>Essa entidade é mapeada com anotações do <code>Objectify</code>, um framework para persistência alto-nível no datastore (mecanismo de persistência do <code>App Engine</code>).</p>
 * 
 * 
 */
@Entity
public class Avaliacao implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	private Long id;
	private String escopo;
	private Date dataAvaliacao;
	private Date dataValidade;
	private NivelTransparencia nivelTransparencia;
	Key<Profissional> avaliador;
	private String patrocinador;
	
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public Long getId() {
		return id;
	}
	
	
	public Avaliacao() {
	}
	
	
	
	public Date getDataAvaliacao() {
		return dataAvaliacao;
	}

	public void setDataAvaliacao(Date dataAvaliacao) {
		this.dataAvaliacao = dataAvaliacao;
	}

	public Date getDataValidade() {
		return dataValidade;
	}

	public void setDataValidade(Date dataValidade) {
		this.dataValidade = dataValidade;
	}

	public NivelTransparencia getNivelTransparencia() {
		return nivelTransparencia;
	}

	public void setNivelTransparencia(NivelTransparencia nivelTransparencia) {
		this.nivelTransparencia = nivelTransparencia;
	}

	public Key<Profissional> getAvaliador() {
		return avaliador;
	}

	public void setAvaliador(Key<Profissional> avaliador) {
		this.avaliador = avaliador;
	}

	public String getEscopo() {
		return escopo;
	}

	public void setEscopo(String escopo) {
		this.escopo = escopo;
	}

	public String getPatrocinador() {
		return patrocinador;
	}

	public void setPatrocinador(String patrocinador) {
		this.patrocinador = patrocinador;
	}




	
}
