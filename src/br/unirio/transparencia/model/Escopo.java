package br.unirio.transparencia.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import br.unirio.transparencia.dao.avaliacao.AvaliacaoDAO;
import br.unirio.transparencia.dao.avaliacao.AvaliacaoDAOObjectify;
import br.unirio.transparencia.dao.organizacao.OrganizacaoDAO;
import br.unirio.transparencia.dao.organizacao.OrganizacaoDAOObjectify;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Ignore;
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
	List<Avaliacao> avaliacoes;
	
	private Key<Avaliacao>[] keysAvaliacoes;
	
	public Key<Avaliacao>[] getKeysAvaliacoes() {
		return keysAvaliacoes;
	}

	public void setKeysAvaliacoes(Key<Avaliacao>[] keysAvaliacoes) {
		this.keysAvaliacoes = keysAvaliacoes;
	}

	@Index
	Key<Organizacao> keyOrganizacao;
    public Key<Organizacao> getKeyOrganizacao() {
		return keyOrganizacao;
	}

	public void setKeyOrganizacao(Key<Organizacao> keyOrganizacao) {
		this.keyOrganizacao = keyOrganizacao;
	}

	@Ignore
    private Organizacao organizacao;
	
	
	
	
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

	public void setId(Long id) {
		this.id = id;
	}
	
	public Long getId() {
		return id;
	}
	
	


	
	public Escopo() {
		avaliacoes = new ArrayList<Avaliacao>();
		AvaliacaoDAO dao = new AvaliacaoDAOObjectify();
	   avaliacoes =  dao.getAllByEscopo(this);
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
		return avaliacoes;
	}

	public void setAvaliacoes(List<Avaliacao> avaliacoes) {
		this.avaliacoes = avaliacoes;
	}





	
}
