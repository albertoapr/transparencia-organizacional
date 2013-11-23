package br.unirio.transparencia.model;

import java.io.Serializable;
import java.util.Date;

import br.unirio.transparencia.dao.organizacao.OrganizacaoDAO;
import br.unirio.transparencia.dao.organizacao.OrganizacaoDAOObjectify;
import br.unirio.transparencia.dao.profissional.ProfissionalDAO;
import br.unirio.transparencia.dao.profissional.ProfissionalDAOObjectify;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Ignore;



@Entity
public class Avaliacao implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	private Long id;
	
   
    
    Key<Organizacao> keyOrganizacao;
    @Ignore
    private Organizacao organizacao;
    
    Key<Profissional> keyAvaliador;
    public Key<Organizacao> getKeyOrganizacao() {
		return keyOrganizacao;
	}



	public void setKeyOrganizacao(Key<Organizacao> keyOrganizacao) {
		this.keyOrganizacao = keyOrganizacao;
	}



	public Key<Profissional> getKeyAvaliador() {
		return keyAvaliador;
	}



	public void setKeyAvaliador(Key<Profissional> keyAvaliador) {
		this.keyAvaliador = keyAvaliador;
	}

	@Ignore
    private Profissional avaliador;
    
	private String patrocinador;
	
	private String escopo;
	
	private Date dataAvaliacao;
	
	private Date dataValidade;
	
	private String resultado; 
	
	private String declaracao; 
	
	private NivelTransparencia nivelTransparencia;
	
	public String getDeclaracao(){
		return "/documentos/avaliacoes/declaracao/"+String.valueOf(getId())+".odt";
	}
	
	public String getResultado(){
		return "/documentos/avaliacoes/resultado/"+String.valueOf(getId())+".odt";
	}
	
	public Avaliacao() {
		this.avaliador =new Profissional();
		this.organizacao =new Organizacao();
		this.dataAvaliacao = new Date();
		this.dataValidade =new Date();
		
		
	}
	
 

	public Organizacao getOrganizacao() {
		if (keyOrganizacao != null){
			OrganizacaoDAO dao = new OrganizacaoDAOObjectify();
			return dao.findByKey(keyOrganizacao);
		}
		return organizacao;
	}

	public void setOrganizacao(Organizacao organizacao) {
		this.organizacao = organizacao;
	}



	public Profissional getAvaliador() {
		if (keyAvaliador != null){
			ProfissionalDAO dao = new ProfissionalDAOObjectify();
			return dao.findByKey(keyAvaliador);
		}
		return avaliador;
	}

	public void setAvaliador(Profissional avaliador) {
		this.avaliador = avaliador;
	}

	
	


	public void setResultado(String resultado) {
		this.resultado = resultado;
	}



	public void setDeclaracao(String declaracao) {
		this.declaracao = declaracao;
	}

	
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public Long getId() {
		return id;
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


	
}
