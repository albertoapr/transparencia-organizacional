package br.unirio.transparencia.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


import com.googlecode.objectify.Key;
import com.googlecode.objectify.Ref;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Load;

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
	@Load
	Ref<Organizacao> organizacao;
	
	private String escopo;
	private Date dataAvaliacao;
	private Date dataValidade;
	private String resumo; //arquivo com o documento resumo
	private String declaracao; //arquivo com o documento de declaração
	public String getResumo() {
		return resumo;
	}

	public void setResumo(String resumo) {
		this.resumo = resumo;
	}

	public String getDeclaracao() {
		return declaracao;
	}

	public void setDeclaracao(String declaracao) {
		this.declaracao = declaracao;
	}

	private NivelTransparencia nivelTransparencia;
	@Load
	Ref<Profissional> avaliadorLider;
	
	@Load
	List<Ref<Profissional>> avaliadores = new ArrayList<Ref<Profissional>>();
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
	
	public Organizacao getOrganizacao() {
		return organizacao.get();
	}

	public void setOrganizacao(Organizacao organizacao) {
		this.organizacao  = Ref.create(organizacao); ;
	}

	public Profissional getAvaliadorLider() {
		return avaliadorLider.get();
	}

	public void setAvaliadorLider(Profissional avaliadorLider) {
		this.avaliadorLider = Ref.create(avaliadorLider);
	}

	public List<Profissional> getProfissionais() {
		List<Profissional> list = new ArrayList<Profissional>();
		for (Ref<Profissional> ref : avaliadores)
			list.add(ref.get());
		return list;
	}

	public void setProfissionais(List<Profissional> avaliadores) {
		for (Profissional prof : avaliadores)
		  this.avaliadores.add(Ref.create(prof));
	}




	
}
