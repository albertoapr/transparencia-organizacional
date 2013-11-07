package br.unirio.transparencia.model;

import java.io.Serializable;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;

/**
 * Classe de modelo que representa uma mercadoria. A mercadoria é um objeto persistido, por isso utilizamos o nome entidade.
 * 
 * <p>As funcionalidades desse sistema demonstração são concentradas no cadastro (CRUD) de mercadorias.</p>
 * 
 * <p>Essa entidade é mapeada com anotações do <code>Objectify</code>, um framework para persistência alto-nível no datastore (mecanismo de persistência do <code>App Engine</code>).</p>
 * 
 * @author YaW Tecnologia
 */
@Entity
public class Mercadoria implements Serializable {

	@Id
	private Long id;
	
	private String nome;
	
	private String descricao;
	
	private Integer quantidade;
	
	private Double preco;
	
	public Mercadoria() {
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

	public void setId(Long id) {
		this.id = id;
	}
	
	public Long getId() {
		return id;
	}
	
	public Double getPreco() {
		return preco;
	}
	
	public void setPreco(Double preco) {
		this.preco = preco;
	}
	
	public Integer getQuantidade() {
		return quantidade;
	}
	
	public void setQuantidade(Integer quantidade) {
		this.quantidade = quantidade;
	}
	
}
