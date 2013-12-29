package br.unirio.transparencia.model;

import static com.googlecode.objectify.ObjectifyService.ofy;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;

/**
 * Classe de modelo que representa um escopo de um organização. O Escopo é um objeto persistido, por isso utilizamos o nome entidade.
 * 
 
 * 
 * <p>Essa entidade é mapeada com anotações do <code>Objectify</code>, um framework para persistência alto-nível no datastore (mecanismo de persistência do <code>App Engine</code>).</p>
 * 
 * 
 */
@Entity
public class Escopo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	private Long id;
	@Index
	private String nome;
	@Index
	private String descricao;
	private List<Avaliacao> avaliacoes = new ArrayList<Avaliacao>();
	@Index
	Key<Organizacao> keyOrganizacao;
    
	public Key<Organizacao> getKeyOrganizacao() {
		return keyOrganizacao;
	}

	public void setKeyOrganizacao(Key<Organizacao> keyOrganizacao) {
		this.keyOrganizacao = keyOrganizacao;
	}

	
	
	
	
	
	public Organizacao getOrganizacao() {
		if (this.keyOrganizacao != null){
			return ofy().load().key(this.getKeyOrganizacao()).get();
		}
		return null;
	}

	public void setOrganizacao(Organizacao organizacao) {
	 if (organizacao!=null)	
		this.keyOrganizacao =Key.create(Organizacao.class, organizacao.getId());
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	public Long getId() {
		return id;
	}
	
	


	
	public Escopo() {
	  if (this.getId()!=null)
	  {
		Key<Escopo> keyEscopo = Key.create(Escopo.class, this.getId());	
		this.avaliacoes=ofy().load().type(Avaliacao.class).filter("keyEscopo", keyEscopo).list();
	  }
	}
	
	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public List<Avaliacao> getAvaliacoes() {
	  
		if (this.getId()!=null)
		  {  
			Key<Escopo> keyEscopo = Key.create(Escopo.class, this.getId());  
		    this.avaliacoes=ofy().load().type(Avaliacao.class).filter("keyEscopo", keyEscopo).list();
	      }
	  
	return avaliacoes;
	}

	public void setAvaliacoes(List<Avaliacao> avaliacoes) {
		this.avaliacoes = avaliacoes;
	}

	public void addAvaliacao(Avaliacao avaliacao){
		if (!avaliacoes.contains(avaliacao))
		   avaliacoes.add(avaliacao);
		else
			avaliacoes.add(avaliacoes.indexOf(avaliacao), avaliacao);
	}
	
	public void removeAvaliacao(Avaliacao avaliacao){
		avaliacoes.remove(avaliacao);
	}




	
}
