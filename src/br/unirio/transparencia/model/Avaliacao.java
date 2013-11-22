package br.unirio.transparencia.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import br.unirio.transparencia.dao.organizacao.OrganizacaoDAO;
import br.unirio.transparencia.dao.organizacao.OrganizacaoDAOObjectify;

import com.googlecode.objectify.Ref;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Ignore;
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
	Ref<Organizacao> organizacaoRef;
	@Ignore
	Organizacao organizacao;
	
	@Ignore
	Long organizacaoId;
	
	@Ignore
	Long avaliadorLiderId;
	
	public Profissional getAvaliadorLider() {
		return avaliadorLider;
	}

	public void setAvaliadorLider(Profissional avaliadorLider) {
		this.avaliadorLider = avaliadorLider;
	}

	public Organizacao getOrganizacao() {
		return organizacao;
	}

	public void setOrganizacao(Organizacao organizacao) {
		this.organizacao = organizacao;
	}

	@Load
	Ref<Profissional> avaliadorLiderRef;
	
	@Ignore
	Profissional avaliadorLider;
	
	@Load
	List<Ref<Profissional>> avaliadores = new ArrayList<Ref<Profissional>>();
	private String patrocinador;
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
	
	
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public Long getId() {
		return id;
	}
	
	
	public Avaliacao() {
		this.organizacao = getOrganizacaoRef();
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
	
	public Organizacao getOrganizacaoRef() {
		if (organizacao!=null)
		   return organizacaoRef.get();
		return null;
	}
/*
	public void setOrganizacaoRef() {
		this.organizacaoRef  = Ref.create(organizacao); 
	}
	*/
	public void setOrganizacaoRef() {
		OrganizacaoDAO dao = new OrganizacaoDAOObjectify();
		Organizacao org = dao.findById(organizacaoId);
		this.organizacaoRef  = Ref.create(org); 
	}
	
	public Long getOrganizacaoId() {
		return organizacaoId;
	}

	public void setOrganizacaoId(Long organizacaoId) {
		this.organizacaoId = organizacaoId;
	}

	public void setOrganizacaoRef(Ref<Organizacao> organizacaoRef) {
		this.organizacaoRef  = organizacaoRef; 
	}

	public Profissional getAvaliadorLiderRef() {
	 if (avaliadorLider!=null)	
		return avaliadorLiderRef.get();
	 return null;
	}

	public void setAvaliadorLiderRef() {
		this.avaliadorLiderRef = Ref.create(avaliadorLider);
	}
	
	public void setAvaliadorLiderRef(Ref<Profissional> avaliadorLiderRef) {
		this.avaliadorLiderRef = avaliadorLiderRef;
	}

	public List<Profissional> getProfissionais() {
		List<Profissional> list = new ArrayList<Profissional>();
		if (avaliadores!=null)
		{	
		for (Ref<Profissional> ref : avaliadores)
			list.add(ref.get());
		return list;
		}
		return null;
	}

	public void setProfissionais(List<Profissional> avaliadores) {
		for (Profissional prof : avaliadores)
		  this.avaliadores.add(Ref.create(prof));
	}




	
}
